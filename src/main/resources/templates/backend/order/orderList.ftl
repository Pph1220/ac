<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>阿C外卖</title>
    <script src="../script/JQuery.js" type="text/javascript"></script>
    <script src="../script/cropper.min.js" type="text/javascript"></script>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../css/cropper.min.css">
    <link rel="stylesheet" href="../css/ImgCropping.css">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse"
                            data-target="#bs-example-navbar-collapse-1"><span
                                class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span
                                class="icon-bar"></span><span class="icon-bar"></span></button>
                    <a class="navbar-brand" href="/productManager/getProductList">阿 C 外 卖</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="../newProduct.html">新增商品</a>
                        </li>
                        <li>
                            <a href="../orderManager/list">所有订单</a>
                        </li>
                    </ul>
                    <#--<form class="navbar-form navbar-left" role="search" action="/product/search" method="get">
                        <div class="form-group">
                            <input type="text" class="form-control" name="productName" />
                        </div> <button id="submit" type="submit" class="btn btn-default">搜索</button>
                    </form>-->
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="../../changePassword.html">修改密码</a>
                        </li>
                        <li>
                            <a href="#" onclick="logOut()">退出登录</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>
</div>
<#--${result.data}-->
<div class="container">
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column">
            <div style="height: 10px;"></div>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th style="text-align: center">编号</th>
                    <th style="text-align: center">订单编号</th>
                    <th style="text-align: center">订单总价</th>
                    <th style="text-align: center">创建时间</th>
                    <th style="text-align: center">订单状态</th>
                </tr>
                </thead>
                <tbody>
                <#if result.data.list?? && result.data.list?size gt 0>
                    <#list result.data.list as info>
                        <tr>
                            <td align="center">${info_index+1}</td>
                            <td align="center"><a
                                        href="/orderManager/detail?strOrderNo=${info.orderNo}">${info.orderNo}</a></td>
                            <td align="center">${info.payment}元</td>
                            <td align="center">${info.createTime}</td>
                            <td align="center">${info.statusDesc}</td>
                        </tr>
                    </#list>
                <#else >
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="col-md-1 column">
            </div>
            <div class="col-md-10 column" style="text-align: center">
                <ul class="pagination pagination-lg">
                    <#if !result.data.hasPreviousPage>
                        <li class="disabled"><a href="#">上一页</a></li>
                    <#else>
                        <li><a href="/product/list?pageNum=${result.data.prePage}">上一页</a></li>
                    </#if>
                    <#list result.data.navigatepageNums as index>
                        <#if result.data.pageNum == index>
                            <li class="disabled"><a href="#">#{index}</a></li>
                        <#else>
                            <li><a href="/product/list?pageNum=${index}">${index}</a></li>
                        </#if>
                    </#list>
                    <#if !(result.data.hasNextPage)>
                        <li class="disabled"><a href="#">下一页</a></li>
                    <#else>
                        <li><a href="/product/list?pageNum=${result.data.nextPage}">下一页</a></li>
                    </#if>
                </ul>
            </div>
            <div class="col-md-1 column">
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    function doUpdate() {
        $.ajax({
            method: 'post',
            url: '../productManager/saveOrUpdateProduct',
            dataType: 'json',
            data: {
                'id': $('#productId').val(),
                'name': $('#name').val(),
                'subtitle': $('#subtitle').val(),
                'mainImage': $('#finalImg')[0].src,
                'detail': $('#detail').val(),
                'price': $('#price').val(),
                'stock': $('#stock').val()
            },
            success: function (result) {
                if (result.status == 0) {
                    alert("修改成功");
                }
            }
        })
    }

    function logOut() {
        $.ajax({
            method: 'post',
            url: '../user/logout',
            dataType: 'json',
            data: {},
            success: function (result) {
                window.location.href = "../../index.html";
            }
        })
    }

    $('#search').click(function () {
        window.location.href = "product/search?productName=" + $('#productName').val();
    })


    function doDelete() {
        if (confirm("是否删除")) {
            $.ajax({
                method: 'post',
                url: '../productManager/deleteProduct',
                dataType: 'json',
                data: {
                    'productId': $('#productId').val()
                },
                success: function (result) {
                    if (result.status == 0) {
                        window.location.href = "/productManager/getProductList";
                    }
                }
            })
        }
    }

    //判断是否为数字
    function checkRate(input) {
        var re = /^[1-9]+.?[1-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
        var nubmer = document.getElementById(input).value;

        if (!re.test(nubmer)) {
            alert("请输入数字");
            document.getElementById(input).value = "";
            return false;
        }
        return true;
    }

    //判断视为为空
    function checkNull(input) {

        var ids = input.split(",");
        for (var i = 0; i < ids.length; i++) {
            var id = ids[i];
            var v = document.getElementById(id).value;
            if (v == null || v == "") {
                alert("请检查是否有空");
                return false;
            }
        }
    }
</script>
</html>
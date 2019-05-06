<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>阿C外卖</title>
    <script src="../script/JQuery.js" type="text/javascript"></script>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div style="line-height: 20px"></div>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="/productManager/getProductList">阿 C 外 卖</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="../newProduct.html">新增商品</a>
                        </li>
                        <li>
                            <a href="">所有订单</a>
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
            <table class="table table-hover">
                <thead>
                <tr>
                    <th style="text-align: center">编号</th>
                    <th style="text-align: center">名称</th>
                    <th style="text-align: center">价钱</th>
                    <th style="text-align: center">库存</th>
                    <th style="text-align: center">状态</th>
                    <th>    </th>
                </tr>
                </thead>
                <tbody>
                <#if result.data.getList()?? && result.data.getList()?size gt 0>
                    <#list result.data.list as info>
                        <tr>
                            <td align="center">${info_index+1}</td>
                            <td align="center"><a href="/productManager/getProductDetail?productId=${info.id}">${info.name}</a></td>
                            <td align="center">${info.price}元</td>
                            <td align="center">${info.stock}份</td>
                            <#if info.status == 1>
                                <td align="center">在售</td>
                            <#else>
                                <td align="center">已下架</td>
                            </#if>
                            <#if info.status == 1>
                                <td align="center"><button type="button" class="btn btn-default btn-danger" onclick="downOrUp(${info.id})">下架</button></td>
                            <#else>
                                <#if info.stock gt 0>
                                    <td align="center"><button type="button" class="btn btn-default btn-success" onclick="downOrUp(${info.id})">上架</button></td>
                                <#else>
                                    <td align="center"><button type="button" disabled class="btn btn-default btn-success">上架</button></td>
                                </#if>
                            </#if>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
</div>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="col-md-1 column">
            </div>
            <div class="col-md-10 column" style="text-align: center">
                <ul class="pagination pagination-lg">
                    <#if !result.data.hasPreviousPage>
                        <li class="disabled"><a href="#">上一页</a></li>
                    <#else>
                        <li><a href="/productManager/getProductList?pageNum=${result.data.prePage}">上一页</a></li>
                    </#if>
                    <#list result.data.navigatepageNums as index>
                        <#if result.data.pageNum == index>
                            <li class="disabled"><a href="#">#{index}</a></li>
                        <#else>
                            <li><a href="/productManager/getProductList?pageNum=${index}">${index}</a></li>
                        </#if>
                    </#list>
                    <#if !(result.data.hasNextPage)>
                        <li class="disabled"><a href="#">下一页</a></li>
                    <#else>
                        <li><a href="/productManager/getProductList?pageNum=${result.data.nextPage}">下一页</a></li>
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
    
    function downOrUp(productId) {
        $.ajax({
            method:'post',
            url:'../productManager/setStatus',
            dataType:'json',
            data:{
                'productId' : productId
            },
            success:function (result) {
                if(result.status == 0){
                    window.location.href = '../productManager/getProductList';
                }
            }
        })
    }
    function logOut(){
        $.ajax({
            method:'post',
            url:'../user/logout',
            dataType:'json',
            data:{

            },
            success:function (result) {
                window.location.href="../../index.html";
            }
        })
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
        var v = document.getElementById(input).value;
        if (v == null || v == ""){
            alert("数量不能为空");
            return false;
        }
    }
</script>
</html>
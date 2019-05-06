<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>阿C外卖</title>
    <script src="../script/JQuery.js" type="text/javascript"></script>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="/product/list">阿 C 外 卖</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="/cart/list">购物车</a>
                        </li>
                        <li>
                            <a href="/shipping/list">收货地址</a>
                        </li>
                        <li>
                            <a href="/order/list">我的订单</a>
                        </li>
                    </ul>
                    <form class="navbar-form navbar-left" role="search" action="/product/search" method="get">
                        <div class="form-group">
                            <input type="text" class="form-control" name="productName" />
                        </div> <button id="submit" type="submit" class="btn btn-default">搜索</button>
                    </form>
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
<div class="container">
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column">
            <#--${result["cartVo"].data}-->
            <div style="height: 10px;"></div>
            <h3>所选商品:</h3>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>商品名称</th>
                    <th>商品单价</th>
                    <th>商品数量</th>
                    <th>总价</th>
                    <th>    </th>
                </tr>
                </thead>
                <tbody>
                <#if result["cartVo"].data.cartProductVoList?? && result["cartVo"].data.cartProductVoList?size gt 0>
                    <#list result["cartVo"].data.cartProductVoList as info>
                        <tr>
                            <td>${info_index+1}</td>
                            <td><a href="/product/detail?productId=${info.productId}">${info.productName}</a></td>
                            <td><span id="${info.id}price">${info.productPrice}</span>元</td>
                            <td><input style="width: 50px" type="text" id="${info.id}count" class="form-control" value="${info.quantity}"份</td>
                            <td><sapan id="${info.id}">${info.productTotalPrice}</sapan>元</td>
                            <td align="center"><button type="button" onclick="doDelete(${info.productId});" class="btn btn-default btn-danger">删除</button></td>
                            <script type="text/javascript">
                                $('#${info.id}count').change(function () {
                                    if (checkRate('${info.id}count')) {
                                        $.ajax({
                                            method: 'get',
                                            url: '/cart/update',
                                            dataType: 'json',
                                            data: {
                                                'productId':${info.productId},
                                                'count': $('#${info.id}count').val()
                                            },
                                            success: function (result) {
                                                if (result.status == 1) {
                                                    alert(result.msg);
                                                }else{
                                                    //alert(typeof parseInt($('#${info.id}count').val()));
                                                    //alert(typeof parseInt($('#${info.id}price').html()));
                                                    $('#${info.id}').html(parseInt($('#${info.id}count').val())*parseInt($('#${info.id}price').html()));
                                                }
                                            }
                                        })
                                    }
                                })
                            </script>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td>    </td>
                        <td>    </td>
                        <td>    </td>
                        <td>    </td>
                        <td>    </td>
                    </tr>
                </#if>
                </tbody>
            </table>
            <div style="height: 10px;"></div>
            <h3>收货地址:</h3>
            <form id="createOrder" action="/order/create" method="post">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>收货人姓名</th>
                    <th>收货人电话</th>
                    <th>收货人地址</th>
                    <th>    </th>
                </tr>
                </thead>
                <tbody>
                        <#if result["shippingVoList"].data?? && result["shippingVoList"].data?size gt 0>
                            <#list result["shippingVoList"].data as info>
                                <#if info_index == 0>
                                    <tr>
                                        <td>${info_index+1}</td>
                                        <td>${info.receiverName}</td>
                                        <td>${info.receiverPhone}</td>
                                        <td>${info.receiverAddress}</td>
                                        <td><input type="radio" name="shippingId" value="${info.id}" checked/></td>
                                    </tr>
                                <#else >
                                    <tr>
                                        <td>${info_index+1}</td>
                                        <td>${info.receiverName}</td>
                                        <td>${info.receiverPhone}</td>
                                        <td>${info.receiverAddress}</td>
                                        <td><input type="radio" name="shippingId" value="${info.id}"/></td>
                                    </tr>
                                </#if>
                            </#list>
                        <#else>
                            <tr>
                                <td>    </td>
                                <td>    </td>
                                <td>    </td>
                                <td>    </td>
                            </tr>
                        </#if>
                </tbody>
            </table>
            </form>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column">
            <div class="row clearfix">
                <div class="col-md-6 column">
                </div>
                <#if result["cartVo"].data.cartProductVoList?? && result["cartVo"].data.cartProductVoList?size gt 0 && result["shippingVoList"].data?? && result["shippingVoList"].data?size gt 0>
                    <div class="col-md-6 column" style="text-align: right">
                        <button style="margin-left: 50px" type="button" onclick="createOrder()" class="btn btn-success btn-default">提交订单</button>
                    </div>
                <#else >
                    <div class="col-md-6 column" style="text-align: right">
                        <button style="margin-left: 50px" type="button" class="btn btn-success btn-default disabled">提交订单</button>
                    </div>
                </#if>
            </div>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

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

    function doDelete(id) {
        if (confirm("是否删除?")) {
            $.ajax({
                method:'post',
                url:'../cart/delete',
                dataType:'json',
                data:{
                    'productId':id
                },
                success:function (result) {
                    //alert(result.status);
                    if (result.status == 0){
                        window.location.href="../cart/list";
                    }
                }
            })
        }
    }

    function createOrder(){
        alert(123);
        $('#createOrder').submit();
    }


    $('#search').click(function () {
        window.location.href = "product/search?productName="+$('#productName').val();
        alert(111);
    })
    //判断是否为数字
    function checkRate(input) {
        var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
        var nubmer = document.getElementById(input).value;

        if (!re.test(nubmer)) {
            alert("请输入数字");
            document.getElementById(input).value = "";
            return false;
        }
        return true;
    }
    //判空
    function checkNull(input) {
        var v = document.getElementById(input).value;
        if (v == null || v == ""){
            alert("数量不能为空");
            return false;
        }
    }
</script>
</html>
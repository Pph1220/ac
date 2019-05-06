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
<form action="/order/pay" method="post">
    <input type="hidden" name="strOrderNo" value="${result.data.orderNo}"/>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-1 column">
            </div>
            <div class="col-md-10 column">
                <h3>订单编号:${result.data.orderNo}</h3>
                <table class="table">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>商品名称</th>
                        <th>商品图片</th>
                        <th>商品单价</th>
                        <th>数量</th>
                        <th>总价</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if result.data.orderItemVos?? && result.data.orderItemVos?size gt 0>
                        <#list result.data.orderItemVos as info>
                            <tr>
                                <td height="100px" style="line-height: 60px">${info_index+1}</td>
                                <td height="100px" style="line-height: 60px">${info.productName}</td>
                                <td><img class="img-rounded" src="${info.productImage}" height="60px" width="60px"></td>
                                <td height="100px" style="line-height: 60px">${info.currentUnitPrice}元</td>
                                <td height="100px" style="line-height: 60px">${info.quantity}份</td>
                                <td height="100px" style="line-height: 60px">${info.totalPrice}元</td>
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
            <div class="col-md-1 column">
            </div>
            <div class="col-md-10 column">
                <p style="line-height: 20px"><span style="font-size: large">收货人姓名:</span><span>${result.data.shippingVo.receiverName}</span></p>
                <p><span style="font-size: large">收货人电话:</span><span>${result.data.shippingVo.receiverPhone}</span></p>
                <p><span style="font-size: large">收货人地址:</span><span>${result.data.shippingVo.receiverAddress}</span></p>
            </div>
            <div class="col-md-1 column">
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-1 column">
            </div>
            <div class="col-md-10 column" style="text-align: right">
                <#if result.data.status gt 10>
                    <span>订单总价:</span><span>${result.data.payment}元</span>
                    <button style="margin-left: 50px" type="submit" class="btn btn-default btn-success" disabled>付款</button>
                <#else >
                    <span>订单总价:</span><span>${result.data.payment}元</span>
                    <button style="margin-left: 50px" type="submit" class="btn btn-default btn-success">付款</button>
                </#if>
            </div>
            <div class="col-md-1 column">
            </div>
        </div>
    </div>
</form>
<#--${result.data}-->
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
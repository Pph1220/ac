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
        <div class="col-md-10 column" style="text-align: center">
            <h3>请使用支付宝扫描下方二维码为订单号为:${result.data.orderNo}的订单付款</h3>
            <img src="${result.data.Img}">
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

    window.onload = function () {
        setInterval(queryPayStatus,2000);

    }

    function queryPayStatus() {
        $.ajax({
            method:'post',
            url:'../order/queryPayStatus',
            dataType:'json',
            data:{
                'orderNo':${result.data.orderNo}
            },
            success:function (result) {
                if (result.data){
                    window.location.href = "../order/detail?strOrderNo=${result.data.orderNo}";
                }
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
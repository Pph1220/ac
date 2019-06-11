<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>阿C外卖</title>
    <script src="../script/JQuery.js" type="text/javascript"></script>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<input id="productId" type="hidden" value="${result.data.id}"/>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse"
                            data-target="#bs-example-navbar-collapse-1"><span
                                class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span
                                class="icon-bar"></span><span class="icon-bar"></span></button>
                    <a class="navbar-brand" href="/product/list">阿 C 外 卖</a>
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
                            <input type="text" class="form-control" name="productName"/>
                        </div>
                        <button id="submit" type="submit" class="btn btn-default">搜索</button>
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
            <#--<img src="${result.data.mainImage}"-->
        </div>
    </div>
</div>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-1 column"></div>
        <div class="col-md-10 column">
            <div style="height: 10px;"></div>
            <h3>${result.data.subtitle}</h3>
            <div class="row clearfix">
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-md-6 column">
                            <img alt="140x140" src="${result.data.mainImage}" style="height: 300px;width: 300px;"/>
                        </div>
                        <div class="col-md-6 column">
                            <ul>
                                <li style="line-height: 100px">
                                    商品名称:${result.data.name}
                                </li>
                                <li style="line-height: 100px">
                                    商品价格:${result.data.price}元
                                </li>
                                <li style="line-height: 100px">
                                    商品库存:${result.data.stock}
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-1 column">
                </div>
            </div>
        </div>
        <div class="col-md-3 column">
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column">
            <div class="row clearfix">
                <div class="col-md-6 column">
                </div>
                <div class="col-md-6 column" style="text-align: right">
                    <input style="width: 50px;display: inline-block" type="text" class="form-control" id="count"/>
                    <button style="display: inline-block" onclick="add();" type="button" class="btn btn-primary">加入购物车
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column">
            <div style="height: 30px;"></div>
            <div><h3>详情描述:</h3></div>
            <div>&nbsp;&nbsp;&nbsp;&nbsp;${result.data.detail}</div>
        </div>
        <div class="col-md-1 column">
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

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

    function add() {
        if (checkRate('count')) {
            $.ajax({
                method: 'get',
                url: '../cart/add',
                dataType: 'json',
                data: {
                    'productId': $('#productId').val(),
                    'count': $('#count').val()
                },
                success: function (result) {
                    if (result.status == 0) {
                        alert("加入购物车成功");
                        $('#count').val("");
                    } else {
                        alert("加入购物车失败");
                    }
                }
            })
        }
    }

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

    //判断视为为空
    function checkNull(input) {
        var v = document.getElementById(input).value;
        if (v == null || v == "") {
            alert("数量不能为空");
            return false;
        }
    }
</script>
</html>
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
        </div>
    </div>
</div>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-1 column">
        </div>
        <div class="col-md-10 column" style="text-align: center">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th style="text-align: center">编号</th>
                    <th style="text-align: center">收货人姓名</th>
                    <th style="text-align: center">收货人电话</th>
                    <th style="text-align: center">收货人地址</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list result.data as info>
                    <tr>
                        <td>${info_index+1}</td>
                        <td>${info.receiverName}</td>
                        <td>${info.receiverPhone}</td>
                        <td>${info.receiverAddress}</td>
                        <td>
                            <button type="button" onclick="doDelete(${info.id})" class="btn btn-default btn-danger">删除
                            </button>
                        </td>
                    </tr>
                </#list>
                <tr>
                    <td></td>
                    <td align="center"><input id="name" style="width: 70px" class="form-control" type="text" value=""/>
                    </td>
                    <td align="center"><input id="phone" style="width: 115px" class="form-control" type="text"
                                              value=""/></td>
                    <td align="center"><input id="address" style="width: 200px" class="form-control" type="text"
                                              value=""/></td>
                    <td>
                        <button type="button" onclick="doAdd()" class="btn btn-default btn-success">新增</button>
                    </td>
                </tr>
                </tbody>
            </table>
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

    function doDelete(id) {
        if (confirm("是否删除")) {
            deleteShipping(id);
        }
    }

    function deleteShipping(id) {
        if (confirm("是否删除?")) {
            $.ajax({
                method: 'post',
                url: '../shipping/delete',
                dataType: 'json',
                data: {
                    'shippingId': id
                },
                success: function (result) {
                    //alert(result.status);
                    if (result.status == 0) {
                        window.location.href = "../shipping/list";
                    }
                }
            })
        }
    }

    function doAdd() {
        checkNull("name,phone,address");
        checkPhone("phone");
        add();
    }

    function add() {
        $.ajax({
            method: 'post',
            url: '../shipping/add',
            dataType: 'json',
            data: {
                'receiverName': $('#name').val(),
                'receiverPhone': $('#phone').val(),
                'receiverAddress': $('#address').val()
            },
            success: function (result) {
                //alert(result.status);
                if (result.status == 0) {
                    window.location.href = "../shipping/list";
                }
            }
        })
    }

    function checkPhone(input) {
        var v = document.getElementById(input).value;
        var TEL_REGEXP = /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
        if (!TEL_REGEXP.test(v)) {
            alert("请输入正确的手机号格式");
            return false;
        }
    }


    $('#search').click(function () {
        window.location.href = "product/search?productName=" + $('#productName').val();
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
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
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="/product/getProductList">阿 C 外 卖</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="#">购物车</a>
                        </li>
                        <li>
                            <a href="#">收货地址</a>
                        </li>
                        <li>
                            <a href="#">我的订单</a>
                        </li>
                    </ul>
                    <form class="navbar-form navbar-left" role="search" action="/product/search" method="get">
                        <div class="form-group">
                            <input type="text" class="form-control" name="productName" />
                        </div> <button id="submit" type="submit" class="btn btn-default">搜索</button>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="#">我的阿C</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <#list result.data.list as a>
                <div class="col-md-4" style="width: 20%">
                    <div class="thumbnail">
                        <img height="300" width="300" src="${a.mainImage}" />
                        <div class="caption">
                            <h3>
                                <a href="/product/detail?productId=${a.id}">${a.name}</a>
                            </h3>
                            <p>
                                ${a.price}元
                            </p>
                            <p>
                                <input type="text" class="form-control" id="${a.id}count"/>
                            </p>
                            <p>
                                <a class="btn btn-primary" id="${a.id}btn">加入购物车</a>
                            </p>
                        </div>
                    </div>
                    <script type="text/javascript">
                        $('#${a.id}btn').click(function () {
                            if (checkRate('${a.id}count')){
                                $.ajax({
                                    method:'get',
                                    url:'../cart/add',
                                    dataType:'json',
                                    data:{
                                        'productId':${a.id},
                                        'count':$('#${a.id}count').val()
                                    },
                                    success:function (result) {
                                        if (result.status == 0){
                                            alert("加入购物车成功");
                                            $('#${a.id}count').val("");
                                        }else{
                                            alert("加入购物车失败");
                                        }
                                    }
                                })
                            }
                        })
                    </script>
                </div>
            </#list>
         </div>
    </div>
    <div class="row clearfix">
        <#--${result.data}-->
        <div class="col-md-12 column">
            <ul class="pagination pagination-lg">
                <li>
                    <a href="#">上一页</a>
                </li>
                <li>
                    <a href="#">1</a>
                </li>
                <li>
                    <a href="#">2</a>
                </li>
                <li>
                    <a href="#">3</a>
                </li>
                <li>
                    <a href="#">4</a>
                </li>
                <li>
                    <a href="#">5</a>
                </li>
                <li>
                    <a href="#">下一页</a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
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
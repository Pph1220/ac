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
    <div class="row clearfix">
        <div class="col-md-12 column">
            <#if result.data.getList()??>
                <#list result.data.getList() as a>
                    <div class="col-md-4" style="width: 20%">
                        <div class="thumbnail">
                            <img height="195px" width="195px" src="${a.mainImage}" />
                            <div class="caption">
                                <h3>
                                    <a href="/product/detail?productId=${a.id}">${a.name}</a>
                                </h3>
                                <p>
                                    ${a.price}元
                                </p>
                                <p>
                                    <input style="display: inline-block;width: 50px" type="text" class="form-control" id="${a.id}count"/>
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
            </#if>
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


    $('#search').click(function () {
        window.location.href = "product/search?productName="+$('#productName').val();
    })
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
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
        <div class="col-md-1 column"></div>
        <div class="col-md-10 column">
            <div style="height: 10px;"></div>
            <div class="row clearfix">
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-md-6 column">
                            <button id="replaceImg" class="l-btn">更换图片</button>
                            <div style="width: 300px;height: 300px;border: solid 1px #555;padding: 5px;margin-top: 10px">
                                <img id="finalImg" src="${result.data.mainImage}" width="100%">
                            </div>


                            <!--图片裁剪框 start-->
                            <div style="display: none" class="tailoring-container">
                                <div class="black-cloth" onclick="closeTailor(this)"></div>
                                <div class="tailoring-content">
                                    <div class="tailoring-content-one">
                                        <label title="上传图片" for="chooseImg" class="l-btn choose-btn">
                                            <input type="file" accept="image/jpg,image/jpeg,image/png" name="file"
                                                   id="chooseImg" class="hidden" onchange="selectImg(this)">
                                            选择图片
                                        </label>
                                        <div class="close-tailoring" onclick="closeTailor(this)">×</div>
                                    </div>
                                    <div class="tailoring-content-two">
                                        <div class="tailoring-box-parcel">
                                            <img id="tailoringImg">
                                        </div>
                                        <div class="preview-box-parcel">
                                            <p>图片预览：</p>
                                            <div class="square previewImg"></div>
                                            <div class="circular previewImg"></div>
                                        </div>
                                    </div>
                                    <div class="tailoring-content-three">
                                        <button class="l-btn cropper-reset-btn">复位</button>
                                        <button class="l-btn cropper-rotate-btn">旋转</button>
                                        <button class="l-btn cropper-scaleX-btn">换向</button>
                                        <button class="l-btn sureCut" id="sureCut">确定</button>
                                    </div>
                                </div>
                            </div>
                            <script type="text/javascript">
                                //弹出框水平垂直居中
                                (window.onresize = function () {
                                    var win_height = $(window).height();
                                    var win_width = $(window).width();
                                    if (win_width <= 768) {
                                        $(".tailoring-content").css({
                                            "top": (win_height - $(".tailoring-content").outerHeight()) / 2,
                                            "left": 0
                                        });
                                    } else {
                                        $(".tailoring-content").css({
                                            "top": (win_height - $(".tailoring-content").outerHeight()) / 2,
                                            "left": (win_width - $(".tailoring-content").outerWidth()) / 2
                                        });
                                    }
                                })();

                                //弹出图片裁剪框
                                $("#replaceImg").on("click", function () {
                                    $(".tailoring-container").toggle();
                                });

                                //图像上传
                                function selectImg(file) {
                                    if (!file.files || !file.files[0]) {
                                        return;
                                    }
                                    var reader = new FileReader();
                                    reader.onload = function (evt) {
                                        var replaceSrc = evt.target.result;
                                        //更换cropper的图片
                                        $('#tailoringImg').cropper('replace', replaceSrc, false);//默认false，适应高度，不失真
                                    }
                                    reader.readAsDataURL(file.files[0]);
                                }

                                //cropper图片裁剪
                                $('#tailoringImg').cropper({
                                    aspectRatio: 1 / 1,//默认比例
                                    preview: '.previewImg',//预览视图
                                    guides: false,  //裁剪框的虚线(九宫格)
                                    autoCropArea: 0.5,  //0-1之间的数值，定义自动剪裁区域的大小，默认0.8
                                    movable: false, //是否允许移动图片
                                    dragCrop: true,  //是否允许移除当前的剪裁框，并通过拖动来新建一个剪裁框区域
                                    movable: true,  //是否允许移动剪裁框
                                    resizable: true,  //是否允许改变裁剪框的大小
                                    zoomable: false,  //是否允许缩放图片大小
                                    mouseWheelZoom: false,  //是否允许通过鼠标滚轮来缩放图片
                                    touchDragZoom: true,  //是否允许通过触摸移动来缩放图片
                                    rotatable: true,  //是否允许旋转图片
                                    crop: function (e) {
                                        // 输出结果数据裁剪图像。
                                    }
                                });
                                //旋转
                                $(".cropper-rotate-btn").on("click", function () {
                                    $('#tailoringImg').cropper("rotate", 45);
                                });
                                //复位
                                $(".cropper-reset-btn").on("click", function () {
                                    $('#tailoringImg').cropper("reset");
                                });
                                //换向
                                var flagX = true;
                                $(".cropper-scaleX-btn").on("click", function () {
                                    if (flagX) {
                                        $('#tailoringImg').cropper("scaleX", -1);
                                        flagX = false;
                                    } else {
                                        $('#tailoringImg').cropper("scaleX", 1);
                                        flagX = true;
                                    }
                                    flagX != flagX;
                                });

                                //裁剪后的处理
                                $("#sureCut").on("click", function () {
                                    if ($("#tailoringImg").attr("src") == null) {
                                        return false;
                                    } else {
                                        var cas = $('#tailoringImg').cropper('getCroppedCanvas');//获取被裁剪后的canvas
                                        var base64url = cas.toDataURL('image/png'); //转换为base64地址形式
                                        $("#finalImg").prop("src", base64url);//显示为图片的形式

                                        //关闭裁剪框
                                        closeTailor();
                                    }
                                });

                                //关闭裁剪框
                                function closeTailor() {
                                    $(".tailoring-container").toggle();
                                }

                            </script>
                        </div>
                        <div class="col-md-6 column">
                            <div style="line-height: 50px"></div>
                            <input type="hidden" id="productId" value="${result.data.id}"/>
                            <label for="subtitle">标题</label><input style="width: 200px" name="subtitle" type="text"
                                                                   class="form-control" id="subtitle"
                                                                   onchange="checkNull('subtitle')"
                                                                   value="${result.data.subtitle}"/>
                            <label for="name">名称</label><input style="width: 200px" name="name" type="text"
                                                               class="form-control" id="name"
                                                               onchange="checkNull('name')"
                                                               value="${result.data.name}"/>
                            <label for="price">价钱</label><input style="width: 200px" name="price" type="text"
                                                                class="form-control" id="price"
                                                                onchange="checkNull('price');checkRate('price')"
                                                                value="${result.data.price}"/>
                            <label for="stock">库存</label><input style="width: 200px" name="stock" type="text"
                                                                class="form-control" id="stock"
                                                                onchange="checkNull('stock');checkRate('stock')"
                                                                value="${result.data.stock}"/>
                        </div>
                    </div>
                </div>
            </div>
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
            <div class="row clearfix">
                <div style="height: 30px;"></div>
                <div><h3>详情描述:</h3></div>
                <textarea style="height: 100px; width: 100%" id="detail">${result.data.detail}</textarea>
            </div>
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
            <div class="col-md-6 column">
            </div>
            <div class="col-md-6 column" style="text-align: right">
                <button style="display: inline-block" onclick="doDelete();" type="button"
                        class="btn btn-default btn-danger">删除
                </button>
                <button style="display: inline-block" onclick="doUpdate();" type="button"
                        class="btn btn-default btn-success">修改
                </button>
            </div>
        </div>
        <div class="col-md-1 column">
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
$(function(){
    $("#submit").on('click',function () {
        var username = $("#username");
        var password = $("#password");
        var email = $("#email");
        var retype = $("#retype");
        var reg= /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)/;

        var check = true;
        //检验用户名
        if (username.val() == null ||username.val()=="") {
            username.parent().removeClass("has-feedback").addClass("has-error");
            $("#usernameError").text("账号不能为空");
            check = false;
        }else if (username.val().length>12 || isNaN(username.val())) {
            username.parent().removeClass("has-feedback").addClass("has-error");
            $("#usernameError").text("账号长度不能大于12位,且只能为数字");
            check = false;
        }else{
            username.parent().removeClass("has-error").addClass("has-feedback");
            $("#usernameError").text("");
        }
        //检验邮箱
        if (email.val() == null ||email.val()=="") {
            email.parent().removeClass("has-feedback").addClass("has-error");
            $("#emailError").text("邮箱不能为空");
            check = false;
        }else  if (reg.test(email.val())==false) {
            email.parent().removeClass("has-feedback").addClass("has-error");
            $("#emailError").text("请输入正确格式的邮箱地址");
            check = false;
        }else{
            email.parent().removeClass("has-error").addClass("has-feedback");
            $("#emailError").text("");
        }

        //检验密码
        if (password.val() == null||password.val()=="") {
            password.parent().removeClass("has-feedback").addClass("has-error");
            $("#passwordError").text("密码不能为空");
            check = false;
        }else if (password.val().length>18||password.val().length<6) {
            password.parent().removeClass("has-feedback").addClass("has-error");
            $("#passwordError").text("密码长度必须在6-18之间");
            check = false;
        }else if ( retype.val()!=""&&retype.val()!=password.val()) {
            password.parent().removeClass("has-feedback").addClass("has-error");
            $("#passwordError").text("两次密码输入不一致");
            check = false;
        }else{
            password.parent().removeClass("has-error").addClass("has-feedback");
            $("#passwordError").text("");
        }


        if (retype.val() == null||retype.val()=="") {
            retype.parent().removeClass("has-feedback").addClass("has-error");
            $("#retypeError").text("确认密码不能为空");
            check = false;
        }else if (retype.val().length>18||retype.val().length<6) {
            retype.parent().removeClass("has-feedback").addClass("has-error");
            $("#retypeError").text("确认密码不能为空");
            check = false;
        }else if ( password.val()!=""&&retype.val()!=password.val()) {
            retype.parent().removeClass("has-feedback").addClass("has-error");
            $("#retypeError").text("两次密码输入不一致");
            check = false;
        }else {
            retype.parent().removeClass("has-error").addClass("has-feedback");
            $("#retypeError").text("");
        }


        //检验出错
        if (!check) {
            return false;
        }else{
            checkAdd = true;
        }
    });

    $("#username").on("blur",function () {
        $.ajax({
            url: '/checkUsername?username='+$("#username").val(),
            success:function(data) {
                if (data.success) {
                    $("#username").parent().removeClass("has-feedback").addClass("has-error");
                    $("#usernameError").text("用户名不存在");
                    $("#submit").attr("disabled","disabled");
                }else {
                    $("#username").parent().removeClass("has-error").addClass("has-feedback");
                    $("#usernameError").text("");
                    $("#submit").removeAttr("disabled");
                }
            },
        });
    });
});
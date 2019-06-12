$(function() {
    if ($("#error").val()!=""&& typeof($("#error").val())!="undefined" ) {
        alert($("#error").val());
        $("#error").val("");
    }
    if ($("#success").val()!=""&& typeof($("#success").val())!="undefined") {
        alert($("#success").val());
        $("#success").val("");
    }

    //搜索功能的返回值是否有错误信息，有则提示
    if ($("#searchError").val()!=""&& typeof($("#searchError").val())!="undefined") {
        alert($("#searchError").val());
        $("#searchError").val("");
    }

$(".toUserspace").on("click",function () {
     var username=$("#principal").text();
     window.location="/u/"+username;
 });

 $(".toProfile").on("click",function () {
     var username=$("#principal").text();
     window.location="/u/"+username+"/profile";
 });

 var avatarApi;
 //获取编辑用户头像的界面
 $(".content").on("click",".edit-avatar",function() {
     avatarApi="/u/"+$(this).attr("username")+"/avatar";
     $.ajax({
         url:avatarApi ,
         success:function(data) {
             $("#avatarFormContainer").html(data);
         },
         error: function() {
             alert("意外错误!");
         }
     });
 });

 /**
  * 将以base64的图片url数据转换为blob
  * @param urlData
  * 用URL方式表示的base64图片数据
  */
 function convertBase64UrlToBlob(urlData) {
     var bytes= window.atob(urlData.split(",")[1]); //去掉url的头，并转换为byte
     //处理异常，将ASCII码小于0的转换为大于0
     var ab =new ArrayBuffer(bytes.length);
     var ia= new Uint8Array(ab);
     for(var i=0;i<bytes.length;i++){
         ia[i]=bytes.charCodeAt(i);
     }
     return new Blob([ab],{type: 'image/png'});
 }


    //提交用户头像的图片数据
    $("#submitEditAvatar").on("click",function(){
        var form =$("#avatarformid")[0];
        var formData =new FormData(form);
        var base64Codes =$(".cropImg>img").attr("src");
        formData.append("file",convertBase64UrlToBlob(base64Codes));

        $.ajax({
            url:'/u/'+username+'/upload',
            type:'POST',
            cache:false,
            data:formData,
            processData:false,
            contentType:false,
            success:function(data) {
                if(data.success) {
                    //成功后，置换头像图片
                    $(".blog-avatar").attr("src",data.body);
                }else{
                    alert("error!"+data.message);
                }
            },
            error: function() {
                alert("出现错误");
            }

        });
    });
});



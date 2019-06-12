$( function(){
    //数据展示的样式
    var columns =[
        {"data": "id"},
        {"data": "username"},
        {"data": "email"},
        {"data": "roles",
          "render":"[, ].name"},
        {"data": function(row, type, val, meta){
                return "<a data-toggle=\"modal\" userId= \""+row.id+"\""+
                    "\n data-target=\"#dialog\" class=\"getEdit\" role=\"button\">\n" +
                    "<span class=\"fa fa-pencil\"></span>\n </a>" +
                    "<a data-toggle=\"modal\" userId=\""+row.id+"\""+
                    "\n class=\"delete\" role=\"button\" style=\"color:red\">\n" +
                    " <span class=\"fa fa-times\"></span>\n  </a>"
            }
        }
    ];
    var pageUrl ="/admin/page";

    var dataTable;  //接收返回的参数

    //初始化DataTable分页的方法
    var initDataTable= function(ele,url,columns) {
            dataTable =ele.DataTable({
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            'paging'      : true,
            'lengthChange': false,
            'searching'   : false,
            'ordering'    : false,
            'info'        : true,
            'autoWidth'   : true,
            'deferRender' : true,
            'serverSide' : true,
            'ajax'        : {
                "url":   url,
            },
            "columns" : columns
        });
        return dataTable;
    }

    //初始化
     dataTable=initDataTable($("#example1"),pageUrl,columns);

    //搜索
    function search() {
        var username = $("#keyword").val();
        var param = {
            "username": username,
        };
        dataTable.settings()[0].ajax.data = param;
        dataTable.ajax.reload();
    }


    //点击按钮进行搜索
   $("#searchUsername").on("click",function () {
       search();
       $("#keyword").text("");
   })

    //回车键进行搜索
    $(".input-group").keyup(function(event){
        if (event.keyCode==13) {
            search();
            $("#keyword").text("");
        }
    });

    //获取编辑用户的对话框
    $("body").on("click",".getEdit",function() {
        $.ajax({
            url:"/admin/edit/"+$(this).attr("userId"),
            cache:false,
            success:function (data) {
                $("#dialogForm").html(data);
            },
            error:function(){
                alert("获取失败！");
            }
        })
    });

    //提交用户更改
    $("#submitEdit").on("click",function(){
        $.ajax({
            url:"/admin/edit",
            type:"POST",
            data:$("#userForm").serialize(), //序列化，以URL标准表示的文本字符串
            success:function(data) {
                $("#userForm")[0].reset();
                if (data.success){          //成功更新用户列表
                    dataTable.ajax.reload();
                    alert(data.message);
                }else{
                    alert(data.message);
                }
            },
            error:function () {
                alert("出现意外错误");
            }
        });
    });

    //删除用户
    $("body").on("click",".delete",function(){
        if(confirm("您确定要删除吗?")){
            $.ajax({
                url:"/admin/"+$(this).attr("userId"),
                type:"DELETE",
                success:function (data) {
                    if(data.success){
                        dataTable.ajax.reload();
                        alert(data.message);
                    }else{
                        alert(data.message);
                    }
                },
                error:function() {
                    alert("出现意外错误");
                }
            });
        }
    });

    //保存新建用户
    $("#submit").on("click",function(){
        if (!checkAdd){
            return false;
        }
        $.ajax({
            url:"/admin/add",
            type:"POST",
            data: $("#userForm").serialize(),
            success:function(data) {
                if (data.success) {
                    $("#userForm")[0].reset();
                    alert(data.message);
                }else{
                    alert(data.message);
                }
            },
            error:function() {
                alert("出现意外错误");
            }
        });
    });

});


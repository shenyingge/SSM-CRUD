<%--
  Created by IntelliJ IDEA.
  User: shen
  Date: 2020/2/26
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>员工列表</title>
    <%
        pageContext.setAttribute("APP_PATH",request.getContextPath());
    %>
    <!-- web路径：
     不以/开始的相对路径，找资源，以当前资源的路径为基准，经常出问题
     以/开始的相对路径，找资源，以服务器的路径为标准（http://localhost:3306），需要加上项目名
     http://localhost:3306/ssm-crud
     -->
    <!-- 引入Jquery -->
    <script type="text/javascript" src="${APP_PATH}/static/js/jquery-3.3.1.min.js"></script>

    <!-- 引入Bootstrap样式 -->
    <link href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>

    <!-- 员工添加的模态框 -->
    <div class="modal fade" id="empAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">员工添加</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="emp_save_form">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">empName</label>
                            <div class="col-sm-10">
                                <input type="text" name="empName" class="form-control" id="empName_add_input" placeholder="empName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">email</label>
                            <div class="col-sm-10">
                                <input type="text" name="email" class="form-control" id="email_add_input" placeholder="email@gmail.com">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email_add_input" class="col-sm-2 control-label">gender</label>
                            <div class="col-sm-10">
                                <label class="radio-inline">
                                    <input type="radio" name="gender" id="gender1_add_input" value="M"> 男
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="gender" id="gender2_add_input" value="F"> 女
                                </label>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="email_add_input" class="col-sm-2 control-label">deptName</label>
                            <div class="col-sm-4">
                                <!-- 部门提交只需要部门id -->
                                <select class="form-control" id="dept_add_select" name="dId">

                                </select>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="emp_save_btn">保存</button>
                    <button type="button" class="btn btn-default" id="emp_save_close" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>



    <!-- 搭建显示页面 -->
    <div class="container">
        <!-- 标题 -->
        <div class="row">
            <div class="col-md-12">
                <h1>SSM-CRUD</h1>
            </div>
        </div>
        <!-- 按钮 -->
        <div class="row">
            <div class="col-md-4 col-md-offset-8">
                <button class="btn btn-primary" id="emp_add_modal_btn">新增</button>
                <button class="btn btn-danger">删除</button>
            </div>
        </div>
        <!-- 表格 -->
        <div class="row">
            <div class="col-md-12">
                <table id="emps_table" class="table table-hover">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>empName</th>
                            <th>gender</th>
                            <th>email</th>
                            <th>deptName</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
        <!-- 分页信息 -->
        <div class="row">
            <!-- 文字分页信息  -->
            <div id="page_info_area" class="col-md-6">

            </div>
            <!-- 分页条信息 -->
            <div id="page_nav_area" class="col-lg-6">

            </div>
        </div>
    </div>
    <script type="text/javascript">

        var totalRecord;

        //1、页面加载完成之后，直接去发送ajax请求，要到分页数据
        $(function(){
            to_page(1);
        });

        //点击页码跳转
        function to_page(pn) {
            $.ajax({
                url:"${APP_PATH}/emps",
                data:"pn="+pn,
                type:"GET",
                success:function (result) {
                    //console.log(result);
                    //1、解析并显示员工数据
                    build_emps_table(result);
                    //2、解析并显示分页数据
                    build_page_info(result);
                    //3、解析并显示分页条
                    build_page_nav(result);
                }
            });
        }

        //显示表格数据
        function build_emps_table(result) {

            //清空表格
            $("#emps_table tbody").empty();

            var emps = result.extend.pageInfo.list;
            $.each(emps,function (index,item) {
                var empIdTd = $("<td></td>").append(item.empId);
                var empNameTd = $("<td></td>").append(item.empName);
                var genderTd = $("<td></td>").append(item.gender=='M'?"男":"女");
                var empEmailTd = $("<td></td>").append(item.email);
                var deptNameTd = $("<td></td>").append(item.department.deptName);
                /*<button class="btn btn-primary btn-sm">
                     <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                     编辑
                  </button>
                  <button class="btn btn-danger btn-sm">
                     <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                     删除
                  </button>
                * */
                var editBtn = $("<button></button>")
                    .addClass("btn btn-primary btn-sm")
                    .append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("编辑");
                var delBtn = $("<button></button>")
                    .addClass("btn btn-danger btn-sm")
                    .append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除");

                var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);

                $("<tr></tr>").append(empIdTd)
                    .append(empNameTd)
                    .append(genderTd)
                    .append(empEmailTd)
                    .append(deptNameTd)
                    .append(btnTd)
                    .appendTo("#emps_table tbody");
            });
        }

        //显示分页信息
        function build_page_info(result) {
            totalRecord = result.extend.pageInfo.total;
            $("#page_info_area").empty();
            $("#page_info_area").append("当前"+result.extend.pageInfo.pageNum+"页，共"+result.extend.pageInfo.pages+"页，共"+result.extend.pageInfo.total+"条记录");
        }

        //显示分页条，添加点击事件
        function build_page_nav(result) {
            $("#page_nav_area").empty();
            var ul = $("<ul></ul>").addClass("pagination");
            var firstPageLi = $("<li></li>").append($("<a></a>").append("首页")).attr("href","#");
            var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
            if(result.extend.pageInfo.pageNum == 1){
                firstPageLi.addClass("disabled");
                prePageLi.addClass("disabled");
            }else {
                firstPageLi.click(function () {
                    to_page(1);
                });
                prePageLi.click(function () {
                    to_page(result.extend.pageInfo.pageNum-1);
                });
            }
            var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
            var lastPageLi = $("<li></li>").append($("<a></a>").append("末页")).attr("href","#");
            if(result.extend.pageInfo.pageNum == result.extend.pageInfo.pages){
                nextPageLi.addClass("disabled");
                lastPageLi.addClass("disabled");
            }else{
                nextPageLi.click(function () {
                    to_page(result.extend.pageInfo.pageNum+1);
                });
                lastPageLi.click(function () {
                    to_page(result.extend.pageInfo.pages);
                });
            }

            ul.append(firstPageLi).append(prePageLi);
            $.each(result.extend.pageInfo.navigatepageNums,function (index,item) {
                var numLi = $("<li></li>").append($("<a></a>").append(item));
                if(result.extend.pageInfo.pageNum == item){
                    numLi.addClass("active");
                }
                numLi.click(function () {
                    to_page(item);
                });
                ul.append(numLi);
            });
            ul.append(nextPageLi).append(lastPageLi);
            var navEle = $("<nav></nav>").append(ul);
            //$("#page_nav_area").append(navEle);
            navEle.appendTo("#page_nav_area");
        }

        //点击新增按钮弹出模态框
        $("#emp_add_modal_btn").click(function () {
            //发出ajax请求，查出部门信息，显示
            getDepts();
            //弹出模态框
            $("#empAddModal").modal({
                backdrop:false
            });
        });

        //查出部门信息
        function getDepts() {
            $.ajax({
                url:"${APP_PATH}/depts",
                type:"GET",
                success:function (result) {
//                    console.log(result);
                    $("#dept_add_select").empty();
                    var depts = result.extend.depts;
                    $.each(depts,function (index,item) {
                        var depOption = $("<option></option>").append(item.deptName).attr("value",item.deptId);
                        depOption.appendTo("#dept_add_select");
                    });
                }
            });
        }
        $("#emp_save_btn").click(function () {
            //1、模态框中填写的表单数据提交给服务器进行保存
            //2、发送ajax请求保存员工
            $.ajax({
                url:"${APP_PATH}/emp",
                type: "POST",
                data: $("#emp_save_form").serialize(),
                success:function (result) {
                    //alert(result.msg);
                    /*保存成功后
                    * 1、关闭模态框
                    * 2、跳转最后一页
                    * 发送请求，请求最后一页
                    * */
                    $("#empAddModal").modal('hide');
                    to_page(totalRecord);
                }
            });
        });


    </script>
</body>
</html>

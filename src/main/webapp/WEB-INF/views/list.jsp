<%--
  Created by IntelliJ IDEA.
  User: shen
  Date: 2020/2/26
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <script type="text/javascript" src="${APP_PATH}/static/js/jquery-3.2.1.min.js"></script>
    <!-- 引入Bootstrap样式 -->
    <link href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
    <!-- 搭建显示页面 -->
    <div class="container">
        <!-- 标题 -->
        <div class="row">
            <div class="col-md-12">
                <h1>SSM-CRUD</h1>
            </div>
        </div>
        <!-- 按钮 -->
        <div class="row"></div>
        <!-- 表格 -->
        <div class="row"></div>
        <!-- 分页信息 -->
        <div class="row"></div>
    </div>
</body>
</html>

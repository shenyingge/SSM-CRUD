# SSM整合记录

## 1. 环境搭建

### 1.1 pox.xml中引入

~~~xml
 * spring

 * springmvc

 * mybatis

 * 数据库连接池，驱动包

 * 其他（jstl，servlet，junit）

   ```xml
     <dependencies>
       <!-- SpringMVC, Spring -->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-webmvc</artifactId>
         <version>4.3.7.RELEASE</version>
       </dependency>
       <!-- Spring-JDBC -->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-jdbc</artifactId>
         <version>4.3.7.RELEASE</version>
       </dependency>
       <!-- spring测试模块 -->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-test</artifactId>
         <version>4.3.7.RELEASE</version>
       </dependency>
       <!-- Spring面向切面编程 -->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-aspects</artifactId>
         <version>4.3.7.RELEASE</version>
       </dependency>
   
       <!-- Mybatis -->
       <dependency>
         <groupId>org.mybatis</groupId>
         <artifactId>mybatis</artifactId>
         <version>3.4.2</version>
       </dependency>
       <!-- Mybatis整合spring的适配包 -->
       <dependency>
         <groupId>org.mybatis</groupId>
         <artifactId>mybatis-spring</artifactId>
         <version>1.3.1</version>
       </dependency>
~~~


​       
```xml
	   <!-- 数据库连接池、驱动 -->
       <dependency>
         <groupId>c3p0</groupId>
         <artifactId>c3p0</artifactId>
         <version>0.9.1.2</version>
       </dependency>
       <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
         <version>5.1.41</version>
       </dependency>
```


​       
~~~xml
       <!-- jstl，servlet，junit -->
       <dependency>
         <groupId>jstl</groupId>
         <artifactId>jstl</artifactId>
         <version>1.2</version>
       </dependency>
       <dependency>
         <groupId>javax.servlet</groupId>
         <artifactId>servlet-api</artifactId>
         <version>2.5</version>
         <scope>provided</scope>
       </dependency>
       <dependency>
         <groupId>junit</groupId>
         <artifactId>junit</artifactId>
         <version>4.12</version>
         <scope>test</scope>
       </dependency>
     </dependencies>
   ```
~~~

### 1.2 引入bootstrap前端框架

~~~jsp
* webapp文件夹下创建static，复制bootstrap文件到该目录

* 引入jquery、bootstrap

  ```jsp
  <!-- 引入Jquery -->
  <script type="text/javascript" src="static/js/jquery-3.2.1.min.js"></script>
  <!-- 引入Bootstrap样式 -->
  <link href="static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  ```
~~~

### 1.3 编写SSM整合的关键配置文件

  * web.xml、spring、springmvc、mybatis

#### 1.3.1 配置web.xml

~~~xml
1. 配置spring容器

   在web.xml中配置

   在项目一启动时就启动spring容器

   配置ContextLoaderListener，加载spring的xml配置文件

```xml
<!-- 1、启动spring容器 -->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:applicationContext.xml</param-value>
</context-param>

<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

2. 创建applicationContext.xml（spring的配置文件）

3. 配置springmvc前端控制器

   在web.xml中配置

```xml
<!-- SpringMVC的前端控制器，拦截所有请求 -->
<servlet>
  <servlet-name>dispatcherServlet</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:springmvc.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>dispatcherServlet</servlet-name>
  <url-pattern>/</url-pattern>
</servlet-mapping>
```

4. 配置解决中文乱码的过滤器

   在web.xml中配置

```xml
  <!-- 解决中文乱码的过滤器，要放在所有过滤器之前 -->
  <filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceRequestEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
    <init-param>
      <param-name>forceResponseEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```
~~~
#### 1.3.2 配置springmvc.xml

​	即 dispatcherServlet-servlet.xml

1. 开启注解扫描

   只扫描Contoller注解

   ```xml
   <context:component-scan base-package="cn.sai" use-default-filters="false">
       <!-- 只扫描控制器，同时关闭默认设置 -->
       <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
   </context:component-scan>
   ```

2. 配置视图解析器对象

   WEB-INF下创建views，存放页面

   ```xml
   <!-- 配置视图解析器，方便页面返回 -->
   <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
       <property name="prefix" value="/WEB-INF/views/"></property>
       <property name="suffix" value=".jsp"></property>
   </bean>
   ```

3. 两个标准配置

   ```xml
   <!-- 两个标准配置 -->
   <!-- 将SpringMVC不能处理的请求交给tomcat -->
   <mvc:default-servlet-handler/>
   <!-- 开启MVC注解支持，能支持SpringMVC更高级的功能，JSR303校验，快捷的ajax。。。映射动态请求 -->
   <mvc:annotation-driven/>配置spring.xml
   ```

#### 1.3.3 配置spring.xml

​	即 applicationContext.xml

​	先引入xml的头信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans.xsd
   http://www.springframework.org/schema/context
   http://www.springframework.org/schema/context/spring-context.xsd
   http://www.springframework.org/schema/aop
   http://www.springframework.org/schema/aop/spring-aop.xsd
   http://www.springframework.org/schema/tx
   http://www.springframework.org/schema/tx/spring-tx.xsd">
```

1. 开启注解扫描

   除Controller

   ```xml
   <!-- 开启注解的扫描，希望处理service和dao，controller不需要spring框架处理 -->
   <context:component-scan base-package="cn.sai">
       <!-- 配置哪些不扫描 -->
       <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
   </context:component-scan>
   ```

2. 配置数据源

   先在resource目录下创建并写好数据源配置文件

   ```xml
   <!-- 数据源，事务控制等等 -->
   <context:property-placeholder location="classpath:dbconfig.properties"/>
   <bean id="pooledDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
       <property name="driverClass" value="${jdbc.driverClass}"></property>
       <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
       <property name="user" value="${jdbc.user}"></property>
       <property name="password" value="${jdbc.password}"></property>
   </bean>
   ```

3. 配置与mybatis的整合

   xml方法

   ```xml
   <!-- 配置和mybatis的整合 -->
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
       <!-- 指定mybatis全局配置文件的位置 -->
       <property name="configLocation" value="classpath:mybatis-config.xml"></property>
       <property name="dataSource" ref="pooledDataSource"></property>
       <!-- 指定mybatis，mapper文件的位置 -->
       <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
   </bean>
   <!-- 配置扫描器，将mybatis接口的实现加入到ioc容器中 -->
   <bean id="mapperScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
       <property name="basePackage" value="cn.sai.crud.dao"/>
   </bean>
   ```

4. 配置事务管理器

   ```xml
   <!-- 配置事务管理器 -->
   <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <property name="dataSource" ref="pooledDataSource"/>
   </bean>
   
   <!-- 配置事务通知 -->
   <aop:config>
       <!-- 切入点表达式 -->
       <aop:pointcut id="txPoint" expression="execution(* cn.sai.crud.service..*(..))"/>
       <!-- 配置事务增强 -->
       <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/>
   </aop:config>
   
   <!-- 配置事务增强，事务如何切入 -->
   <tx:advice id="txAdvice" transaction-manager="transactionManager">
       <tx:attributes>
           <!-- 所有方法都是事务方法 -->
           <tx:method name="*"/>
           <!-- get方法开启只读 -->
           <tx:method name="get*" read-only="true" />
       </tx:attributes>
   </tx:advice>
   ```

#### 1.3.4 配置mybatis.xml

​	引入配置文件头

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
```

1. 配置驼峰规则

   ```xml
   <settings>
       <setting name="mapUnderscoreToCamelCase" value="true"/>
   </settings>
   ```

2. 配置包别名

   ```xml
   <typeAliases>
       <package name="cn.sai.crud.bean"/>
   </typeAliases>
   ```

3. 使用mybatis的逆向工程创建mapper

   ```xml
   <!DOCTYPE generatorConfiguration PUBLIC
           "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
           "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
   <generatorConfiguration>
       <context id="DB2Tables" targetRuntime="MyBatis3">
   
           <!-- 取消注释 -->
           <commentGenerator>
               <property name="suppressAllComments" value="true" />
           </commentGenerator>
   
           <!-- 配置数据库连接信息 -->
           <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                           connectionURL="jdbc:mysql://localhost:3306/ssm_crud?characterEncoding=utf-8"
                           userId="blog"
                           password="123456"
           />
   
           <javaTypeResolver>
               <property name="forceBigDecimals" value="false"/>
           </javaTypeResolver>
   
           <!-- 指定javabean生成的位置 -->
           <javaModelGenerator
                   targetPackage="cn.sai.crud.bean"
                   targetProject="./src/main/java">
               <property name="enableSubPackages" value="true"/>
               <property name="trimStrings" value="true"/>
           </javaModelGenerator>
   
           <!-- 指定sql映射文件生成的位置 -->
           <sqlMapGenerator
                   targetPackage="mapper"
                   targetProject="./src/main/resources">
               <property name="enableSubPackages" value="true"/>
           </sqlMapGenerator>
           <!-- 指定dao接口生成的位置，mapper接口 -->
           <javaClientGenerator
                   type="XMLMAPPER"
                   targetPackage="cn.sai.crud.dao"
                   targetProject="./src/main/java">
               <property name="enableSubPackages" value="true"/>
           </javaClientGenerator>
   
           <!-- 指定每个表的生成策略 -->
           <table tableName="tbl_emp" domainObjectName="Employee"></table>
           <table tableName="tbl_dept" domainObjectName="Department"></table>
   
       </context>
   </generatorConfiguration>
   ```

   使用test类运行逆向工程生成bean等文件的代码

   ```java
   package cn.sai.crud.test;
   
   import org.mybatis.generator.api.MyBatisGenerator;
   import org.mybatis.generator.config.Configuration;
   import org.mybatis.generator.config.xml.ConfigurationParser;
   import org.mybatis.generator.internal.DefaultShellCallback;
   
   import java.io.File;
   import java.util.ArrayList;
   import java.util.List;
   
   public class MBGTest {
       public static void main(String[] args) throws Exception {
           List<String> warnings = new ArrayList<String>();
           boolean overwrite = true;
           File configFile = new File("mbg.xml");
           System.out.println(configFile.exists());
           ConfigurationParser cp = new ConfigurationParser(warnings);
           Configuration config = cp.parseConfiguration(configFile);
           DefaultShellCallback callback = new DefaultShellCallback(overwrite);
           MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
           myBatisGenerator.generate(null);
       }
   }
   ```

4. 修改逆向工程生成的文件

   * Employee中添加department成员变量，增加getter、setter方法

   * EmployeeMapper.java中添加两个方法，将select查询到的结果集中加入部门信息

     ```java
     //根据条件查询，返回带部门信息
     List<Employee> selectByExampleWithDept(EmployeeExample example);
     //根据id查询，返回带部门信息
     Employee selectByPrimaryKeyWithDept(Integer empId);
     ```

   * EmployeeMapper.xml中添加这两个方法的配置文件

     * 添加一个带部门信息的sql-list

       ```xml
       <sql id="WithDept_Column_List">
         e.emp_id, e.emp_name, e.gender, e.email, e.d_id, d.dept_id, d.dept_name
       </sql>
       ```

     * 添加一个带部门信息的结果集

       ```xml
       <resultMap id="WithDeptResultMap" type="cn.sai.crud.bean.Employee">
         <id column="emp_id" jdbcType="INTEGER" property="empId" />
         <result column="emp_name" jdbcType="VARCHAR" property="empName" />
         <result column="gender" jdbcType="CHAR" property="gender" />
         <result column="email" jdbcType="VARCHAR" property="email" />
         <result column="d_id" jdbcType="INTEGER" property="dId" />
         <!-- 指定联合查询出的部门字段的封装 -->
         <association property="department" javaType="cn.sai.crud.bean.Department">
           <id column="dept_id" property="deptId"/>
           <result column="dept_name" property="deptName"/>
         </association>
       </resultMap>
       ```

     * 增加联合查询得到部门信息的两个sql映射

       ```xml
       <!--//根据条件查询，返回带部门信息
         List<Employee> selectByExampleWithDept(EmployeeExample example);
         //根据id查询，返回带部门信息
         Employee selectByPrimaryKeyWithDept(Integer empId);-->
       <!-- 根据条件查询，带部门信息 -->
       <select id="selectByExampleWithDept" resultMap="WithDeptResultMap">
         select
         <if test="distinct">
           distinct
         </if>
         <include refid="WithDept_Column_List" />
         from tbl_emp e
         left join tbl_dept d on e.d_id=d.dept_id
         <if test="_parameter != null">
           <include refid="Example_Where_Clause" />
         </if>
         <if test="orderByClause != null">
           order by ${orderByClause}
         </if>
       </select>
       <!-- 根据id查询，带部门信息 -->
       <select id="selectByPrimaryKeyWithDept" resultMap="WithDeptResultMap">
         select
         <include refid="WithDept_Column_List" />
         from tbl_emp e
         left join tbl_dept d on e.d_id=d.dept_id
         where emp_id = #{empId,jdbcType=INTEGER}
       </select>
       ```

   5. 测试框架

      1. 生成Employee、Department有参构造方法。（生成的同时再次生成无参构造）

         Employee生成有参构造器时不添加department属性

      2. 配置测试文件

         在完成批量操作之前需要进行配置批量sqlsession（在applicationContext.xml中）
         
         ```xml
         <!-- 配置一个可以批量执行的SqlSession -->
         <bean id="sessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
             <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"/>
             <!-- 类型设置成批量 -->
             <constructor-arg name="executorType" value="BATCH"/>
         </bean>
         ```
         
         完成测试类
         
         ```java
         package cn.sai.crud.test;
         
         import cn.sai.crud.bean.Department;
         import cn.sai.crud.bean.Employee;
         import cn.sai.crud.dao.DepartmentMapper;
         import cn.sai.crud.dao.EmployeeMapper;
         import org.apache.ibatis.session.SqlSession;
         import org.junit.Test;
         import org.junit.runner.RunWith;
         import org.springframework.beans.factory.annotation.Autowired;
         import org.springframework.context.ApplicationContext;
         import org.springframework.context.support.ClassPathXmlApplicationContext;
         import org.springframework.test.context.ContextConfiguration;
         import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
         
         import java.util.UUID;
         
         /**
          * 测试dao层的工作
          * 推荐Spring的项目使用Spring的单元测试，可以自动注入我们需要的组件
          * 1、导入SpringTest模块
          * 2、使用@ContextConfiguration指定Spring配置文件的位置
          * 3、使用RunWith注解，调用SpringJUnit4ClassRunner进行测试
          * 4、直接autowired要使用的组件即可
          *
         */
         @RunWith(SpringJUnit4ClassRunner.class)
         @ContextConfiguration(locations = {"classpath:applicationContext.xml"})
         public class MapperTest {
             /**
              * 测试DepartmentMapper
             */
             @Autowired
             DepartmentMapper departmentMapper;
             @Autowired
             EmployeeMapper employeeMapper;
             @Autowired
             SqlSession sqlSession;
             @Test
             public void testCRUD(){
         //        //1、创建SpringIOC容器
         //        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
         //        //2、从容器中获取mapper
         //        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);
         //        System.out.println(departmentMapper);
                 //1、插入几个部门
         //        departmentMapper.insertSelective(new Department(null,"开发部"));
         //        departmentMapper.insertSelective(new Department(null,"测试部"));
                 //2、生成员工数据，测试
                 employeeMapper.insertSelective(new Employee(null,"sai","M","shenyingge@pku.edu.cn",1));
                 //3、批量插入多个员工，可以使用执行批量操作的SqlSession
         
         //        for (int i = 0; i < ; i++) {
         //            employeeMapper.insertSelective(new Employee(null,"sai","M","shenyingge@pku.edu.cn",1));
         //        }
         
                 /**
                  * 批量插入数据
                 */
                 /*
                 EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
                 for (int i = 0; i < 100; i++) {
                     String uid = UUID.randomUUID().toString().substring(0, 5) + i;
                     mapper.insertSelective(new Employee(null,uid,"M",uid+"@gmail.com", 1));
                 }
                 */
                 System.out.println("批量完成");
             }
         }
         ```
         
      
   6. 配置pagehelper（github查文档）

      1. 引入依赖

         ```xml
         <!-- 引入pagehelper分页插件 -->
         <dependency>
           <groupId>com.github.pagehelper</groupId>
           <artifactId>pagehelper</artifactId>
           <version>5.0.0</version>
         </dependency>
         ```

      2. 在mybatis-config.xml中配置

         ```xml
         <!-- 需要放在typeAliases后面 -->
         <plugins>
             <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
         </plugins>
         ```

## 2. 构建业务逻辑

### 2.1 查询业务

* 业务逻辑

* 1. 访问index.jsp页面
  2. index.jsp页面发送出查询员工列表请求
  3. EmployeeController来接收请求，查出员工数据
  4. 来到list.jsp页面展示

  URI：/emps



* 修改index.jsp，从主页转发到emps目录

  ```jsp
  <jsp:forward page="/emps"></jsp:forward>
  ```

* 写前端控制器Controller来处理请求

  同时创建对应的Service处理业务

  再使用对应的Dao层mapper来执行数据库操作

  1. EmployeeController

     ```java
     package cn.sai.crud.controller;
     
     import cn.sai.crud.bean.Employee;
     import cn.sai.crud.service.EmployeeService;
     import com.github.pagehelper.PageHelper;
     import com.github.pagehelper.PageInfo;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.stereotype.Controller;
     import org.springframework.ui.Model;
     import org.springframework.web.bind.annotation.RequestMapping;
     import org.springframework.web.bind.annotation.RequestParam;
     
     import java.util.List;
     
     //处理员工CRUD
     @Controller
     public class EmployeeController {
         @Autowired
         EmployeeService employeeService;
         //查询员工数据（分页查询）
         @RequestMapping("/emps")
         public String getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
             //这不是分页查询
             //引入PageHelper分页插件
             //在查询之前只需要调用，传入页码，以及每页显示条数
             PageHelper.startPage(pn,10);
             //在startPage后面紧跟的查询就会显示为分页查询
             List<Employee> emps = employeeService.getAll();
             //使用PageInfo包装结果
             //pageInfo封装了页面详细的信息（pageHelper提供）
             PageInfo page = new PageInfo(emps,5);  //navigatePages：显示的页数
             //Model将pageInfo交给页面
             model.addAttribute("pageInfo",page);
             //返回list，根据SpringMVC配置文件，视图解析器会直接到WEB-INF/views/下找list页面
             return "list";
         }
     }
     ```

     2. EmployeeService

     ```java
     package cn.sai.crud.service;
     
     import cn.sai.crud.bean.Employee;
     import cn.sai.crud.dao.EmployeeMapper;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.stereotype.Service;
     
     import java.util.List;
     
     @Service
     public class EmployeeService {
     
         @Autowired
         EmployeeMapper employeeMapper;
         //查询所有员工
         public List<Employee> getAll() {
             return employeeMapper.selectByExampleWithDept(null);
         }
     }
     ```

* 测试CRUD请求

* 

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
//        employeeMapper.insertSelective(new Employee(null,"sai","M","shenyingge@pku.edu.cn",1));
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

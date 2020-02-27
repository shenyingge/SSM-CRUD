package cn.sai.crud.test;

import cn.sai.crud.bean.Employee;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


//使用spring提供的单元测试，测试CRUD请求是否可行
//Spring4测试的时候，需要servlet3.0的支持
@RunWith(SpringJUnit4ClassRunner.class)
//传入SpringMVC的IOC容器
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:springmvc.xml"})
public class MVCTest {
    //传入SpringMVC的IOC
    @Autowired
    WebApplicationContext context;
    //虚拟mvc请求，获取到处理结果
    MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testPage() throws Exception {
        //模拟请求拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5")).andReturn();
        //请求成功以后。请求域中会有pageInfo，可以取出pageInfo进行验证
        MockHttpServletRequest request = result.getRequest();
        PageInfo pI = (PageInfo)request.getAttribute("pageInfo");
        System.out.println(pI.getPageNum());
        System.out.println(pI.getPages());
        System.out.println(pI.getTotal());
        int[] nums = pI.getNavigatepageNums();
        for (int i = 0; i < nums.length; i++) {
            System.out.printf(nums[i] + " ");
        }

        //获取员工数据
        List<Employee> list = pI.getList();
        for (Employee emp : list) {
            System.out.println("ID: "+emp.getEmpId() + "name: "+emp.getEmpName());
        }
        System.out.println(request.getContextPath());
    }

}

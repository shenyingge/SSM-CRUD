package cn.sai.crud.controller;

import cn.sai.crud.bean.Employee;
import cn.sai.crud.bean.Msg;
import cn.sai.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//处理员工CRUD
@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    //SpringMVC的ResponseBody可以直接把对象转为json，需要jackson包
    @ResponseBody
    @RequestMapping("/emps")
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1")Integer pn){
        PageHelper.startPage(pn,5);
        List<Employee> emps = employeeService.getAll();
        PageInfo page = new PageInfo(emps,5);  //navigatePages：显示的页数
        return Msg.success().add("pageInfo",page);
    }

    //查询员工数据（分页查询）
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
        //这不是分页查询
        //引入PageHelper分页插件
        //在查询之前只需要调用，传入页码，以及每页显示条数
        PageHelper.startPage(pn,5);
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

package cn.sai.crud.controller;

import cn.sai.crud.bean.Employee;
import cn.sai.crud.bean.Msg;
import cn.sai.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//处理员工CRUD
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //多个删除
    @ResponseBody
    @RequestMapping(value = "/emp/{ids}",method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("ids") String ids){
        //批量删除
        if(ids.contains("-")){
            String[] str_ids = ids.split("-");
            List<Integer> list = new ArrayList<>();
            for(String id : str_ids){
                list.add(Integer.parseInt(id));
            }
            employeeService.deleteBatch(list);
        }else{
            //单个删除
            employeeService.deleteEmp(Integer.parseInt(ids));
        }
        return Msg.success();
    }

    /*
    * 问题：
    * 如果直接发送ajax=PUT请求
    * 请求体中有数据但是Employee封装不上
    * 除了id之外的数据为null
    * update tbl_emp where emp_id = 1014
    * 原因：
    * Tomcat的问题
    * Tomcat不把PUT请求请求体中的数据封装成map，只有POST才封装map
    * 解决：
    * 要直接发送PUT
    * 在xml配置HttpPutFormContentFilter
    * 将请求体的数据解析包装成一个map
    * */
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(Employee employee, BindingResult result){
        if(result.hasErrors()){
            //校验失败，返回失败，显示错误信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println("错误字段名："+error.getField());
                System.out.println("错误信息：+error.getDefaultMessage()");
                map.put(error.getField(),error.getDefaultMessage());
            }
            return Msg.failure().add("errorfields", map);
        }else{
            employeeService.updateEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){

        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    //检查用户名是否重复
    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkUser(@RequestParam("EmpName") String EmpName){
        //先判断用户名是否合法
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
        if(!EmpName.matches(regx)){
            return Msg.failure().add("va_msg","用户名非法");
        }

        //数据库校验
        boolean b = employeeService.checkUser(EmpName);
        if(b){
            return Msg.success();
        }else{
            return Msg.failure().add("va_msg","用户名已注册");
        }
    }

    //员工保存
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if(result.hasErrors()){
            //校验失败，返回失败，显示错误信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println("错误字段名："+error.getField());
                System.out.println("错误信息：+error.getDefaultMessage()");
                map.put(error.getField(),error.getDefaultMessage());
            }
            return Msg.failure().add("errorfields", map);
        }else{
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

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

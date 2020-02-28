package cn.sai.crud.controller;

import cn.sai.crud.bean.Department;
import cn.sai.crud.bean.Msg;
import cn.sai.crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//处理和部门有关的请求
@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //返回所有的部门信息
    @ResponseBody
    @RequestMapping("/depts")
    public Msg getDepts(){

        List<Department> list = departmentService.getDepts();

        return Msg.success().add("depts",list);
    }

}

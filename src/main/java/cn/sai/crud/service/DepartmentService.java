package cn.sai.crud.service;

import cn.sai.crud.bean.Department;
import cn.sai.crud.bean.Msg;
import cn.sai.crud.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    //查询所有部门
    public List<Department> getDepts() {
        return departmentMapper.selectByExample(null);
    }
}

package cn.sai.crud.service;

import cn.sai.crud.bean.Employee;
import cn.sai.crud.bean.EmployeeExample;
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

    //保存员工信息
    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    //检查用户名，true代表可用
    public boolean checkUser(String EmpName) {
        EmployeeExample example = new EmployeeExample();
        //创建查询条件
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(EmpName);
        employeeMapper.selectByExample(example);
        long count = employeeMapper.countByExample(example);
        return count == 0;
    }
}

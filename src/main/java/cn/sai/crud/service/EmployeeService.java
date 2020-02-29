package cn.sai.crud.service;

import cn.sai.crud.bean.Employee;
import cn.sai.crud.bean.EmployeeExample;
import cn.sai.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    //查询所有员工
    public List<Employee> getAll() {

        EmployeeExample example = new EmployeeExample();
        example.setOrderByClause("emp_id ASC");
        List<Employee> list = employeeMapper.selectByExampleWithDept(example);
/*        for(Employee e:list){
            System.out.println(e.getEmpId());
        }*/
        return list;
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

    //根据id查询
    public Employee getEmp(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        return employee;
    }
    //根据id删除
    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        //delete from xxx where emp_id in(1,2,3)
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }
}

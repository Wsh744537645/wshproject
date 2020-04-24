package com.stephendemo.controller;

import com.stephendemo.elasticsearch.Employee;
import com.stephendemo.elasticsearch.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2020-04-24
 */

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/es/employee/add")
    public Employee add(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/es/employee/delete")
    public Employee delete(String id){
        Employee employee = employeeRepository.queryEmployeeById(id);
        if(employee != null){
            employeeRepository.delete(employee);
        }
        return employee;
    }

    @GetMapping("/es/employee/update")
    public Employee update(String id, String firstName){
        Employee employee = employeeRepository.queryEmployeeById(id);
        if(employee != null){
            employee.setFirstName(firstName);
            employeeRepository.save(employee);
        }
        return employee;
    }

    @GetMapping("/es/employee/query")
    public Employee query(String id){
        //return queryEmployeeById(id);
        return employeeRepository.queryEmployeeById(id);
    }

    private Employee queryEmployeeById(String id){
        return employeeRepository.findById(id).get();
    }
}
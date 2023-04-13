package com.szherbekov.spring.rest.service;

import com.szherbekov.spring.rest.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {
    public List<Employee> getAllEmployees ();

    public void saveEmployee (Employee employee);

    public Employee getEmployee (int id);

    public void deleteEmployee (int id);
}

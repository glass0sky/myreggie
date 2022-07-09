package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.entity.Employee;
import com.mdy.myreggie.mapper.EmployeeMapper;
import com.mdy.myreggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

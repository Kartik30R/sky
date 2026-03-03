package com.askver.sky.controller;

import com.askver.sky.DTO.CreateEmployeeRequest;
import com.askver.sky.model.Company;
import com.askver.sky.model.Employee;
import com.askver.sky.model.Role;
import com.askver.sky.repo.EmployeeRepository;
import com.askver.sky.userdetail.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String createEmployee(
            @RequestBody CreateEmployeeRequest request,
            Authentication authentication
    ){

        CustomUserDetails admin =
                (CustomUserDetails)
                        authentication.getPrincipal();

        UUID companyId = admin.getCompanyId();

        Company company = new Company();
        company.setId(companyId);

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        employee.setCompany(company);
        employee.setRole(Role.ROLE_EMPLOYEE);

        employeeRepository.save(employee);

        return "Employee created successfully";
    }
}
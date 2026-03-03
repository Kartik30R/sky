package com.askver.sky.userdetail;

import com.askver.sky.model.Company;
import com.askver.sky.model.Employee;
import com.askver.sky.repo.CompanyRepository;
import com.askver.sky.repo.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final CompanyRepository companyRepo;
    private final EmployeeRepository employeeRepo;

    public CustomUserDetailsService(
            CompanyRepository companyRepo,
            EmployeeRepository employeeRepo
    ) {
        this.companyRepo = companyRepo;
        this.employeeRepo = employeeRepo;
    }

    public UserDetails loadUserByUsername(
            String email
    ) {

         Optional<Company> company =
                companyRepo.findByEmail(email);

        if (company.isPresent()) {

            Company c = company.get();

            return new CustomUserDetails(
                    c.getId(),
                    c.getId(),
                    c.getEmail(),
                    c.getPassword(),
                    c.getRole()
            );
        }

         Employee e = employeeRepo
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found")
                );

        return new CustomUserDetails(
                e.getId(),
                e.getCompany().getId(),
                e.getEmail(),
                e.getPassword(),
                e.getRole()
        );
    }
}
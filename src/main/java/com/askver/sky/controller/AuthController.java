package com.askver.sky.controller;

import com.askver.sky.DTO.LoginRequest;
import com.askver.sky.DTO.RegisterCompanyRequest;
import com.askver.sky.model.Company;
import com.askver.sky.model.Role;
import com.askver.sky.repo.CompanyRepository;
import com.askver.sky.userdetail.CustomUserDetails;
import com.askver.sky.userdetail.CustomUserDetailsService;
import com.askver.sky.userdetail.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    public AuthController(
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            CustomUserDetailsService userService,
            PasswordEncoder passwordEncoder,
            CompanyRepository companyRepository
    ){
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request
    ){

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails user =
                (CustomUserDetails)
                        userService.loadUserByUsername(
                                request.getEmail()
                        );

        return jwtUtil.generateToken(user);
    }

    @PostMapping("/register-company")
    public String registerCompany(
            @RequestBody RegisterCompanyRequest request
    ){

        if(companyRepository
                .findByEmail(request.getEmail())
                .isPresent()){
            return "Company already exists";
        }

        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setEmail(request.getEmail());
        company.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        company.setRole(Role.ROLE_ADMIN);

        companyRepository.save(company);

        return "Company registered successfully";
    }
}
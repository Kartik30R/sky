package com.askver.sky.DTO;

import com.askver.sky.model.Role;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    private String name;
    private String email;
    private String password;
    private Role role;

}
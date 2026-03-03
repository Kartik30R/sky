package com.askver.sky.DTO;


import lombok.Data;

@Data
public class RegisterCompanyRequest {

     private String companyName;

     private String email;

     private String password;

}
package com.example.dolbomi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
    private String name;
    private String adminPhoneNo;
    private String password;
    private String registerNo;

    public String getName(){
        return name;
    }

    public String getAdminPhoneNo(){
        return adminPhoneNo;
    }

    public String getPassword(){
        return password;
    }

    public String getRegisterNo(){
        return registerNo;
    }
}

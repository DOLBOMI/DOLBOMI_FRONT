package com.example.dolbomi;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleUser {

    @SerializedName("userIndex")
    private Integer userIndex;
    @SerializedName("name")
    private String name;
    @SerializedName("registerNo")
    private Integer registerNo;
    @SerializedName("status")
    private Integer status;

    public  SimpleUser() {

    }
    @Builder
    public SimpleUser(Integer userIndex, String name, Integer registerNo, Integer status) {
        this.userIndex = userIndex;
        this.name = name;
        this.registerNo = registerNo;
        this.status = status;
    }
}
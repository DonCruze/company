package com.example.company.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserDto {
    private String userName;
    private Long given;
    private Map<String,Long> needTo;
    private Map<String,Long> needGive;
}

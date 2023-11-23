package com.example.company.dto;

import lombok.Data;

@Data
public class UserDto {
    private String userName;
    private Long moneyGive;
    private Long moneyNeed;
    private Long moneyMostReturn;
}

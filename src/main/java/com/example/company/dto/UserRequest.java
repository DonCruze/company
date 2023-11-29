package com.example.company.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String partyName;
    private Long given;
}

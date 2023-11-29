package com.example.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private String partyName;
    private Long totalForAll;
    private List<UserDto> users;

}

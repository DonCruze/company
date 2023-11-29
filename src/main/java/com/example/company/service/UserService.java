package com.example.company.service;

import com.example.company.dto.UserRequest;
import com.example.company.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
   ResponseEntity<?> getBalanceParty(String partyName);

   ResponseEntity<?> getAllParty();

   ResponseEntity<?> addUser(UserRequest request);

   ResponseEntity<?> deleteUser(Long userId);
}

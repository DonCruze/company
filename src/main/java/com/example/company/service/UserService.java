package com.example.company.service;

import com.example.company.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
   ResponseEntity<?> getBalance();

   ResponseEntity<?> addUser(UserRequest request);

   ResponseEntity<?> deleteUser(Long userId);
}

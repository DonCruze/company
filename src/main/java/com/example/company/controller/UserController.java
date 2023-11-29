package com.example.company.controller;

import com.example.company.dto.UserRequest;
import com.example.company.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getOneParty(@PathVariable String name) {
        return userService.getBalanceParty(name);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllParty() {
        return userService.getAllParty();
    }

    @PostMapping()
    public ResponseEntity<?> user(
            @RequestBody UserRequest dto
    ) {
        return userService.addUser(dto);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return userService.deleteUser(id);
    }
}

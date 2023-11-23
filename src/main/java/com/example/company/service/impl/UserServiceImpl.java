package com.example.company.service.impl;

import com.example.company.dto.UserRequest;
import com.example.company.dto.UserDto;
import com.example.company.dto.UserResponse;
import com.example.company.entity.UserEntity;
import com.example.company.repository.UserRepository;
import com.example.company.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public ResponseEntity<?> getBalance() {

        List<UserEntity> users = repository.findAllByState(1);

        long needOrGet;

        if (users.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        long totalMoney = users.stream().mapToLong(UserEntity::getMoney).sum();
        long forAll = totalMoney / users.size();

        for (UserEntity e : users) {
            needOrGet = e.getMoney() - forAll;
            long needTo = Math.max(0, needOrGet);
            long  needGive = Math.max(0, -needOrGet);

            e.setNeedGive(needGive);
            e.setNeedTo(needTo);
        }

        repository.saveAll(users);

        return ResponseEntity.ok(new UserResponse(forAll,entitiesToDtos(users)));
    }

    @Override
    public ResponseEntity<?> addUser(UserRequest request) {
        String trimmedUserName = StringUtils.trimWhitespace(request.getUserName());

        if (!StringUtils.hasText(trimmedUserName)) {
            throw new RuntimeException("User name is empty");
        }

        Optional<UserEntity> optionalUser = repository.findFirstByUserName(trimmedUserName);
        UserEntity user = optionalUser.orElseGet(UserEntity::new);
        user.setUserName(trimmedUserName);
        user.setMoney(request.getGiven());
        user.setState(1);
        repository.save(user);

        return ResponseEntity.ok("User saved");
    }



    @Override
    public ResponseEntity<?> deleteUser(Long userId) {
        Optional<UserEntity> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setState(0);
            repository.save(user);
            return ResponseEntity.ok("user deleted");
        }

        throw new RuntimeException("userId not found");
    }

    public List<UserDto> entitiesToDtos(List<UserEntity> entities) {
        List<UserDto> collect = entities.stream()
                .map((UserEntity entity) -> entityToDto(entity))
                .collect(Collectors.toList());
        return collect;
    }

    public UserDto entityToDto(UserEntity entity) {
        UserDto dto = new UserDto();
        Optional.ofNullable(entity.getUserName()).ifPresent(dto::setUserName);
        Optional.ofNullable(entity.getMoney()).ifPresent(dto::setMoneyGive);
        Optional.ofNullable(entity.getNeedGive()).ifPresent(dto::setMoneyMostReturn);
        Optional.ofNullable(entity.getNeedTo()).ifPresent(dto::setMoneyNeed);
        return dto;
    }
}

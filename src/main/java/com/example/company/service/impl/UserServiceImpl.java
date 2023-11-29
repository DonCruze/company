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

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public ResponseEntity<?> getBalanceParty(String partyName) {

        return ResponseEntity.ok(getBalance(partyName));
    }

    public UserResponse getBalance(String partyName){
        List<UserEntity> users = repository.findAllByStateAndPartyName(1, partyName);

        if (users.isEmpty()) {
            throw new RuntimeException("User list is empty");
        }

        long totalMoney = users.stream().mapToLong(UserEntity::getMoney).sum();
        long forAll = totalMoney / users.size();

        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity e : users) {
            long needOrGet = e.getMoney() - forAll;
            long moneyNeed = Math.max(0, needOrGet);
            long moneyGive = Math.max(0, -needOrGet);

            List<String> usersToGive = users.stream()
                    .filter(x -> moneyGive != 0 && (x.getMoney() - forAll) > 0)
                    .map(UserEntity::getUserName)
                    .toList();

            Map<String, Long> needGiveMap = new HashMap<>();
            if (moneyGive == forAll) {
                long share = moneyGive / usersToGive.size();
                usersToGive.forEach(username -> needGiveMap.put(username, share));
            } else {
                usersToGive.forEach(username -> needGiveMap.put(username, Math.min(moneyGive, forAll)));
            }

            List<String> usersToGet = users.stream()
                    .filter(x -> moneyNeed != 0 && (x.getMoney() - forAll) < 0)
                    .map(UserEntity::getUserName)
                    .toList();

            Map<String, Long> needToMap = new HashMap<>();
            for (String username : usersToGet) {
                long userMoney = forAll - users.stream().filter(u -> u.getUserName().equals(username)).findFirst().map(UserEntity::getMoney).orElse(0L);
                if (userMoney == forAll) {
                    userMoney = e.getMoney() - forAll;
                }
                long share = Math.min(userMoney, forAll);
                needToMap.put(username, share);
            }

            UserDto userDto = new UserDto();
            userDto.setUserName(e.getUserName());
            userDto.setGiven(e.getMoney());
            userDto.setNeedTo(needToMap);
            userDto.setNeedGive(needGiveMap);

            userDtos.add(userDto);
        }

        repository.saveAll(users);

        return new UserResponse(partyName, forAll, userDtos);
    }

    @Override
    public ResponseEntity<?> getAllParty() {
        List<UserEntity> parties = repository.findAllByState(1);

        List<String> uniquePartyNames = parties.stream()
                .map(UserEntity::getPartyName)
                .distinct()
                .toList();
        List<UserResponse> response = new ArrayList<>();
        for (String s : uniquePartyNames){

            response.add(getBalance(s));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> addUser(UserRequest request) {
        String trimmedUserName = StringUtils.trimWhitespace(request.getUserName());
        String trimmedPartyName = StringUtils.trimWhitespace(request.getPartyName());

        if (!StringUtils.hasText(trimmedUserName)) {
            throw new RuntimeException("User name is empty");
        }

        Optional<UserEntity> optionalUser = repository.findFirstByUserNameAndPartyName(trimmedUserName,trimmedPartyName);
        UserEntity user = optionalUser.orElseGet(UserEntity::new);
        user.setUserName(trimmedUserName);
        user.setPartyName(request.getPartyName());
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

}

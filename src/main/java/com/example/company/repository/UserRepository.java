package com.example.company.repository;

import com.example.company.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByState(Integer state);
    List<UserEntity> findAllByStateAndPartyName(Integer state,String partyName);

    Optional<UserEntity> findFirstByUserNameAndPartyName(String userName, String partyName);
}

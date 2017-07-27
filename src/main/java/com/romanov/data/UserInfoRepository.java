package com.romanov.data;

import com.romanov.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInfoRepository extends CrudRepository<User, Long> {

    @Override
    <S extends User> S save(S entity);

    void deleteById(Long id);

    List<User> findByEmail(String email);
}

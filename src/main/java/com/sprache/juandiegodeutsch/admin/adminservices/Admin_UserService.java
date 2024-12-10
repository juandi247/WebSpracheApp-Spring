package com.sprache.juandiegodeutsch.admin.adminservices;


import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class Admin_UserService {

    private final UserRepository userRepository;


    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
}

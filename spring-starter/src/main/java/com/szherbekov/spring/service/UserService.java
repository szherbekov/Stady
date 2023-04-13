package com.szherbekov.spring.service;

import com.szherbekov.spring.database.entity.Company;
import com.szherbekov.spring.database.repository.CrudRepository;
import com.szherbekov.spring.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CrudRepository<Integer, Company> companyRepository;
}

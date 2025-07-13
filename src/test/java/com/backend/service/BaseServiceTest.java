package com.backend.service;

import com.backend.DatabaseCleanerExtension;
import com.backend.repository.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(DatabaseCleanerExtension.class)
@SpringBootTest
public abstract class BaseServiceTest {

    @Autowired
    protected MemberRepository memberRepository;
}

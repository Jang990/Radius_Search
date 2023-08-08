package com.example.demo.repository;

import com.example.demo.domain.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class PostDB_ClientBulkRepositoryTest {

    @Autowired
    PostDB_ClientBulkRepository clientBulkRepository;

    @Test
    void saveClientWithGroup() {

    }
}
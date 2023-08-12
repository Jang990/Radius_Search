package com.example.demo.repository;

import com.example.demo.domain.Client;
import com.example.demo.domain.ClientLocation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class QueryTest {
    @Autowired
    JPAQueryFactory query;

    @Autowired PostDB_ClientBulkRepository clientBulkRepository;
    @Autowired ClientRepository clientRepository;

    long start, end, runningTimeNano;

    @BeforeEach
    void beforeEach() {
        List<Client> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Client client = new Client(i, 0.001);
            list.add(client);
        }

        clientBulkRepository.saveClient(list);
    }

    @Test
    void postdb() {
        start = System.nanoTime();

        double nowX = 125.001;
        double nowY = 35.001;
        ClientLocation location = new ClientLocation(nowY, nowX);

        List<Client> result = clientRepository.findTestST_Dwithin(location.getLocation());
        System.out.println(result.size());

        end = System.nanoTime();
        runningTimeNano = end - start;
        double runningTimeMillis = runningTimeNano / 1_000_000.0;
        System.out.println("동작 시간: " + runningTimeMillis);
        // 약 10ms
    }

    @Test
    void postdb_distance() {
        start = System.nanoTime();

        double nowX = 125.001;
        double nowY = 35.001;
        ClientLocation location = new ClientLocation(nowY, nowX);

        List<Client> result = clientRepository.findPostgreSQLST_Distance(location.getLocation());
        System.out.println(result.size());

        end = System.nanoTime();
        runningTimeNano = end - start;
        double runningTimeMillis = runningTimeNano / 1_000_000.0;
        System.out.println("동작 시간: " + runningTimeMillis);
        // 약 400ms
    }
}

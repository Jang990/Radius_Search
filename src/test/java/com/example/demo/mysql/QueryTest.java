package com.example.demo.mysql;

import com.example.demo.domain.Client;
import com.example.demo.domain.ClientLocation;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.MySQL_ClientBulkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class QueryTest {
    @Autowired ClientRepository clientRepository;
    @Autowired MySQL_ClientBulkRepository clientBulkRepository;

    long start, end, runningTimeNano;

    @BeforeEach
    void beforeEach() {
        List<Client> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Client client = new Client(i, 0.001);
            list.add(client);
        }
//        clientRepository.saveAll(list);
        clientBulkRepository.saveClient(list);
    }

    @Test
    void test1() {
        start = System.nanoTime();

        ClientLocation location = new ClientLocation(35.001d, 125.001d);
        List<Client> result = clientRepository.findMySQLST_Distance(location.getLocation());
        System.out.println(result.size());

        end = System.nanoTime();
        runningTimeNano = end - start;
        double runningTimeMillis = runningTimeNano / 1_000_000.0;
        System.out.println("동작 시간: " + runningTimeMillis);
        // 약 146ms
    }

    @Test
    void test2() {
        start = System.nanoTime();

        ClientLocation location = new ClientLocation(35.001d, 125.001d);
        Polygon polygon = createSquareAroundPoint(35.001d, 125.001d, 3);
        List<Client> result = clientRepository.findMySQLST_Contains(polygon, location.getLocation(), 3000);
        System.out.println(result.size());

        end = System.nanoTime();
        runningTimeNano = end - start;
        double runningTimeMillis = runningTimeNano / 1_000_000.0;
        System.out.println("동작 시간: " + runningTimeMillis);
        // 약 146ms
    }

    public Polygon createSquareAroundPoint(double latitude, double longitude, double radiusInKm) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        // Calculate the coordinates of the square's vertices
        double halfSideLength = radiusInKm / (111.32 * Math.cos(Math.toRadians(latitude)));
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(longitude - halfSideLength, latitude - halfSideLength);
        coordinates[1] = new Coordinate(longitude + halfSideLength, latitude - halfSideLength);
        coordinates[2] = new Coordinate(longitude + halfSideLength, latitude + halfSideLength);
        coordinates[3] = new Coordinate(longitude - halfSideLength, latitude + halfSideLength);
        coordinates[4] = coordinates[0];  // Close the ring

        Polygon square = geometryFactory.createPolygon(coordinates);

        return square;
    }
}

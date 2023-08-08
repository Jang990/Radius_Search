package com.example.demo.repository;

import com.example.demo.domain.Client;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.locationtech.jts.geom.Point;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // ============================ PostgreSQL 관련 ============================
    @Query(value = "select c from Client c where st_dwithin(c.location.location, :point, 3000, false) = true")
    List<Client> findTestST_Dwithin(@Param("point") Point point);

    @Query(value = "select c from Client c where ST_DistanceSphere(c.location.location, :point) <= 3000")
    List<Client> findPostgreSQLST_Distance(@Param("point") Point point);

    // ============================ MySQL 관련 ============================
    @Query(value = "select c from Client c where ST_Distance_Sphere(c.location.location, :point) <= 3000")
    List<Client> findMySQLST_Distance(@Param("point") Point point);

    @Query(value = "select c from Client c where " +
            "ST_Contains(:polygon, :point) " +
            "AND ST_Distance_Sphere(c.location.location, :point) <= :distanceMeter")
    List<Client> findMySQLST_Contains(Polygon polygon, @Param("point") Point point, int distanceMeter);
}

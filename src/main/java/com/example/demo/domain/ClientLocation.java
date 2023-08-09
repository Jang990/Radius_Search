package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Embeddable
@Getter
@NoArgsConstructor
@ToString
public class ClientLocation {

//    @Column(columnDefinition = "geometry(Point, 4326)") // PostgreSQL
//    @Column(columnDefinition = "POINT SRID 4326") // MySQL
    private Point location;

    public ClientLocation(Double latitude, Double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}

package com.example.demo.repository;

import com.example.demo.domain.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class MySQL_ClientBulkRepository {
    private final JdbcTemplate template;

    public void saveClient(List<Client> newClient) {
        final String insertSQL = "INSERT INTO " +
                "client (name, phone_number, address, detail, \"location\", created_at, updated_at) " +
                "VALUES(?, ?, ?, ?, ST_GeomFromText(?, 4326), NOW(), NOW())";

        template.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Client client = newClient.get(i);
                ps.setString(1, client.getName());
                setStringOrSetNull(ps, 2, client.getPhoneNumber());
                setStringOrSetNull(ps, 3, client.getAddress() == null ? null : client.getAddress().getAddress());
                setStringOrSetNull(ps, 4, client.getAddress() == null ? null : client.getAddress().getDetail());
                ps.setString(5, client.getLocation().getLocation().toText());
//                ps.setDouble(6, 33d);

//                setDoubleOrSetNull(ps, 5, client.getLocation() == null ? null : client.getLocation().getLocation().getX());
//                setDoubleOrSetNull(ps, 6, client.getLocation() == null ? null : client.getLocation().getLocation().getY());
            }

            private void setStringOrSetNull(PreparedStatement ps, int index, String value) throws SQLException {
                if (value != null) {
                    ps.setString(index, value);
                } else {
                    ps.setNull(index, Types.VARCHAR);
                }
            }

            private void setDoubleOrSetNull(PreparedStatement ps, int index, Double value) throws SQLException {
                if (value != null) {
                    ps.setDouble(index, value);
                } else {
                    ps.setNull(index, Types.DOUBLE);
                }
            }

            @Override
            public int getBatchSize() {
                return newClient.size();
            }
        });
    }

}
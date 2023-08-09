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
                "client (name, phone_number, address, detail, location, created_at, updated_at) " +
                "VALUES(?, ?, ?, ?, ST_GeomFromText(?, 4326), NOW(), NOW())";

        template.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Client client = newClient.get(i);
                ps.setString(1, client.getName());
                setStringOrSetNull(ps, 2, client.getPhoneNumber());
                setStringOrSetNull(ps, 3, client.getAddress() == null ? null : client.getAddress().getAddress());
                setStringOrSetNull(ps, 4, client.getAddress() == null ? null : client.getAddress().getDetail());
//                ps.setString(5, client.getLocation().getLocation().toText());

                /*
                PostGis와는 다르게 위도 경도를 뒤집어서 저장한다. - 영어 사용자가 위도 경도라고 말하는 경향이 있어서 그렇다나 뭐라나...
                https://dba.stackexchange.com/questions/242001/mysql-8-st-geomfromtext-giving-error-latitude-out-of-range-in-function-st-geomfr
                해당 글을 참고하면 좋다.
                 */
                ps.setString(5, "POINT(" + client.getLocation().getLocation().getY() +" " + client.getLocation().getLocation().getX()+")");
            }

            private void setStringOrSetNull(PreparedStatement ps, int index, String value) throws SQLException {
                if (value != null) {
                    ps.setString(index, value);
                } else {
                    ps.setNull(index, Types.VARCHAR);
                }
            }

            @Override
            public int getBatchSize() {
                return newClient.size();
            }
        });
    }

}
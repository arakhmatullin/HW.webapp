package ru.itpark.service;

import lombok.var;
import ru.itpark.domain.Auto;
import ru.itpark.util.JdbcTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoService {
    private final DataSource ds;

    public AutoService() throws NamingException, SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS autos (id TEXT PRIMARY KEY , name TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT);";
        Context context = new InitialContext();
        ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        try (Connection conn = ds.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);

            }

        }
    }

    public List<Auto> getAll() throws SQLException {
        final String sql = "SELECT id, name, description, imageUrl FROM autos";
        return JdbcTemplate.execQuery(ds,sql, rs -> new Auto(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("imageUrl")
                )
        );
    }

    public void create(String name, String description, String imageUrl) throws SQLException {
        final String sql = "INSERT INTO autos (id, name, description, imageUrl) VALUES (?, ?, ?, ?)";
        JdbcTemplate.execUpdate(ds,sql, stmt-> {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, imageUrl);
        }
        );

    }
}




































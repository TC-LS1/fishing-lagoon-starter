package com.drpicox.fishingLagoon.phase2.persistence;

import com.drpicox.fishingLagoon.bots.BotId;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TraitorsStore {

    private Connection connection;

    public TraitorsStore(Connection connection) throws SQLException {
        this.connection = connection;

        try (var stmt = connection.createStatement()) {
            stmt.execute("" +
                    "CREATE TABLE IF NOT EXISTS traitors (" +
                    "  traitorId VARCHAR(255)," +
                    "  PRIMARY KEY (traitorId)" +
                    ")"
            );
        }
    }

    public Set<BotId> list() {
        var result = new HashSet<BotId>();

        try (var stmt = connection.prepareStatement("SELECT * FROM traitors")) {
            try (var rows = stmt.executeQuery()) {
                while (rows.next()) {
                    String traitorId = rows.getString("traitorId");
                    result.add(new BotId(traitorId));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

    public void save(BotId traitorId) {
        try (var stmt = connection.prepareStatement("MERGE INTO traitors(traitorId) VALUES (?)")) {
            stmt.setString(1, traitorId.getValue());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

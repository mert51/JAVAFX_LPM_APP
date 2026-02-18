package com.sau.lpm.lpm_p1.db;

import com.sau.lpm.lpm_p1.dto.Place;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PlaceCrudOperations {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "silyaz";

    // Get a place by id
    public Optional<Place> getPlaceById(int id) {
        Place place = null;
        String query = "SELECT id, building, floor, room, seat FROM place WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    place = new Place();
                    place.setId(rs.getInt("id"));
                    place.setBuilding(rs.getString("building"));
                    place.setFloor(rs.getString("floor"));
                    place.setRoom(rs.getString("room"));
                    place.setSeat(rs.getInt("seat"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return place != null ? Optional.of(place) : Optional.empty();
    }

    // Insert a place by id
    public int insertPlaceById(Place place) {
        int result = 0;
        String insertSql = "INSERT INTO place (id, building, floor, room, seat) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Check if there exist a record on that id
            if (getPlaceById(place.getId()).isPresent()) {
                return -1; // already exists
            }
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setInt(1, place.getId());
                ps.setString(2, place.getBuilding());
                ps.setString(3, place.getFloor());
                ps.setString(4, place.getRoom());
                ps.setInt(5, place.getSeat());
                result = ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Delete a place by id
    public int deletePlaceById(int id) {
        int result = 0;
        String deleteSql = "DELETE FROM place WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(deleteSql)) {
            ps.setInt(1, id);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Update a place by id
    public int updatePlaceById(Place place) {
        int result = 0;
        String updateSql = "UPDATE place SET building = ?, floor = ?, room = ?, seat = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Check if there exist a record on that id
            if (getPlaceById(place.getId()).isPresent()) {
                try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                    ps.setString(1, place.getBuilding());
                    ps.setString(2, place.getFloor());
                    ps.setString(3, place.getRoom());
                    ps.setInt(4, place.getSeat());
                    ps.setInt(5, place.getId());
                    result = ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }
}


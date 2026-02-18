package com.sau.lpm.lpm_p1.db;

import com.sau.lpm.lpm_p1.dto.Reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ReservationCrudOperations {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "silyaz";

    // Get a reservation by studentId and placeId
    public Optional<Reservation> getReservationByStudentAndPlace(int studentId, int placeId) {
        Reservation reservation = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("RESERVATION GET - Database connected");
            String query = "SELECT * FROM reservation WHERE student_id = ? AND place_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, placeId);
            System.out.println("RESERVATION QUERY: StudentId=" + studentId + ", PlaceId=" + placeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reservation = new Reservation();
                reservation.setStudentId(resultSet.getInt("student_id"));
                reservation.setPlaceId(resultSet.getInt("place_id"));
                reservation.setDate(resultSet.getString("date"));
                reservation.setDuration(resultSet.getInt("duration"));
                System.out.println("RESERVATION FOUND: " + reservation.getDate());
            }
            if (reservation == null) {
                System.out.println("StudentId=" + studentId + ", PlaceId=" + placeId + " NOT FOUND");
            }
        } catch (SQLException throwables) {
            System.err.println("RESERVATION GET ERROR:");
            throwables.printStackTrace();
        }
        if (reservation != null)
            return Optional.of(reservation);
        else
            return Optional.empty();
    }

    // Insert a reservation
    public int insertReservation(Reservation reservation) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("RESERVATION INSERT - Database connected");
            // Check if there exist a record on that id
            try {
                if (getReservationByStudentAndPlace(reservation.getStudentId(), reservation.getPlaceId()).isPresent()) {
                    System.out.println("INSERT - Reservation already exists");
                    result = -1;
                    return result;
                }
            } catch (Exception e) {
                System.out.println("control error: " + e.getMessage());
            }

            String query = "INSERT INTO reservation (student_id, place_id, date, duration) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, reservation.getStudentId());
            statement.setInt(2, reservation.getPlaceId());

            // Convert String date to java.sql.Date
            LocalDate localDate = LocalDate.parse(reservation.getDate(), DateTimeFormatter.ISO_DATE);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            statement.setDate(3, sqlDate);

            statement.setInt(4, reservation.getDuration());
            System.out.println("INSERT query: StudentId=" + reservation.getStudentId() + ", PlaceId=" + reservation.getPlaceId() + ", Date=" + reservation.getDate() + ", Duration=" + reservation.getDuration());
            result = statement.executeUpdate();
            System.out.println("INSERT is done!: " + result + ")");
        } catch (SQLException e) {
            System.err.println("RESERVATION INSERT ERROR:");
            e.printStackTrace();

            // Check for foreign key constraint violation
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                if (e.getMessage().contains("student_id")) {
                    throw new RuntimeException("Student ID " + reservation.getStudentId() + " not found in database!");
                } else if (e.getMessage().contains("place_id")) {
                    throw new RuntimeException("Place ID " + reservation.getPlaceId() + " not found in database!");
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("RESERVATION INSERT ERROR:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Delete a reservation by studentId and placeId
    public int deleteReservation(int studentId, int placeId) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("RESERVATION DELETE - Database connected");
            String query = "DELETE FROM reservation WHERE student_id = ? AND place_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, placeId);
            System.out.println("DELETE query: StudentId=" + studentId + ", PlaceId=" + placeId);
            result = statement.executeUpdate();
            System.out.println("DELETE is done !: " + result + ")");
        } catch (Exception e) {
            System.err.println("RESERVATION DELETE ERROR:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Update a reservation by studentId and placeId
    public int updateReservation(Reservation reservation) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("RESERVATION UPDATE database connected");
            // Check if there exist a record on that id
            if (getReservationByStudentAndPlace(reservation.getStudentId(), reservation.getPlaceId()).isPresent()) {
                // Update both date and duration. Convert date String to java.sql.Date like in insertReservation
                String query = "UPDATE reservation SET date = ?, duration = ? WHERE student_id = ? AND place_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);

                // Convert String date to java.sql.Date
                LocalDate localDate = LocalDate.parse(reservation.getDate(), DateTimeFormatter.ISO_DATE);
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                statement.setDate(1, sqlDate);

                statement.setInt(2, reservation.getDuration());
                statement.setInt(3, reservation.getStudentId());
                statement.setInt(4, reservation.getPlaceId());
                System.out.println("UPDATE query: StudentId=" + reservation.getStudentId() + ", PlaceId=" + reservation.getPlaceId() + ", Date=" + reservation.getDate() + ", Duration=" + reservation.getDuration());
                result = statement.executeUpdate();
                System.out.println("UPDATE is done " + result + ")");
            } else {
                System.out.println("UPDATE - Reservation not found in database!");
                throw new RuntimeException("Reservation not found!");
            }
        } catch (SQLException e) {
            System.err.println("RESERVATION UPDATE ERROR:");
            e.printStackTrace();

            // Check for foreign key constraint violation
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                if (e.getMessage().contains("student_id")) {
                    throw new RuntimeException("Student ID " + reservation.getStudentId() + " not found in database!");
                } else if (e.getMessage().contains("place_id")) {
                    throw new RuntimeException("Place ID " + reservation.getPlaceId() + " not found in database!");
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("RESERVATION UPDATE ERROR (Non-SQL):");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }
}


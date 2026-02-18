package com.sau.lpm.lpm_p1.db;

import com.sau.lpm.lpm_p1.dto.Student;

import java.sql.*;
import java.util.Optional;

public class StudentCrudOperation {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "silyaz";

    // Get a student by id
    public Optional<Student> getStudentById(int id) {
        Student student = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Database connected");
            String query = "SELECT * FROM student WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            System.out.println(" id=" + id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setDepartment(resultSet.getString("address"));
                System.out.println("student found " + student.getName());
            }
            if (student == null) {
                System.out.println("ID=" + id + "cannot be found");
            }
        } catch (SQLException throwables) {
            System.err.println("db error:");
            throwables.printStackTrace();
        }
        if (student != null)
            return Optional.of(student);
        else
            return Optional.empty();
    }

    // Insert a student by id
    public int insertStudentById(Student student) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("INSERT - Database connected");
            // Check if there exist a record on that id
            if (getStudentById(student.getId()).isPresent()) {
                System.out.println("INSERT - student already exist (ID=" + student.getId() + ")");
                result = -1;
            } else {
                String query = "INSERT INTO student (id, name, address) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, student.getId());
                statement.setString(2, student.getName());
                statement.setString(3, student.getDepartment());
                System.out.println("INSERT query: ID=" + student.getId() + ", Name=" + student.getName() + ", Dept=" + student.getDepartment());
                result = statement.executeUpdate();
                System.out.println("INSERT is done: " + result + ")");
            }
        } catch (Exception e) {
            System.err.println("INSERT ERROR:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Delete a student by id
    public int deleteStudentById(int id) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM student WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            // PostgreSQL foreign key violation SQL state: 23503
            String sqlState = e.getSQLState();
            if ("23503".equals(sqlState)) {
                System.err.println("DELETE - Foreign key violation: student is referenced by other records (ID=" + id + ")");
                // Use a special return code to indicate FK constraint violation
                return -2;
            }
            System.err.println("DELETE ERROR:");
            e.printStackTrace();
            // Use -1 to indicate a generic error
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Update a student by id
    public int updateStudentById(Student student) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("UPDATE - Database connected");
            // Check if there exist a record on that id
            if (getStudentById(student.getId()).isPresent()) {
                String query = "UPDATE student SET name = ?, address = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, student.getName());
                statement.setString(2, student.getDepartment());
                statement.setInt(3, student.getId());
                System.out.println("UPDATE query: ID=" + student.getId() + ", Name=" + student.getName() + ", Dept=" + student.getDepartment());
                result = statement.executeUpdate();
                System.out.println("UPDATE is done! : " + result + ")");
            } else {
                System.out.println("UPDATE - student cannot found (ID=" + student.getId() + ")");
            }
        } catch (Exception e) {
            System.err.println("UPDATE ERROR:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    // Count reservations that reference a given student
    public int countReservationsByStudentId(int studentId) {
        int count = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT COUNT(*) FROM reservation WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("COUNT reservations error:");
            e.printStackTrace();
        }
        return count;
    }

    // Delete reservations that reference a given student
    public int deleteReservationsByStudentId(int studentId) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM reservation WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            result = statement.executeUpdate();
            System.out.println("Deleted " + result + " reservation(s) for student ID=" + studentId);
        } catch (SQLException e) {
            System.err.println("DELETE reservations error:");
            e.printStackTrace();
        }
        return result;
    }
}


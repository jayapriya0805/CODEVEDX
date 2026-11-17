package com.studentmanagement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class StudentManagement {
    private static final Scanner SC = new Scanner(System.in);
    private static String url;
    private static String username;
    private static String password;

    public static void main(String[] args) {
        if (!loadConfig()) {
            return;
        }

        System.out.println("==================================");
        System.out.println("     STUDENT MANAGEMENT SYSTEM");
        System.out.println("==================================");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected successfully!");

            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("----------- MAIN MENU -----------");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Delete Student");
                System.out.println("4. Exit");
                System.out.println("---------------------------------");

                int choice = readInt("Enter your choice: ");
                switch (choice) {
                    case 1:
                        addStudent(connection);
                        break;
                    case 2:
                        viewStudents(connection);
                        break;
                    case 3:
                        deleteStudent(connection);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Application closed. Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 to 4.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Check MySQL Server status and config.properties.");
        }
    }

    private static boolean loadConfig() {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            p.load(in);
            url = p.getProperty("db.url");
            username = p.getProperty("db.username");
            password = p.getProperty("db.password");

            if (url == null || username == null || password == null
                    || password.trim().isEmpty() || password.trim().equals("CHANGE_ME")) {
                System.out.println("Please update config.properties with your MySQL password.");
                return false;
            }
            return true;
        } catch (IOException e) {
            System.out.println("Could not find config.properties. Run the app from its project folder.");
            return false;
        }
    }

    private static void addStudent(Connection connection) {
        System.out.println("\n--- Add Student ---");
        String name = readText("Student name: ");
        String email = readText("Email: ");
        String department = readText("Department: ");
        int age = readInt("Age: ");

        if (age < 1 || age > 120) {
            System.out.println("Enter an age between 1 and 120.");
            return;
        }

        String sql = "INSERT INTO students (name, email, department, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, department);
            ps.setInt(4, age);
            ps.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("That email is already registered. Use a different email.");
        } catch (SQLException e) {
            System.out.println("Could not add student: " + e.getMessage());
        }
    }

    private static void viewStudents(Connection connection) {
        System.out.println("\n--- All Student Records ---");
        String sql = "SELECT id, name, email, department, age FROM students ORDER BY id";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            boolean found = false;
            System.out.printf("%-5s %-22s %-30s %-18s %-5s%n",
                    "ID", "NAME", "EMAIL", "DEPARTMENT", "AGE");
            System.out.println("--------------------------------------------------------------------------------");
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-22s %-30s %-18s %-5d%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getInt("age"));
            }
            if (!found) {
                System.out.println("No student records found.");
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve students: " + e.getMessage());
        }
    }

    private static void deleteStudent(Connection connection) {
        System.out.println("\n--- Delete Student ---");
        int id = readInt("Enter the student ID to delete: ");
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Could not delete student: " + e.getMessage());
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SC.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SC.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}

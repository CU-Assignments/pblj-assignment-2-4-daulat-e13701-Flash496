import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManagementApp {

    static final String URL = "jdbc:mysql://localhost:3306/myDatabase";
    static final String USER = "flash";
    static final String PASSWORD = "admin";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Create Student");
            System.out.println("2. Read Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter StudentID: ");
                        int studentID = scanner.nextInt();
                        System.out.print("Enter Name: ");
                        String name = scanner.next();
                        System.out.print("Enter Department: ");
                        String department = scanner.next();
                        System.out.print("Enter Marks: ");
                        double marks = scanner.nextDouble();
                        createStudent(new Student(studentID, name, department, marks));
                        System.out.println("Student created.");
                    }
                    case 2 -> {
                        System.out.println("Student List:");
                        for (Student student : readStudents()) {
                            System.out.println("StudentID: " + student.getStudentID() + ", Name: " + student.getName() +
                                    ", Department: " + student.getDepartment() + ", Marks: " + student.getMarks());
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter StudentID: ");
                        int studentID = scanner.nextInt();
                        System.out.print("Enter new Marks: ");
                        double newMarks = scanner.nextDouble();
                        updateStudent(studentID, newMarks);
                        System.out.println("Student updated.");
                    }
                    case 4 -> {
                        System.out.print("Enter StudentID: ");
                        int studentID = scanner.nextInt();
                        deleteStudent(studentID);
                        System.out.println("Student deleted.");
                    }
                    case 5 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void createStudent(Student student) throws SQLException {
        String query = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, student.getStudentID());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getDepartment());
            preparedStatement.setDouble(4, student.getMarks());
            preparedStatement.executeUpdate();
        }
    }

    static List<Student> readStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Student";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("StudentID"), resultSet.getString("Name"),
                        resultSet.getString("Department"), resultSet.getDouble("Marks")));
            }
        }
        return students;
    }

    static void updateStudent(int studentID, double newMarks) throws SQLException {
        String query = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newMarks);
            preparedStatement.setInt(2, studentID);
            preparedStatement.executeUpdate();
        }
    }

    static void deleteStudent(int studentID) throws SQLException {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentID);
            preparedStatement.executeUpdate();
        }
    }
}

class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getMarks() {
        return marks;
    }
}

import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/MyDatabase";
    static final String USER = "flash";
    static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nProduct Management System");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> createProduct(connection, scanner);
                    case 2 -> readProducts(connection);
                    case 3 -> updateProduct(connection, scanner);
                    case 4 -> deleteProduct(connection, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();
            System.out.println("Product created successfully!");
        }
    }

    static void readProducts(Connection connection) throws SQLException {
        String query = "SELECT * FROM Product";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("ProductID: " + resultSet.getInt("ProductID") +
                        ", Name: " + resultSet.getString("ProductName") +
                        ", Price: " + resultSet.getDouble("Price") +
                        ", Quantity: " + resultSet.getInt("Quantity"));
            }
        }
    }

    static void updateProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID to update: ");
        int productId = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double newPrice = scanner.nextDouble();

        String query = "UPDATE Product SET Price = ? WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Product updated!" : "Product not found.");
        }
    }

    static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID to delete: ");
        int productId = scanner.nextInt();

        String query = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Product deleted!" : "Product not found.");
        }
    }
}

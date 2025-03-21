import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FetchEmployeeData {
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/myDatabase";
        String user = "flash";
        String password = "admin";

        try {
         
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");

            
            Statement statement = connection.createStatement();
            String query = "SELECT EmpID, Name, Salary FROM Employee";

            
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Employee Data:");
            while (resultSet.next()) {
                System.out.println("EmpID: " + resultSet.getInt("EmpID") +
                        ", Name: " + resultSet.getString("Name") +
                        ", Salary: " + resultSet.getDouble("Salary"));
            }

            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package salesSystem;
import java.sql.*;
import java.util.Scanner;

public class Manager {
    
    public void listSalesperson(Connection con){
        
        // choose increasing or decreasing order
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose ordering: ");
        int choice = sc.nextInt();
        
        if (choice != 0 || choice != 1) return;
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs;
            
            String decreasing = (choice == 1) ? ";" : " DESC;";
            
            // get query result
            rs = stmt.executeQuery(
                    "SELECT [Salesperson ID], [Salesperson Name], [Salesperson Phone Number], [Salesperson Experience] "
                    + "FROM Salesperson "
                    + "ORDER BY [Salesperson Experience] "
                    + decreasing
            );
                    
            // display query result
            System.out.println("| ID | Name | Mobile Phone | Years of Experience |");
            while(rs.next()){
                System.out.println(
                    "| "
                    + rs.getString(1)
                    + " | "
                    + rs.getString(2)
                    + " | "
                    + rs.getString(3)
                    + " | "
                    + rs.getString(4)
                    + " | ");
                }                          
            
            stmt.close();
            
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }
    
    public void countTransaction(Connection con){
        
        // user input the range of years of experience
        Scanner sc = new Scanner(System.in);
        System.out.print("Type in the lower bound for years of experience: ");
        String lowerBound = sc.next();
        System.out.print("Type in the upper bound for years of experience: ");
        String upperBound = sc.next();
        
        try{
            Statement stmt = con.createStatement();
            
            // get query result
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT S.[Salesperson ID], S.[Salesperson Name], S.[Salesperson Experience], COUNT(T.[Transaction ID]) "
                    + "FROM "
                        + "(SELECT [Salesperson ID], [Salesperson Name], [Salesperson Experience] "
                        + "FROM Salesperson "
                        + "WHERE [Salesperson Experience] >= " + lowerBound + " AND [Salesperson Experience] <= " + upperBound
                        + ") S " +
                    "INNER JOIN Transaction T ON T.[Salesperson ID] = S.[Salesperson ID] " +
                    "GROUP BY S.[Salesperson ID], S.[Salesperson Name], S.[Salesperson Experience];");

            // display query result
            System.out.println("| ID | Name | Years of Experience | Number of Transaction |");
            while(rs.next()){
                System.out.println(
                    "| "
                    + rs.getString(1)
                    + " | "
                    + rs.getString(2)
                    + " | "
                    + rs.getString(3)
                    + " | "
                    + rs.getString(4)
                    + " | ");
            }
            
            System.out.println("End of Query");
            
            stmt.close();
        
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }
    
}

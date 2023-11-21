package salesSystem;
import java.sql.*;
import java.util.Scanner;

public class Manager {
    
    /* to be used in query statment (easy to update the field name) */
    
    // field names in Manufacturer
    private String manufacturerID =             "mID";
    private String manufacturerName =           "mName";
    private String manufacturerAddress =        "mAddress";
    private String manufacturerPhoneNumber =    "mPhoneNo";
    
    // field names in Parts
    private String partID =                     "pID";
    private String partName =                   "pName";
    private String partPrice =                  "pPrice";
    private String partWarranty =               "pWarranty";
    private String partAvailableQuantity =      "pAvailableQuantity";
    
    // field names in Salesperson
    private String salespersonID =              "sID";
    private String salespersonName =            "sName";
    private String salespersonAddress =         "sAddress";
    private String salespersonPhoneNumber =     "sPhoneNumber";
    private String salespersonExperience =      "sExperience";
    
    // field names in Transaction
    private String transactionID = "tID";
    
    public void listSalesperson(Connection con){
        
        try{
            
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // choose increasing or decreasing order
            Scanner sc = new Scanner(System.in);
            System.out.print("Choose ordering: ");
            int choice = sc.nextInt();
            if (choice != 0 && choice != 1) return;
            String decreasing = (choice == 2) ? " DESC;" : " ;";
            
            // get query result (OLD)
            /* 
            rs = stmt.executeQuery(
                    "SELECT [Salesperson ID], [Salesperson Name], [Salesperson Phone Number], [Salesperson Experience] "
                    + "FROM salesperson "
                    + "ORDER BY [Salesperson Experience] "
                    + decreasing
            );
            */
            
            // get query result (UPDATED)
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT " + salespersonID + "," + salespersonName + "," + salespersonPhoneNumber + "," + salespersonExperience + " " +
                    "FROM salesperson " +
                    "ORDER BY " + salespersonExperience +
                    decreasing
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
            
            // testing
            System.out.println("Hello World (end) (testing)");
            
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }
    
    public void countTransaction(Connection con){
              
        try{
            
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // user input the range of years of experience
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the lower bound for years of experience: ");
            String lowerBound = sc.next();
            System.out.print("Type in the upper bound for years of experience: ");
            String upperBound = sc.next();
            
            // get query result (OLD)
            /*
            rs = stmt.executeQuery(
                    "SELECT S.[Salesperson ID], S.[Salesperson Name], S.[Salesperson Experience], COUNT(T.[Transaction ID]) "
                    + "FROM "
                        + "(SELECT [Salesperson ID], [Salesperson Name], [Salesperson Experience] "
                        + "FROM Salesperson "
                        + "WHERE [Salesperson Experience] >= " + lowerBound + " AND [Salesperson Experience] <= " + upperBound
                        + ") S " +
                    "INNER JOIN Transaction T ON T.[Salesperson ID] = S.[Salesperson ID] " +
                    "GROUP BY S.[Salesperson ID], S.[Salesperson Name], S.[Salesperson Experience];");
            */
            
            // get query result (UPDATED)
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT S." + salespersonID + ", S." + salespersonName + ", S." + salespersonExperience + ", COUNT(T." + transactionID + ") " +
                    "FROM " +
                        "(SELECT " + salespersonID + ", " + salespersonName + ", " + salespersonExperience + " " +
                        "FROM Salesperson " +
                        "WHERE " + salespersonExperience + " >= " + lowerBound + " AND " + salespersonExperience + " <= " + upperBound +
                        ") S " +
                    "INNER JOIN Transaction T ON T." + salespersonID + " = S." + salespersonID + " " +
                    "GROUP BY S." + salespersonID + ", S." + salespersonName + ", S." + salespersonExperience + ";");
            
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
            
            // testing
            System.out.println("Hello World (end) (testing)");
        
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }
    
    public void listManufacturerOrderBySales(Connection con){
        
        try{
            
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // get query result
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SSELECT M." + manufacturerID + ", M." + manufacturerName + ",  SUM(P." + partPrice + ") " +
                    "FROM Part AS P, Transaction AS T, Manufacturer AS M " +
                    "WHERE P." + partID + " = T." + partID + " AND P." + manufacturerID + " = M." + manufacturerID + " " +
                    "GROUP BY M." + manufacturerID + ", M." + manufacturerName + " " +
                    "ORDER BY SUM(P." + partPrice + ") DESC;"
            );

            // display query result
            System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
            while (rs.next()) {
                System.out.println(
                        "| "
                        + rs.getString(1)
                        + " | "
                        + rs.getString(2)
                        + " | "
                        + rs.getString(3)
                        + " | ");
            }

            stmt.close();
            
            // testing
            System.out.println("Hello World (end) (testing)");
            
        }catch(Exception e){
            System.out.println("Error occured: " + e);
        }
    }
    
    public void listNPopular(Connection con){
        
        try{
        
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // user input the range of years of experience
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the number of parts: ");
            String input = sc.next();
            
            

            // get query result (UPDATED)
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT TOP " + input + " T." + partID + ", P." + partName + ", COUNT(" + transactionID + ") " +
                    "FROM Transaction AS T, Part AS P " +
                    "WHERE T." + partID + " = P." + partID + " " +
                    "GROUP BY T." + partID + ", P." + partName + " " +
                    "ORDER BY COUNT(" + transactionID + ") DESC"
            );

            // display query result
            System.out.println("| Part ID | Part Name | No. of Transaction |");
            while (rs.next()) {
                System.out.println(
                        "| "
                        + rs.getString(1)
                        + " | "
                        + rs.getString(2)
                        + " | "
                        + rs.getString(3)
                        + " | ");
            }
            
            System.out.println("End of Query");
            
            stmt.close();
            
            // testing
            System.out.println("Hello World (end) (testing)");
            
        }catch(Exception e){
            System.out.println("Error occured: " + e);
        }
    }
    
}

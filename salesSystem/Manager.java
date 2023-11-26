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
    private String partWarranty =               "pWarrantyPeriod";
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
            
            // choose increasing or decreasing order
            Scanner sc = new Scanner(System.in);
            System.out.println("Choose ordering: ");
            System.out.println("1. By ascending order");
            System.out.println("2. By descending order");
            System.out.print("Choose the list ordering: ");
            int choice;
            try {
                choice = sc.nextInt();
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid choice!");
                return;
            }
            if (choice != 1 && choice != 2){
                System.out.println("Invalid choice!");
                return;
            }
            String decreasing = (choice == 2) ? " DESC;" : " ;";
            
            // get query result
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
            System.out.println("End of Query");
        }catch (Exception e){
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
        
    }
    
    public void countTransaction(Connection con){
              
        try{
            
            // user input the range of years of experience
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the lower bound for years of experience: ");
            int lowerBound;
            try {
                lowerBound = sc.nextInt();
                if (lowerBound<0){
                    throw new Exception();
                }
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid lower bound!");
                return;
            }
            System.out.print("Type in the upper bound for years of experience: ");
            int upperBound;
            try {
                upperBound = sc.nextInt();
                if (upperBound<lowerBound||upperBound<0){
                    throw new Exception();
                }
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid upper bound!");
                return;
            }
            
            // get query result
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT S." + salespersonID + ", S." + salespersonName + ", S." + salespersonExperience + ", COUNT(T." + transactionID + ") " +
                    "FROM " +
                        "(SELECT " + salespersonID + ", " + salespersonName + ", " + salespersonExperience + " " +
                        "FROM salesperson " +
                        "WHERE " + salespersonExperience + " >= " + lowerBound + " AND " + salespersonExperience + " <= " + upperBound +
                        ") S " +
                    "INNER JOIN transaction T ON T." + salespersonID + " = S." + salespersonID + " " +
                    "GROUP BY S." + salespersonID + ", S." + salespersonName + ", S." + salespersonExperience + " " +
                    "ORDER BY S." + salespersonID + " DESC;"
                    );
            
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
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
        
    }
    
    public void listManufacturerOrderBySales(Connection con){
        
        try{
            
            // get query result
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT M." + manufacturerID + ", M." + manufacturerName + ",  SUM(P." + partPrice + ") " +
                    "FROM part AS P, transaction AS T, manufacturer AS M " +
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
            System.out.println("End of Query");
        }catch(Exception e){
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
    }
    
    public void listNPopular(Connection con){
        
        try{
        
            // user input the range of years of experience
            Scanner sc = new Scanner(System.in);
            System.out.print("Type in the number of parts: ");
            int input;
            try {
                input = sc.nextInt();
                if (input<1){
                    throw new Exception();
                }
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid number!");
                return;
            }
            
            
            // get query result
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                    "SELECT " + "T." + partID + ", P." + partName + ", COUNT(" + transactionID + ") " +
                    "FROM transaction AS T, part AS P " +
                    "WHERE T." + partID + " = P." + partID + " " +
                    "GROUP BY T." + partID + ", P." + partName + " " +
                    "ORDER BY COUNT(" + transactionID + ") DESC " +
                    "LIMIT " + input
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
            stmt.close();
            System.out.println("End of Query");
        }catch(Exception e){
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
    }
    
}

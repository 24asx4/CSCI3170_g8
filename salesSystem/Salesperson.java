package salesSystem;
import java.sql.*;
import java.util.Scanner;

public class Salesperson {
    
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
    // field names in category
    private String categoryID =                 "cID";
    private String categoryName =               "cName";
    
    // field names in Transaction
    private String transactionID = "tID";
    private String transactionDate = "tDate";
    
    public void searchPart(Connection con){
        
        try{
            
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // choose increasing or decreasing order
            Scanner sc = new Scanner(System.in);
            System.out.println("Choose the Search criterion: ");
            System.out.println("1. Part Name");
            System.out.println("2. Manufacturer Name");
            System.out.print("Choose the Search criterion: ");
            int choice = sc.nextInt();
            if (choice != 1 && choice != 2) return;
            String searchField = (choice == 1) ? "P.pName" : "M.mName";
            System.out.print("Type in the Search Keyword: ");
            sc.nextLine();
            String searchValue = sc.nextLine();
            System.out.println("Choose order: ");
            System.out.println("1. By price, ascending order");
            System.out.println("2. By price, descending order");
            System.out.print("Choose the Search criterion: ");
            choice = sc.nextInt();
            if (choice != 1 && choice != 2) return;
            String order = (choice == 2) ? "DESC" : "";
            // get query result (UPDATED)
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                
                String.format("SELECT P.%s,P.%s,M.%s,C.%s,P.%s,P.%s,P.%s ",
                partID, partName, manufacturerName, categoryName, partAvailableQuantity, partWarranty, partPrice) +
                String.format("FROM ((part P INNER JOIN category C ON P.%s=C.%s) INNER JOIN manufacturer M ON P.%s=M.%s)",
                categoryID, categoryID, manufacturerID, manufacturerID) +
                String.format("WHERE %s LIKE '%%%s%%'", searchField, searchValue) +
                String.format("ORDER BY P.%s %s ;", partPrice, order)
            );
            
            // display query result
            System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price | ");
            while(rs.next()){
                System.out.print("| ");
                for (int i=1;i<=7;i++){
                    System.out.print(rs.getString(i));
                    System.out.print(" | ");
                }
                System.out.println("");
            }                          
            
            stmt.close();
            
            System.out.println("End of Query");
            // testing
            System.out.println("Hello World (end) (testing)");
            
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }

    public void sellPart(Connection con){
        
        try{
            
            // testing
            System.out.println("Hello World (beginning) (testing)");
            
            // choose increasing or decreasing order
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter The Part ID: ");
            int pIDChoice = sc.nextInt();
            System.out.print("Enter The Salesperson ID: ");
            int sIDChoice = sc.nextInt();
            // get query result (UPDATED)
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(
                String.format("SELECT %s FROM part WHERE %s=%d;",
                partAvailableQuantity,partID,pIDChoice)
            );
            
            // display query result
            int number_available=-1;

            while(rs.next()){
                number_available=Integer.parseInt(rs.getString(1));
            }
            
            if (number_available<=0){
                System.out.println("The part is out of stock!");
                return;
            } 
                                     
            stmt.close();
            int lastID=-1;
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    String.format("SELECT %s FROM transaction ORDER BY %s DESC LIMIT %d;"
                    ,transactionID,transactionID,1)
            );
            while(rs.next()){
                lastID=Integer.parseInt(rs.getString(1));
            }
            stmt.close();
            stmt = con.createStatement();
            stmt.execute(
                String.format("UPDATE part SET %s=%d WHERE %s=%d;"
                ,partAvailableQuantity,number_available-1,partID,pIDChoice)
            );
            stmt.close();
            stmt = con.createStatement();
            stmt.execute(
                String.format(
                    "INSERT INTO transaction (%s,%s,%s,%s) VALUE(%d,%d,%d,DATE_FORMAT(CURDATE(),'%%d/%%m/%%Y'));"
                ,transactionID,partID,salespersonID,transactionDate,lastID+1,pIDChoice,sIDChoice)
            );
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    String.format("SELECT %s,%s,%s FROM part WHERE %s=%d;"
                    ,partName,partID,partAvailableQuantity,partID,pIDChoice)
            );
            while(rs.next()){
                System.out.printf("Product: %s(id: %s) Remaining Quality: %s \n",
                rs.getString(1),rs.getString(2),rs.getString(3));
            }
            System.out.println("End of Query");
            // testing
            System.out.println("Hello World (end) (testing)");
            
        }catch (Exception e){
            System.out.println("Error occured: " + e);
        }
        
    }
    
}
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
    private String transactionID =              "tID";
    private String transactionDate =            "tDate";
    
    public void searchPart(Connection con){
        
        try{
            
            
            /* choose search for part or manufacturer */
            Scanner sc = new Scanner(System.in);
            System.out.println("Choose the Search criterion: ");
            System.out.println("1. Part Name");
            System.out.println("2. Manufacturer Name");
            System.out.print("Choose the Search criterion: ");
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
            String searchField = (choice == 1) ? "P.pName" : "M.mName";
            /* input the name for searching */
            System.out.print("Type in the Search Keyword: ");
            sc.nextLine();
            String searchValue = sc.nextLine();
            /* choose increasing or decreasing order by price */
            System.out.println("Choose order: ");
            System.out.println("1. By price, ascending order");
            System.out.println("2. By price, descending order");
            System.out.print("Choose the Search criterion: ");
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
            String order = (choice == 2) ? "DESC" : "";
            /* get query result */
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
            
        }catch (Exception e){
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
        
    }

    public void sellPart(Connection con){
        
        try{

            /* input part id for selling */
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter The Part ID: ");
            int pIDChoice;
            try {
                pIDChoice = sc.nextInt();
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid Part ID!");
                return;
            }
            /* input salesperson id of the salesperson who are going to sell the part */
            System.out.print("Enter The Salesperson ID: ");
            int sIDChoice;
            try {
                sIDChoice = sc.nextInt();
            } catch (Exception e){
                sc.nextLine();
                System.out.println("Invalid Salesperson ID!");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs;
            /* check if the salesperson exist */
            rs = stmt.executeQuery(
                String.format("SELECT %s FROM salesperson WHERE %s=%d;",
                salespersonID,salespersonID,sIDChoice)
            );
            if (!rs.next()){
                System.out.println("The salesperson does not exist!");
                return;
            }
            stmt.close();

            /* get the quanlity */
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                String.format("SELECT %s FROM part WHERE %s=%d;",
                partAvailableQuantity,partID,pIDChoice)
            );
            
            int number_available=-999;

            while(rs.next()){
                number_available=Integer.parseInt(rs.getString(1));
            }
            /* check if the part exist */
            if (number_available==-999){
                System.out.println("The part does not exist!");
                return;
            }
            /* check if there is enough part to sell */
            if (number_available<=0){
                System.out.println("The part is out of stock!");
                return;
            }                    
            stmt.close();

            /* get the last tID of the table transaction */
            int lastID=1;
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    String.format("SELECT %s FROM transaction ORDER BY %s DESC LIMIT %d;"
                    ,transactionID,transactionID,1)
            );
            while(rs.next()){
                lastID=Integer.parseInt(rs.getString(1));
            }
            stmt.close();
            /* reduce the part quantity by 1 in part */
            stmt = con.createStatement();
            stmt.execute(
                String.format("UPDATE part SET %s=%d WHERE %s=%d;"
                ,partAvailableQuantity,number_available-1,partID,pIDChoice)
            );
            stmt.close();
            /* insert a new transaction record */
            stmt = con.createStatement();
            stmt.execute(
                String.format(
                    "INSERT INTO transaction (%s,%s,%s,%s) VALUE(%d,%d,%d,DATE_FORMAT(CURDATE(),'%%d/%%m/%%Y'));"
                ,transactionID,partID,salespersonID,transactionDate,lastID+1,pIDChoice,sIDChoice)
            );
            /* get the required information */
            stmt.close();
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    String.format("SELECT %s,%s,%s FROM part WHERE %s=%d;"
                    ,partName,partID,partAvailableQuantity,partID,pIDChoice)
            );
            /* print the confirm message (including product id, name, quantity) */
            while(rs.next()){
                System.out.printf("Product: %s(id: %s) Remaining Quantity: %s \n",
                rs.getString(1),rs.getString(2),rs.getString(3));
            }
            System.out.println("End of Query");
            
        }catch (Exception e){
            System.out.println("The table(s) have not been set up properly yet! Please create the tables using the operation 1 of the Administrator.");
            //System.out.println("Error occured: " + e);
        }
        
    }
    
}
package project;
import java.sql.*;
import java.util.Scanner;

public class Manager {
    
    public void listSalesperson(Connection con){
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose ordering: ");
        int choice = sc.nextInt();
        
        try{
        
            Statement stmt = con.createStatement();
            ResultSet rs;
            switch(choice){
                case 1:
                    rs = stmt.executeQuery("SELECT ID, Name,  FROM Salesperson ORDER BY [Salesperson Experience];");
                    break;
                case 2:
                    rs = stmt.executeQuery("SELECT * FROM Salesperson ORDER BY [Salesperson Experience] DESC;");
                    break;
                default:
                    break;
            }
        
        }catch (Exception e){
        }
        
    }
    
}

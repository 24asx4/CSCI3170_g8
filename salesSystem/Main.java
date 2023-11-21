package salesSystem;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;



public class Main {
    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db8?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group8";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToMySQL(){
            Connection con = null;
            try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            } catch (ClassNotFoundException e){
                    System.out.println("[Error]: Java MySQL DB Driver not found!!");
                    System.exit(0);
            } catch (SQLException e){
                    System.out.println(e);
            }
            return con;
    }


    public static void main(String[] args){
        Connection con = connectToMySQL();
        int choice=0;
        Scanner input=new Scanner(System.in);
        System.out.println("Welcome to sales system!");
        while (choice!=4) {
            Menu menu=new Menu();
            menu.mainMenu();
            choice=input.nextInt();
            switch (choice) {
                case 1:
                    menu.adminMenu(con);
                    break;
                case 2:
                    menu.salesPersonMenu(con);
                    break;
                case 3:
                    menu.managerMenu(con);
                    break;
                case 4:
                    ;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }    
}

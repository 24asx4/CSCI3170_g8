package salesSystem;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Menu {
    private int choice;

    public Menu() {
        choice = 0;
    }

    public void mainMenu() {
        System.out.println();
        System.out.println("-----Main Menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Operations for administrator");
        System.out.println("2. Operations for salesperson");
        System.out.println("3. Operations for manager");
        System.out.println("4. Exit this program");
        System.out.print("Enter Your Choice: ");
    }

    public void salesPersonMenu(Connection con) {
        while (this.choice == 0) {
            System.out.println();
            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for parts");
            System.out.println("2. Sell a part");
            System.out.println("3. Return to main menu");
            System.out.print("Enter Your Choice: ");
            Scanner input = new Scanner(System.in);
            try{
                this.choice = input.nextInt();
            } catch (Exception e){
                this.choice=0;
                input.nextLine();
            }
            Salesperson salesperson = new Salesperson();
            switch (this.choice) {
                case 1:
                    salesperson.searchPart(con);
                    break;
                case 2:
                    salesperson.sellPart(con);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    this.choice = 0;
                    break;
            }
        }
    }

    public void managerMenu(Connection con) {
        while (this.choice == 0) {
            System.out.println();
            System.out.println("-----Operations for manager menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println(
                    "2. Count the no. of record of each salesperson under a specific range on years of experience");
            System.out.println("3. Show the total sale value of each manufacturer");
            System.out.println("4. Show the N most popular part");
            System.out.println("5. Return to main menu");
            System.out.print("Enter Your Choice: ");
            Scanner input = new Scanner(System.in);
            try{
                this.choice = input.nextInt();
            } catch (Exception e){
                this.choice=0;
                input.nextLine();
            }
            Manager manager = new Manager();
            switch (this.choice) {
                case 1:
                    manager.listSalesperson(con);
                    break;
                case 2:
                    manager.countTransaction(con);
                    break;
                case 3:
                    manager.listManufacturerOrderBySales(con);
                    break;
                case 4:
                    manager.listNPopular(con);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    this.choice = 0;
                    break;
            }
        }
    }

    public void adminMenu(Connection con) {
        while (this.choice == 0) {
            System.out.println();
            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show content of a table");
            System.out.println("5. Return to main menu");
            System.out.print("Enter Your Choice: ");
            Scanner input = new Scanner(System.in);
            try{
                this.choice = input.nextInt();
            } catch (Exception e){
                this.choice=0;
                input.nextLine();
            }
            Admin admin = new Admin();
            switch (this.choice) {
                case 1:
                    admin.createTable(con);
                    break;
                case 2:
                    admin.deleteTable(con);
                    break;
                case 3:
                    admin.loadDataFromFile(con);
                    break;
                case 4:
                    admin.showContent(con);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    this.choice = 0;
                    break;
            }
        }
    }
}

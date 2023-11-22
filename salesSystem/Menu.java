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
            this.choice = input.nextInt();
            switch (this.choice) {
                case 1:
                    ;
                    break;
                case 2:
                    ;
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
            this.choice = input.nextInt();
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
            this.choice = input.nextInt();
            switch (this.choice) {
                case 1:
                    try {
                        Statement stmt = con.createStatement();
                        String sql = "CREATE TABLE IF NOT EXISTS category (cID int NOT NULL, cName varchar(20) NOT NULL, PRIMARY KEY(cID), CHECK (cID>=1 AND cID<=9))";
                        stmt.executeUpdate(sql);
                        sql = "CREATE TABLE IF NOT EXISTS manufacturer (mID int NOT NULL, mName varchar(20) NOT NULL, mAddress varchar(50) NOT NULL, mPhoneNumber int NOT NULL, PRIMARY KEY(mID), CHECK (mID<=99 AND mPhoneNumber>=10000000 AND mPhoneNumber<=99999999))";
                        stmt.executeUpdate(sql);
                        sql = "CREATE TABLE IF NOT EXISTS part (pID int NOT NULL, pName varchar(20) NOT NULL, pPrice int NOT NULL, mID int NOT NULL, cID int NOT NULL, pWarranty int NOT NULL, pAvailableQuantity int NOT NULL, PRIMARY KEY(pID), FOREIGN KEY(mID) REFERENCES manufacturer(mID), FOREIGN KEY(cID) REFERENCES category(cID), CHECK (pID<=999 AND mPrice<=99999 AND pWarranty<=99 AND pAvailableQuantity<=99))";
                        stmt.executeUpdate(sql);
                        sql = "CREATE TABLE IF NOT EXISTS salesperson (sID int NOT NULL, sName varchar(20) NOT NULL, sAddress varchar(50) NOT NULL, sPhoneNumber int NOT NULL, sExperience int NOT NULL, PRIMARY KEY(sID), CHECK (sID<=99 AND sPhoneNumber>=10000000 AND sPhoneNumber<=99999999 AND sExperience>=1 AND sExperience<=9))";
                        stmt.executeUpdate(sql);
                        sql = "CREATE TABLE IF NOT EXISTS transaction (tID int NOT NULL, pID int NOT NULL, sID int NOT NULL, tDate varchar(10) NOT NULL, PRIMARY KEY(tID), FOREIGN KEY(pID) REFERENCES part(pID), FOREIGN KEY(sID) REFERENCES salesperson(sID), CHECK (tID<=9999))";
                        stmt.executeUpdate(sql);
                        System.out.println("Processing...Done! Database is initialized!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Statement stmt = con.createStatement();
                        String[] tables = { "transaction", "salesperson", "part", "manufacturer", "category" };
                        for (int i = 0; i < tables.length; i++) {
                            String sql = "DROP TABLE IF EXISTS " + tables[i];
                            stmt.executeUpdate(sql);
                        }
                        System.out.println("Processing...Done! Database is removed!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    ;
                    break;
                case 4:
                    ;
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

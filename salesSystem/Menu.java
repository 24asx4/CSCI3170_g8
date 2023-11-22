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
                        stmt.execute(sql);
                        sql = "CREATE TABLE IF NOT EXISTS manufacturer (mID int NOT NULL, mName varchar(20) NOT NULL, mAddress varchar(50) NOT NULL, mPhoneNumber int NOT NULL, PRIMARY KEY(mID), CHECK (mID>=1 AND mID<=99 AND mPhoneNumber>=10000000 AND mPhoneNumber<=99999999))";
                        stmt.execute(sql);
                        sql = "CREATE TABLE IF NOT EXISTS part (pID int NOT NULL, pName varchar(20) NOT NULL, pPrice int NOT NULL, mID int NOT NULL, cID int NOT NULL, pWarrantyPeriod int NOT NULL, pAvailableQuantity int NOT NULL, PRIMARY KEY(pID), FOREIGN KEY(mID) REFERENCES manufacturer(mID), FOREIGN KEY(cID) REFERENCES category(cID), CHECK (pID>=1 AND pID<=999 AND mPrice>=1 AND mPrice<=99999 AND pWarrantyPeriod>=1 AND pWarrantyPeriod<=99 AND pAvailableQuantity>=0 AND pAvailableQuantity<=99))";
                        stmt.execute(sql);
                        sql = "CREATE TABLE IF NOT EXISTS salesperson (sID int NOT NULL, sName varchar(20) NOT NULL, sAddress varchar(50) NOT NULL, sPhoneNumber int NOT NULL, sExperience int NOT NULL, PRIMARY KEY(sID), CHECK (sID>=1 AND sID<=99 AND sPhoneNumber>=10000000 AND sPhoneNumber<=99999999 AND sExperience>=1 AND sExperience<=9))";
                        stmt.execute(sql);
                        sql = "CREATE TABLE IF NOT EXISTS transaction (tID int NOT NULL, pID int NOT NULL, sID int NOT NULL, tDate varchar(10) NOT NULL, PRIMARY KEY(tID), FOREIGN KEY(pID) REFERENCES part(pID), FOREIGN KEY(sID) REFERENCES salesperson(sID), CHECK (tID>=1 AND tID<=9999))";
                        stmt.execute(sql);
                        System.out.println("Processing...Done! Database is initialized!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Statement stmt = con.createStatement();
                        String[] tables = { "transaction", "salesperson", "part", "manufacturer", "category" };
                        for (int i = 0; i <= 4; i++) {
                            String sql = "DROP TABLE IF EXISTS " + tables[i];
                            stmt.execute(sql);

                        }
                        System.out.println("Processing...Done! Database is removed!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println();
                    System.out.print("Type in the Source Data Folder Path: ");
                    String pathname = input.next();
                    try {
                        File f = new File(pathname + "/category.txt");
                        Scanner s = new Scanner(f);
                        while (s.hasNextLine()) {
                            String[] strings = s.nextLine().split("\t");
                            if (strings.length == 0)
                                break;
                            int cID = Integer.parseInt(strings[0]);
                            String cName = strings[1];
                            try {
                                Statement stmt = con.createStatement();
                                String sql = String.format("REPLACE INTO category VALUES (%d, '%s')", cID, cName);
                                stmt.execute(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        File f = new File(pathname + "/manufacturer.txt");
                        Scanner s = new Scanner(f);
                        while (s.hasNextLine()) {
                            String[] strings = s.nextLine().split("\t");
                            if (strings.length == 0)
                                break;
                            int mID = Integer.parseInt(strings[0]);
                            String mName = strings[1];
                            String mAddress = strings[2];
                            int mPhoneNumber = Integer.parseInt(strings[3]);
                            try {
                                Statement stmt = con.createStatement();
                                String sql = String.format("REPLACE INTO manufacturer VALUES (%d, '%s', '%s', %d)", mID,
                                        mName, mAddress, mPhoneNumber);
                                stmt.execute(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        File f = new File(pathname + "/part.txt");
                        Scanner s = new Scanner(f);
                        while (s.hasNextLine()) {
                            String[] strings = s.nextLine().split("\t");
                            if (strings.length == 0)
                                break;
                            int pID = Integer.parseInt(strings[0]);
                            String pName = strings[1];
                            int pPrice = Integer.parseInt(strings[2]);
                            int mID = Integer.parseInt(strings[3]);
                            int cID = Integer.parseInt(strings[4]);
                            int pWarrantyPeriod = Integer.parseInt(strings[5]);
                            int pAvailableQuantity = Integer.parseInt(strings[6]);
                            try {
                                Statement stmt = con.createStatement();
                                String sql = String.format("REPLACE INTO part VALUES (%d, '%s', %d, %d, %d, %d, %d)",
                                        pID, pName, pPrice, mID, cID, pWarrantyPeriod, pAvailableQuantity);
                                stmt.execute(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        File f = new File(pathname + "/salesperson.txt");
                        Scanner s = new Scanner(f).useDelimiter("\\n|\\t");
                        while (s.hasNextLine()) {
                            String[] strings = s.nextLine().split("\t");
                            if (strings.length == 0)
                                break;
                            int sID = Integer.parseInt(strings[0]);
                            String sName = strings[1];
                            String sAddress = strings[2];
                            int sPhoneNumber = Integer.parseInt(strings[3]);
                            int sExperience = Integer.parseInt(strings[4]);
                            try {
                                Statement stmt = con.createStatement();
                                String sql = String.format("REPLACE INTO salesperson VALUES (%d, '%s', '%s', %d, %d)",
                                        sID, sName, sAddress, sPhoneNumber, sExperience);
                                stmt.execute(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        File f = new File(pathname + "/transaction.txt");
                        Scanner s = new Scanner(f).useDelimiter("\\n|\\t");
                        while (s.hasNextLine()) {
                            String[] strings = s.nextLine().split("\t");
                            if (strings.length == 0)
                                break;
                            int tID = Integer.parseInt(strings[0]);
                            int pID = Integer.parseInt(strings[1]);
                            int sID = Integer.parseInt(strings[2]);
                            String tDate = strings[3];
                            try {
                                Statement stmt = con.createStatement();
                                String sql = String.format("REPLACE INTO transaction VALUES (%d, %d, %d, '%s')", tID,
                                        pID, sID, tDate);
                                stmt.execute(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Processing...Done! Data is inputted to the database!");
                    break;
                case 4:
                    System.out.print("Which table would you like to show: ");
                    String table = input.next();
                    System.out.println("Content of table " + table + ": ");
                    try {
                        Statement stmt = con.createStatement();
                        String sql = "SELECT * FROM " + table;
                        ResultSet rs = stmt.executeQuery(sql);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        System.out.print("|");
                        for (int i = 1; i <= rsmd.getColumnCount(); i++)
                            System.out.print(" " + rsmd.getColumnName(i) + " |");
                        System.out.print("\n");
                        while (rs.next()) {
                            System.out.print("|");
                            for (int i = 1; i <= rsmd.getColumnCount(); i++)
                                System.out.print(" " + rs.getString(i) + " |");
                            System.out.print("\n");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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

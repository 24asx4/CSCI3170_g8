# CSCI3170
Presented by Group 8

LAI Kai Hong (1155174609)  
CHENG Tsz Kin (1155175870)  
CHAN Ho Yeung (1155159600)

For Window,
Method of compilation: ./compile.bat
(javac ./salesSystem/Main.java)  
Method of execution: ./run.bat
(java -cp mysql-jdbc.jar;. salesSystem.Main)

For Linux,
Method of compilation: ./compile.sh
(javac ./salesSystem/Main.java)
Method of execution: ./run.sh
(java -cp mysql-jdbc.jar:. salesSystem.Main)

List of files (for the program part) (inside the folder "salesSystem"):
-Main.java
    -control flow of the program
-Menu.java
    -contain all the menu function (including the topmost menu, 3 sub-menus)
-Admin.java
    -contain the function to provide admin's operation
-Manager.java
    -contain the function to provide manager's operation
-Salesperson.java
    -contain the function to provide salesperson's operation

Sample data:
    -stored in "sample_data" folder
    -5 files (category.txt, manufacturer.txt, part.txt, salespserson.txt, transaction.txt)
    -folder name can be changed (the program will ask for the folder name)
    -files name of the 5 .txt is fixed, renaming them will cause error

JDBC driver:
-mysql-jdbc.jar
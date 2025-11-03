package bank.main;


import bank.db.DatabaseConn;
import bank.models.Accounts;
import bank.models.User;
import bank.services.AccountsManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    public static void main(String[] args) {
        Connection connection = DatabaseConn.getConnection();
        Scanner scanner = new Scanner(System.in);

        if (connection == null ){
            System.out.println("❌ Database connection failed!");
            return;
        }


        try {
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection,scanner);
            AccountsManager accountsManager = new AccountsManager(connection,scanner);

            String email;
            long accountNumber;

            while (true){
                System.out.println("*** WELCOME TO BANKING MANAGEMENT SYSTEM ***");
                System.out.println();
                System.out.println("1. Register ");
                System.out.println("2. Login ");
                System.out.println("3. Exit");
                System.out.print(" Enter you Choice : ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null){
                            System.out.println();
                            System.out.println(" User Logged In !!!");
                            if (!accounts.accountExists(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account ");
                                System.out.println("2. Exit ");
                                if (scanner.nextInt() == 1){
                                    accountNumber = accounts.openAccount(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is :"+accountNumber);
                                }else {
                                    break;
                                }
                            }
                            accountNumber = accounts.getAccountNumber(email);
                            int choice2 = 0;
                            if (choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println(" Enter Your Choice :");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountsManager.debitMoney(accountNumber);
                                        break;
                                    case 2:
                                        accountsManager.creditMoney(accountNumber);
                                        break;
                                    case 3:
                                        accountsManager.transferMoney(accountNumber);
                                        break;
                                    case 4:
                                        accountsManager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Please enter valid choice !!");
                                        break;
                                }
                            }
                        }else {
                            System.out.println("Incorrect Email or Password");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System...........");
                        return;
                    default:
                        System.out.println("Please Enter valid choice !!");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

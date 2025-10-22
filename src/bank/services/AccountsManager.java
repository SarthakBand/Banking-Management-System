package bank.services;



import java.sql.*;
import java.util.Scanner;

public class AccountsManager {
    private Connection connection;
    private Scanner scanner;

    public AccountsManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //Credit Money
    public void creditMoney(long accountNumber) throws SQLException{
        scanner.nextLine();
        System.out.print(" Enter Amount to be credited : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter Security Pin : ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accountNumber != 0){
                String query = "SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1,amount);
                preparedStatement.setString(2,securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    String creditQuery = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(creditQuery);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,accountNumber);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0){
                        System.out.println("Rs. "+amount+" credited to you bank account !!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else {
                        System.out.println(" Transaction Failed !!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else {
                    System.out.println("Invalid Security Pin !");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    //Debit Money
    public void debitMoney(long accountNumber) throws  SQLException{
        scanner.nextLine();
        System.out.print(" Enter Amount to Debit :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter Security Pin !");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (accountNumber != 0){
                String query = "SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,accountNumber);
                preparedStatement.setString(2,securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    double currentBalance = resultSet.getDouble("balance");
                    if (amount <= currentBalance){
                        String debitQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debitQuery);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement.setLong(2,accountNumber);
                        int rowsAffected = preparedStatement1.executeUpdate();

                        if (rowsAffected > 0){
                            System.out.println("Rs. "+amount+" debited from your account !");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction Failed !");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println(" Insufficient Balance ");
                    }
                }else {
                    System.out.println(" Invalid Pin !");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    // Transfer money from one account to another account
    public void transferMoney (long senderAccountMoney) throws SQLException {
        scanner.nextLine();
        System.out.print(" Enter Receiver Account number : ");
        long receiverAccountNumber = scanner.nextLong();
        System.out.print("Enter the amount to be Transferred :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter Security Pin :");
        String securityPin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if (senderAccountMoney != 0 && receiverAccountNumber != 0){
                String query = "SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,senderAccountMoney);
                preparedStatement.setString(2,securityPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    double currentBalance = resultSet.getDouble("balance");
                    if (amount <= currentBalance){
                        //Write queries for Credit and Debit
                        String debitQuery = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String creditQuery = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // debit prepared Statements
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
                        // Credit prepared Statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);

                        //Set values for debit prepared Statements
                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,senderAccountMoney);

                        //Set Values for credit prepared statements
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,receiverAccountNumber);

                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();

                        if (rowsAffected1 > 0 && rowsAffected2 > 0){
                            System.out.println("Transaction Successful !!!");
                            System.out.println("Rs. "+amount+" transferred successfully ");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed !");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance !");
                    }
                }else {
                    System.out.println("Invalid Security Pin !");
                }
            }else {
                System.out.println("Invalid Account Number !");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }



    // To get account balance
    public void getBalance(long accountNumber){
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String securityPin = scanner.nextLine();
        try {
            String balanceQuery = "SELECT balance FROM Accounts WHERE account_number =? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(balanceQuery);
            preparedStatement.setLong(1,accountNumber);
            preparedStatement.setString(2,securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println(" Your Balance : "+balance);
            }else {
                System.out.println(" Invalid Pin !");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

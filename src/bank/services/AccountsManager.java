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
    public void debitMoney (long accountNumber) throws  SQLException{
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




    
}

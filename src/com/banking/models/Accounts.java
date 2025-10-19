package com.banking.models;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //Check if account exists
    public boolean accountExists(String email){
        String query =  "SELECT account_number FROM Accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    //Generate account number before making new account
    private  long generateAccountNumber(){
        try {
            String genAccNumQuery = "SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(genAccNumQuery);

            if (resultSet.next()){
                long lastAccountNumber = resultSet.getLong("account_number");
                return lastAccountNumber + 2;
            }else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    // New Users can open and make new account
    public long openAccount (String email) {
        //Check if account exists
        if (!accountExists(email)){
            String AccountQuery = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES (?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("Enter Full Name :");
            String fullName = scanner.nextLine();
            System.out.print("Enter Initial Amount : ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Set Security Pin : ");
            String securityPin = scanner.nextLine();

            try {
                long accountNumber = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(AccountQuery);
                preparedStatement.setLong(1,accountNumber);
                preparedStatement.setString(2,fullName);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,securityPin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0){
                    return accountNumber;
                }else {
                    throw new RuntimeException(" Account Creation Failed !!");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException(" This Account ALready Exists !");
    }



    // Get Account Number
    public long getAccountNumber(String email){
        String query = "SELECT account_number FROM Accounts WHERE email=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("⚠️Account Number does not Exist");
    }
}

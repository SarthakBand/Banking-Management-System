package com.banking.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    

    // New Users can open and make new account
    public long openAccount (String email) {
        //Check if account exists
        if (!accountExists(email)){
            String AccountQuery = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES (?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("");
        }
    }
}

package bank.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    // Allow users to register to the Bank
    public void register() {
        //Taking User credentials
        scanner.nextLine();
        System.out.print("Full Name :");
        String fullName =  scanner.nextLine();
        System.out.print("Email :");
        String email = scanner.nextLine();
        System.out.println("Password :");
        String password = scanner.nextLine();

        //To check if the user already exists
        if (userExists(email)){
            System.out.print("User already Exists for this Email !!");
            System.out.println("Please Login Instead !!");
            return;
        }

        //Query
        String registerQuery = "INSERT INTO user(full_name , email , password) VALUES (?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Registration Successful !!");
            }else {
                System.out.println("Registration Failed !");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    // Allow existing users to login to the bank
    public String login(){
        scanner.nextLine();
        System.out.print("Enter you Email :");
        String email = scanner.nextLine();
        System.out.print("Enter you Password :");
        String password = scanner.nextLine();

        //Query
        String loginQuery = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return email;
            }else {
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    // To check if the user really exists
    public boolean userExists (String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}

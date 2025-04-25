/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Customer {

// This Class represents a customer in the Bank system with a unique username and password.
// And it manages customer's account balance, level, and spendings and it performs 
// actions like depositing money, withdrawing money, and making online purchases.    
    
// This class is mutable.
    
// Abstraction Function:
// Represents a bank customer with a unique username and password.
// Has a balance that has information of amount of money in the customer's account.
// The level represents the customer balance status which are Gold , Silver and Platinum.
// The amountSpent tracks of the total amount spent through online purchases.
// The filePath indicates the path to the file that stores customer information. 
    
// Rep Invariant:
// Username and password must not be null.
// Balance cant be negative.
// Level must be one of "Silver", "Gold", or "Platinum".
// Amount spent cannot be negative.
// FilePath must not be null.
    
    private String username;
    private String password;
    private double balance;
    private String level;
    private double amountSpent;
    private String filePath;
    private CustomerState state;

    public Customer(String username, String password) {
        
        // Requires: Username and password are not null.
        // Modifies: This
        // Effects: Initializes a new Customer object with the given username and password.
        //         Sets balance to 100 and level to "Silver" by default.
        //         Loads customer information from file if available.
        this.username = username;
        this.password = password;
        this.filePath = "customers/" + username + ".txt";
        loadCustomerFromFile();
        if (this.balance == 0) {
            this.balance = 100;
            saveCustomerToFile();
        }
        this.level = CustomerLevel.determineLevel(this.balance);
        this.amountSpent = 0;
        setLevelBasedOnBalance();
    }
    
    public String getUsername() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the username of the customer.
        return username;
    }
    
    public void setUsername(String Username) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the username of the customer to the given value.
        this.username = username;
    }
    
    public String getPassword() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the password of the customer.
        return password;
    }
    
    public void setPassword(String password) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the password of the customer to the given value.
        this.password = password;
    }
    
    public double getBalance() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the balance of the customer.
        return balance;
    }
    
    public void setBalance(double balance) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the balance of the customer to the given value.
        //          Adjusts the customer's level based on the new balance.
        this.balance = balance;
        this.level = CustomerLevel.determineLevel(balance);
        saveCustomerToFile();
    }
    
    public String getBalanceAsString() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the balance of the customer as a string.
        return String.valueOf(balance);
    }
    
    public String getLevel() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the level of the customer.
        return level;
    }
    
    public void setLevel(String level) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the level of the customer to the given value.
        this.level = level;
    }

    
    public void setState(CustomerState state) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the state of the customer to the given state.
        this.state = state;
    }
    
    public double getAmountSpent() {
        // Requires: None
        // Modifies: None
        // Effects: Returns the amount spent by the customer.
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        // Requires: None
        // Modifies: This
        // Effects: Sets the amount spent by the customer to the given value.
        this.amountSpent = amountSpent;
    }
    
    public boolean login(String username, String password) {
        // Requires: None
        // Modifies: None
        // Effects: Returns true if the given username and password match the customer
        //          username and password, false otherwise.
        return this.username.equals(username) && this.password.equals(password);
    }

    public void deposit(double amount) {
        // Requires: Amount should not be negative.
        // Modifies: This
        // Effects: Increases the balance of the customer by the given amount.
        //         Adjusts the customer's level according to condition
        this.balance += amount;
        setLevelBasedOnBalance();;
        saveCustomerToFile();
    }

    public void withdraw(double amount) {
        // Requires: Amount should not be negative and less than or equal to current balance
        // Modifies: This
        // Effects: Decreases the balance of the customer by the given amount.
        //         Adjusts the customer's level according to condition
        if (amount <= this.balance) {
            this.balance -= amount;
            setLevelBasedOnBalance();
            saveCustomerToFile();
        }
    }
    
    private void setLevelBasedOnBalance() {
        // Requires: None
        // Modifies: This
        // Effects: Sets the level of the customer based on their balance.
        CustomerLevel.setLevelBasedOnBalance(this);
    }

    public void processOnlinePurchase(double amount) {
        // Requires: Amount should not be negative.
        // Modifies: This
        // Effects:  If the purchase amount is 50 dollars,processes the purchase and deducts the amount
        //           from the balance and adjusts the customer's level according to conditon.
        state.processOnlinePurchase(this, amount);
        saveCustomerToFile();
    }
    
    private boolean repOk() {
        // Requires: None
        // Modifies: None
        // Effects:  Returns true if the Customer object satisfies the rep invariant, false otherwise
        return username != null && password != null &&
                balance >= 0 && (level.equals("Silver") || level.equals("Gold") || level.equals("Platinum")) &&
                amountSpent >= 0 && filePath != null;
    }

    private void saveCustomerToFile() {
        // Requires: None
        // Modifies: File system
        // Effects: Saves the customer's information password and balance to a file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(password);
            writer.newLine();
            writer.write(String.valueOf(balance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadCustomerFromFile() {
        // Requires: None
        // Modifies: File system
        // Effects:  Loads the customer's information password and balance from a file.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            if ((line = reader.readLine()) != null) {
                this.password = line.trim();
            }
            if ((line = reader.readLine()) != null) {
                this.balance = Double.parseDouble(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }

    }

    @Override
    public String toString() {
        // Requires: None
        // Modifies: None
        // Effects:  Returns a string representation of the Customer object.
        return "Customer{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", level='" + level + '\'' +
                ", amountSpent=" + amountSpent +
                '}';
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;

public class UserInterface extends Application {

    private Manager manager = new Manager("admin", "admin");
    private Map<String, String> customerCredentials;

    @Override
    public void start(Stage primaryStage) {
        boolean credentialsLoaded = loadCustomerCredentials();
        
        primaryStage.setTitle("Bank Application");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("password");
        GridPane.setConstraints(passwordInput, 1, 1);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        loginButton.setOnAction(e -> {
            String username = usernameInput.getText().trim();
            String password = passwordInput.getText().trim();

            if (manager.authenticateManager(username, password)) {
                managerWindow(primaryStage);
            } else {
                if (customerCredentials.containsKey(username) && customerCredentials.get(username).equals(password)) {
                    Customer customer = new Customer(username, password);
                    openCustomerWindow(primaryStage, customer);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password.");
                    alert.showAndWait();
                }
            }
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        Scene scene = new Scene(grid, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private boolean loadCustomerCredentials() 
    {
    customerCredentials = new HashMap<>();
    String dirPath = "customers/";
    File dir = new File(dirPath);
    if (!dir.exists()) {
        dir.mkdirs();
    }
    
    File[] customerFiles = dir.listFiles();
    if (customerFiles != null) 
    {
        for (File file : customerFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String username = file.getName().replace(".txt", "");
                String password = br.readLine();
                String balanceStr = br.readLine();
                if (balanceStr != null) {
                    double balance = Double.parseDouble(balanceStr);
                    customerCredentials.put(username, password);
                    Customer customer = new Customer(username, password);
                    customer.setBalance(balance);

                } else {
                    System.err.println("Balance not found for customer: " + username);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                return false; 
            }
        }
        return true; 
    }
    return false; 
}
    
    private void updateCustomerBalance(String username, double newBalance) {
    File file = new File("customers/" + username + ".txt");
    if (file.exists()) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(newBalance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    private void deleteCustomerFromFile(String username) {
    File file = new File("customers/" + username + ".txt");
    try {
        if (file.delete()) {
            System.out.println("Customer file deleted successfully: " + username + ".txt");
        } else {
            throw new Exception();
        }
    } catch (Exception e) {
        System.err.println("Failed to delete customer file: " + username + ".txt");
    }
}
    
    private void managerWindow(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Manager Window");
        alert.setHeaderText(null);
        alert.setContentText("Hi Admin!");
        alert.showAndWait();
        openManagerWindow(primaryStage);
    }

    private void openManagerWindow(Stage primaryStage) {
        Stage managerStage = new Stage();
        managerStage.setTitle("Manager Window");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Button addCustomerButton = new Button("Add Customer");
        GridPane.setConstraints(addCustomerButton, 0, 0);

        addCustomerButton.setOnAction(e -> openAddCustomerWindow(primaryStage));

        Button deleteCustomerButton = new Button("Delete Customer");
        GridPane.setConstraints(deleteCustomerButton, 0, 1);

        Button logoutButton = new Button("Logout");
        GridPane.setConstraints(logoutButton, 1, 0);

        addCustomerButton.setOnAction(e -> {
            openAddCustomerWindow(primaryStage);
            managerStage.close();
        });

        deleteCustomerButton.setOnAction(e -> openDeleteCustomerWindow(primaryStage));

        logoutButton.setOnAction(e -> {
            managerStage.close();
            start(primaryStage);
        });

        grid.getChildren().addAll(addCustomerButton, deleteCustomerButton, logoutButton );
        Scene scene = new Scene(grid, 300, 100);
        managerStage.setScene(scene);
        managerStage.show();
    }
   
    private boolean usernameExists(String username) {
    return customerCredentials.containsKey(username);
    }
    
    private void openAddCustomerWindow(Stage primaryStage) {
    Stage addCustomerStage = new Stage();
    addCustomerStage.setTitle("Add Customer");

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(5);
    grid.setHgap(5);

    Label newUsernameLabel = new Label("New Username:");
    GridPane.setConstraints(newUsernameLabel, 0, 0);

    TextField newUsernameInput = new TextField();
    newUsernameInput.setPromptText("New Username");
    GridPane.setConstraints(newUsernameInput, 1, 0);

    Label newPasswordLabel = new Label("New Password:");
    GridPane.setConstraints(newPasswordLabel, 0, 1);

    PasswordField newPasswordInput = new PasswordField();
    newPasswordInput.setPromptText("New Password");
    GridPane.setConstraints(newPasswordInput, 1, 1);

    Button addButton = new Button("Add Customer");
    GridPane.setConstraints(addButton, 1, 2);

    addButton.setOnAction(event -> {
        String username = newUsernameInput.getText().trim();
        String password = newPasswordInput.getText().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            if (usernameExists(username)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists. Please choose another one.");
                alert.showAndWait();
            } else {
                manager.addCustomer(username, password);
                System.out.println("Customer added successfully: " + username);
                loadCustomerCredentials();
                addCustomerStage.close();
                start(primaryStage);
            }
        } else {
            System.out.println("Please enter both username and password.");
        }
    }
    );

    grid.getChildren().addAll(newUsernameLabel, newUsernameInput, newPasswordLabel, newPasswordInput, addButton);
    Scene scene = new Scene(grid, 300, 150);
    addCustomerStage.setScene(scene);
    addCustomerStage.show();
}
    
    private void openDeleteCustomerWindow(Stage primaryStage) {
    primaryStage.setTitle("Delete Customer");

    Label user = new Label("Username: ");
    Label pass = new Label("Password: ");
    Label deleteText = new Label("");
    deleteText.setVisible(false); 

    TextField userField = new TextField();
    PasswordField passField = new PasswordField();

    Button deleteC = new Button("Delete");

    GridPane deletePane = new GridPane();

    deleteC.setOnAction(event -> {
        String username = userField.getText().trim();
        deleteCustomerFromFile(username);
        deleteText.setVisible(true); 
    });

    deletePane.setAlignment(Pos.CENTER);
    deletePane.setVgap(10);
    deletePane.setHgap(10);
    deletePane.setPadding(new Insets(20));
    deletePane.add(user, 0, 0);
    deletePane.add(userField, 1, 0);
    deletePane.add(pass, 0, 1);
    deletePane.add(passField, 1, 1);
    deletePane.add(deleteC, 0, 2);
    deletePane.add(deleteText, 0, 3);

    Scene scene = new Scene(deletePane, 300, 150);
    primaryStage.setScene(scene);
    primaryStage.show();
}
    private void openCustomerWindow(Stage primaryStage, Customer customer) {
   
    Button logoutButton = new Button("Logout");
    Button depositButton = new Button("Deposit");
    Button withdrawButton = new Button("Withdraw");
    Button purchaseButton = new Button("Purchase");

    Label welcomeText = new Label("Hi, " + customer.getUsername()); 
    Label balanceLabel = new Label();
    Label levelLabel = new Label("Level: " + customer.getLevel());

    welcomeText.setStyle("-fx-font-size: 20;");
    balanceLabel.setStyle("-fx-font-size: 18;");
    levelLabel.setStyle("-fx-font-size: 18;");

    GridPane customerGrid = new GridPane();
    customerGrid.setVgap(10);
    customerGrid.setHgap(10);
    customerGrid.setPadding(new Insets(20));

    customerGrid.add(welcomeText, 0, 0, 2, 1);
    customerGrid.add(balanceLabel, 0, 1);
    customerGrid.add(levelLabel, 1, 1);
    customerGrid.add(depositButton, 0, 2);
    customerGrid.add(withdrawButton, 0, 3);
    customerGrid.add(purchaseButton, 1, 2);
    customerGrid.add(logoutButton, 1, 3);

    logoutButton.setOnAction(e -> {
        primaryStage.close(); 
        Stage newStage = new Stage(); 
        start(newStage); 
    });

    depositButton.setOnAction(e -> depositWindow(primaryStage, customer));
    withdrawButton.setOnAction(e -> withdrawWindow(primaryStage, customer));
    purchaseButton.setOnAction(e -> purchaseWindow(primaryStage, customer));

    double balance = customer.getBalance();
    balanceLabel.setText("Balance: $" + balance);

    Scene scene = new Scene(customerGrid, 400, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
}
    
    private void depositWindow(Stage primaryStage, Customer customer) {
    GridPane depositPane = new GridPane();
    
    Label depositText = new Label("Enter Amount:");
    Label levelLabel = new Label("Account Level: " + customer.getLevel());
    Label balanceLabel = new Label("Balance: $" + customer.getBalance());
    Label errorLabel = new Label("");
    
    Button back = new Button("Back");
    Button deposit = new Button("Deposit");
    
    TextField depositField = new TextField();
    
    deposit.setOnAction(e -> {
        double amount = Double.parseDouble(depositField.getText());
        customer.deposit(amount);
        CustomerLevel.setLevelBasedOnBalance(customer);
        levelLabel.setText("Account Level: " + customer.getLevel());
        balanceLabel.setText("Balance: $" + customer.getBalance());
        errorLabel.setText("$" +  amount + " has been deposited to your account.");
    });
    
    back.setOnAction(e -> openCustomerWindow(primaryStage, customer));
    
    depositPane.setAlignment(Pos.CENTER);
    depositPane.add(back, 0, 7);
    depositPane.add(deposit, 0, 1);
    depositPane.add(depositText, 1, 0);
    depositPane.add(depositField, 1, 1);
    depositPane.add(levelLabel, 2, 1);
    depositPane.add(balanceLabel, 1, 2);
    depositPane.add(errorLabel, 1, 4);
      
    Scene scene = new Scene(depositPane, 600, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void withdrawWindow(Stage primaryStage, Customer customer){
    GridPane withdrawPane = new GridPane();
    
    Label withdrawText = new Label("Enter Amount:");
    Label levelLabel = new Label("Account Level: " + customer.getLevel());
    Label balanceLabel = new Label("Balance: $" + customer.getBalance());
    Label errLabel = new Label("");
    
    Button back = new Button("Back");
    Button withdraw = new Button("Withdraw");
    
    TextField withdrawField = new TextField();
    
    withdraw.setOnAction(e -> {
        double amount = Double.parseDouble(withdrawField.getText());
              
        if (amount <= customer.getBalance()) {
            customer.withdraw(amount);
            CustomerLevel.setLevelBasedOnBalance(customer);
            levelLabel.setText("Account Level: " + customer.getLevel());
            balanceLabel.setText("Balance: $" + customer.getBalance());   
            errLabel.setText("Withdrawal of $" + amount + " has been made.");
        } else {
            errLabel.setText("Error: Insufficient Funds.");
        }  
    });
   
    back.setOnAction(e -> openCustomerWindow(primaryStage, customer));
    
    withdrawPane.setAlignment(Pos.CENTER);
    withdrawPane.add(back, 0, 7);
    withdrawPane.add(withdraw, 0, 1);
    withdrawPane.add(withdrawText, 1, 0);
    withdrawPane.add(withdrawField, 1, 1);
    withdrawPane.add(levelLabel, 2, 1);
    withdrawPane.add(balanceLabel, 1, 2);
    withdrawPane.add(errLabel, 1, 4);
    
    Scene scene = new Scene(withdrawPane, 600, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void purchaseWindow(Stage primaryStage, Customer customer) {
    GridPane purchasePane = new GridPane();

    Label purchaseText = new Label("Amount of Purchase:");
    Label levelLabel = new Label("Account Level: " + customer.getLevel());
    Label balanceLabel = new Label("Balance: $" + customer.getBalance());
    Label purchaseLabel = new Label("");

    Button back = new Button("Back");
    Button purchase = new Button("Purchase");

    TextField purchaseField = new TextField();

    purchase.setOnAction(e -> {
        double amount = Double.parseDouble(purchaseField.getText());

        if (amount >= 50) {
            customer.processOnlinePurchase(amount); 
            levelLabel.setText("Account Level: " + customer.getLevel());
            balanceLabel.setText("Balance: $" + customer.getBalance());
            purchaseLabel.setText("Purchase successful. Amount deducted from your account.");
        } else {
            purchaseLabel.setText("Online purchase amount must be at least $50.");
        }
    });

    back.setOnAction(e -> openCustomerWindow(primaryStage, customer));

    purchasePane.setAlignment(Pos.CENTER);
    purchasePane.add(back, 0, 7);
    purchasePane.add(purchase, 0, 1);
    purchasePane.add(purchaseText, 1, 0);
    purchasePane.add(purchaseField, 1, 1);
    purchasePane.add(levelLabel, 2, 1);
    purchasePane.add(balanceLabel, 1, 2);
    purchasePane.add(purchaseLabel, 1, 4);

    Scene scene = new Scene(purchasePane, 600, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}


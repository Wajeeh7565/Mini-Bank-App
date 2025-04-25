/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private String username;
    private String password;
    private final List<Customer> customers;

    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
        this.customers = new ArrayList<>();
        loadCustomersFromFile();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public boolean authenticateManager(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public boolean authenticateCustomer(String username, String password) {
        Customer customer = findCustomer(username, password);
        return customer != null && customer.login(username, password);
    }

    public Customer findCustomer(String username, String password) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public void addCustomer(String username, String password) {
        Customer newCustomer = new Customer(username, password);
        customers.add(newCustomer);
        saveCustomerToFile(newCustomer);
    }

    public void deleteCustomer(String username) {
        customers.removeIf(customer -> customer.getUsername().equals(username));
    }

    private void loadCustomersFromFile() {
        File dir = new File("customers/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] customerFiles = dir.listFiles();
        if (customerFiles != null) {
            for (File file : customerFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String username = file.getName().replace(".txt", "");
                    String password = br.readLine();
                    double balance = Double.parseDouble(br.readLine());
                    Customer customer = new Customer(username, password);
                    customer.setBalance(balance);
                    customers.add(customer);
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveCustomerToFile(Customer customer) {
        String filePath = "customers/" + customer.getUsername() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(customer.getPassword());
            writer.newLine();
            writer.write(String.valueOf(customer.getBalance()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

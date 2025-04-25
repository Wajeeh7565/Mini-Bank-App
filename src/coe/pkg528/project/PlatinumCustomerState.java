/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

public class PlatinumCustomerState implements CustomerState {
    @Override
    public void setLevelBasedOnBalance(Customer customer) {
        double balance = customer.getBalance();
        if (balance < 10000) {
            customer.setState(new SilverCustomerState());
        } else if (balance >= 10000 && balance < 20000) {
            customer.setState(new GoldCustomerState());
        }
    }
    
    @Override
    public void processOnlinePurchase(Customer customer, double amount) {
        customer.withdraw(amount); 
    }
}
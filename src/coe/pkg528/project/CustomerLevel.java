/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

public class CustomerLevel {
    public static final String SILVER = "Silver";
    public static final String GOLD = "Gold";
    public static final String PLATINUM = "Platinum";

    public static String determineLevel(double balance) {
        if (balance < 10000) {
            return SILVER;
        } else if (balance < 20000) {
            return GOLD;
        } else {
            return PLATINUM;
        }
    }

    public static void setLevelBasedOnBalance(Customer customer) {
        String level = determineLevel(customer.getBalance());
        customer.setLevel(level);
        switch (level) {
            case SILVER:
                customer.setState(new SilverCustomerState());
                break;
            case GOLD:
                customer.setState(new GoldCustomerState());
                break;
            case PLATINUM:
                customer.setState(new PlatinumCustomerState());
                break;
            default:
                System.out.println("Invalid customer level.");
        }
    }
}

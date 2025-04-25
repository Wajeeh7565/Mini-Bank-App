/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coe.pkg528.project;

/**
 *
 * @author Muhammad Wajeeh Ul Hassan
 */

public interface CustomerState 
{
   public void processOnlinePurchase(Customer customer, double amount);
   public void setLevelBasedOnBalance(Customer customer);
}

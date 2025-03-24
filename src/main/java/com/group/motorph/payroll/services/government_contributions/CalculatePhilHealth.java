/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.government_contributions;

/**
 *
 * @author MO-IT101-Group 13 | CP1 | S1101
 * Members:
 * Arais, Jude
 * Silva, John Ferd
 * Ponteres, Kaselyn Cates
 * Gabinete, Kristel
 * 
 */

public class CalculatePhilHealth {
    
    /**
     * Calculates PhilHealth contribution based on gross weekly pay. The
     * contribution is calculated as 3% of gross pay, split equally between
     * employer and employee (employee pays half).
     *
     * @param grossWeekPay The gross weekly pay amount
     * @return The calculated PhilHealth contribution amount
     */
    
    public static double calculatePhilHealth(double grossWeekPay) {
        // 3% divided by 4 (weekly) and by 2 (employee share)
        return (grossWeekPay * (0.03 / 4)) / 2;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.government_contributions;
        
public class CalculatePagIbig {
    
    /**
     * Calculates Pag-IBIG contribution based on gross weekly pay. The
     * contribution rate varies based on the pay amount.
     *
     * @param grossWeekPay The gross weekly pay amount
     * @return The calculated Pag-IBIG contribution amount
     */
    public static double calculatePagIbig(double grossWeekPay) {

        // Monthly thresholds converted to weekly
        // Lower threshold: ₱1,000 monthly / 4 = ₱250 weekly
        double lowerThreshold = 1000 / 4;
        // Upper threshold: ₱1,500 monthly / 4 = ₱375 weekly
        double upperThreshold = 1500 / 4;

        if (grossWeekPay >= lowerThreshold && grossWeekPay <= upperThreshold) {
            // 1% contribution for pay within thresholds
            return grossWeekPay * 0.01;
        } else if (grossWeekPay > upperThreshold) {
            // 2% contribution for pay above upper threshold
            return grossWeekPay * 0.02;
        } else {
            // No contribution for pay below lower threshold
            return 0;
        }
    }
    
}

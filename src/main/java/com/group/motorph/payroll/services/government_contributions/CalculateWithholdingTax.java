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

public class CalculateWithholdingTax {
    
    /**
     * Calculates withholding tax based on weekly earnings. This implements the
     * progressive tax rate system according to Philippine tax law. Monthly tax
     * rates are divided by 4 to get weekly equivalent rates.
     *
     * @param weeklyEarnings The weekly earnings amount
     * @return The calculated withholding tax amount
     */
    
    public static double calculateWithholdingTax(double weeklyEarnings) {

        // Initializing tax variable
        double tax;

        // Calculate tax based on progressive tax rates (monthly rates divided by 4 for weekly) 
        if (weeklyEarnings <= 20832 / 4) {
            // No tax for income below this threshold
            tax = 0.0;
        } else if (weeklyEarnings <= 33333 / 4) {
            // 20% of the excess over 20,833/4
            tax = (weeklyEarnings - (20833 / 4)) * 0.20;
        } else if (weeklyEarnings <= 66667 / 4) {
            // 2,500/4 plus 25% of the excess over 33,333/4
            tax = (2500 / 4) + ((weeklyEarnings - (33333 / 4)) * 0.25);
        } else if (weeklyEarnings <= 166667 / 4) {
            // 10,833/4 plus 30% of the excess over 66,667/4
            tax = (10833 / 4) + ((weeklyEarnings - (66667 / 4)) * 0.30);
        } else if (weeklyEarnings <= 666667 / 4) {
            // 40,833.33/4 plus 32% of the excess over 166,667/4
            tax = (40833.33 / 4) + ((weeklyEarnings - (166667 / 4)) * 0.32);
        } else {
            // 200,833.33/4 plus 35% of the excess over 666,667/4
            tax = (200833.33 / 4) + ((weeklyEarnings - (666667 / 4)) * 0.35);
        }

        return tax;
    };
    
}

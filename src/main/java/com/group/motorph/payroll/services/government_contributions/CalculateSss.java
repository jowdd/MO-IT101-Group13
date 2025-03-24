/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.government_contributions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.group.motorph.payroll.utilities.ParseNumber;

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

public class CalculateSss {
    
    /**
     * Calculates SSS contribution based on gross weekly pay.
     * This uses the SSS contribution table to determine the appropriate contribution amount.
     * 
     * @param grossWeekPay The gross weekly pay amount
     * @param sssFilePath The file path of SSS table
     * @return The calculated SSS contribution amount
     */
    public static double calculateSss(double grossWeekPay, String sssFilePath) {
        double sss = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(sssFilePath))) {
            // Skip header
            br.readLine();

            String line;

            // Process each line in the SSS contribution table
            while ((line = br.readLine()) != null) {

                // Split line by tab character to get individual fields
                String[] fields = line.split("\t");

                // Parse salary range and contribution from the table
                int compensationRangeFrom = ParseNumber.parseInt(fields[0]);
                int compensationRangeTo = ParseNumber.parseInt(fields[2]);
                double contribution = ParseNumber.parseDouble(fields[3]);

                // Check if gross week pay falls within this compensation range
                // Convert monthly ranges to weekly by dividing by 4
                if (grossWeekPay > compensationRangeFrom / 4 && grossWeekPay < compensationRangeTo / 4) {

                    // If within range, use this contribution amount (divided by 4 for weekly)
                    sss = contribution / 4;
                    break; // Found the right bracket, stop searching
                }
            }

            // Special case: If pay exceeds maximum SSS compensation bracket
            // Use maximum contribution amount
            if (grossWeekPay > 24750 / 4) {
                sss = 1125.00 / 4;
            }
        } catch (IOException e) {
            System.err.println("Error reading SSS table file: " + e.getMessage());
        }

        return sss;
    }
    
}

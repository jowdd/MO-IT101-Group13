/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.data_loader;

import com.group.motorph.payroll.models.EmployeeData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

public class LoadEmployeeData {
    
    /**
     * Loads employee data for the specified employee ID from the employee data file.
     * This includes personal information, position details, and compensation rates.
     * 
     * @param targetEmployeeId The employee ID for which to load data
     * @param employeeDataPath The path file for employee data table
     * @param employeeData The list where the loaded employee data will be stored
     */
    public static void loadEmployeeData(String targetEmployeeId, String employeeDataPath, ArrayList<EmployeeData> employeeData) {

        try (BufferedReader br = new BufferedReader(new FileReader(employeeDataPath))) {
            // Skip header
            br.readLine();

            String line;

            // Read each line of the employee data file
            while ((line = br.readLine()) != null) {

                // Split the line by tab character to get individual fields
                String[] fields = line.split("\t");

                // First field (index 0) contains the employee ID
                String employeeId = fields[0];

                // Skip this record if it's not for the target employee
                if (!employeeId.equals(targetEmployeeId)) {

                    // Move to next line if not our target employee
                    continue;
                }

                // Extract employee information from specific positions in the TSV
                String lastName = fields[1];
                String firstName = fields[2];
                String birthday = fields[3];
                String status = fields[10];
                String position = fields[11];

                // Parse allowance values, removing commas and converting to double
                double riceSubsidy = ParseNumber.parseDouble(fields[14]);
                double phoneAllowance = ParseNumber.parseDouble(fields[15]);
                double clothingAllowance = ParseNumber.parseDouble(fields[16]);
                double hourlyRate = ParseNumber.parseDouble(fields[18]);

                // Create a new EmployeeData object with the extracted information
                EmployeeData entry = new EmployeeData(employeeId, lastName, firstName, birthday, status, position,
                        riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);

                // Add the employee data to our array list
                employeeData.add(entry);
            }
        } catch (IOException e) {
            System.err.println("Error reading employee data file: " + e.getMessage());
        }
    }
    
}

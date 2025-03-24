/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.ui;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.WeeklyTotals;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import com.group.motorph.payroll.utilities.Formatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

public class ConsoleUI {
    
    // Date formatter for displaying dates (Month dd, yyyy).
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    
    /**
     * Displays the welcome message for the MotorPH Payroll System. This is
     * shown at the start of the program to identify the application.
     */
    public static void displayWelcomeMessage() {
        System.out.println("-------------------------");
        System.out.println("MotorPH Payroll System");
        System.out.println("-------------------------");
    }
    
    /**
     * Displays detailed weekly salary information in a formatted manner. This
     * includes pay period dates, earnings, allowances, deductions, and net pay.
     * @param week The weekly totals containing hours worked and overtime
     * @param data The employee data containing rates and allowances
     * @param sss The SSS contribution amount
     * @param pagIbig The Pag-IBIG contribution amount
     * @param philHealth The PhilHealth contribution amount
     * @param tax The withholding tax amount
     * @param grossWeekPay The gross weekly pay
     * @param netWeekPay The net weekly pay after deductions
     */
    public static void displayWeeklySalary(WeeklyTotals week, EmployeeData data,
            double sss, double pagIbig, double philHealth, double tax, double grossWeekPay, double netWeekPay) {

        // Display pay period dates in a readable format
        System.out.println("Pay Period: "
                + week.payPeriodStart.format(DISPLAY_DATE_FORMATTER) + " - "
                + week.payPeriodEnd.format(DISPLAY_DATE_FORMATTER));

        // Display weekly earning details
        System.out.println("--- Weekly Earning ---");
        System.out.println("Hours Worked: " + Formatter.formatDecimal(week.hoursWorked));
        System.out.println("Hourly Rate: " + Formatter.formatDecimal(data.hourlyRate));
        System.out.println("Over Time: " + Formatter.formatDecimal(week.overTime));

        // Display weekly allowances (monthly allowances divided by 4)
        System.out.println("--- Weekly Allowances ---");
        System.out.println("Rice Subsidy: " + Formatter.formatDecimal(data.riceSubsidy / 4));
        System.out.println("Phone Allowance: " + Formatter.formatDecimal(data.phoneAllowance / 4));
        System.out.println("Clothing Allowance: " + Formatter.formatDecimal(data.clothingAllowance / 4));

        // Display government-mandated deductions
        System.out.println("--- Deductions ---");
        System.out.println("SSS: " + Formatter.formatDecimal(sss));
        System.out.println("PAGIbig: " + Formatter.formatDecimal(pagIbig));
        System.out.println("Phil Health: " + Formatter.formatDecimal(philHealth));
        System.out.println("Withholding Tax: " + Formatter.formatDecimal(tax));

        // Display final pay details
        System.out.println("Gross Pay: " + Formatter.formatDecimal(grossWeekPay));
        System.out.println("Net Pay: " + Formatter.formatDecimal(netWeekPay));
        System.out.println("-------------------------------------------------------");
    }
    
    
    /**
     * Displays basic employee details from the loaded employee data.This 
     * includes personal information and position details.
     * @param employeeData The list where the employee data are stored
     */
    public static void displayEmployeeDetails(ArrayList<EmployeeData> employeeData) {

        for (EmployeeData data : employeeData) {

            // Display section header for employee information
            System.out.println("\n----------------------");
            System.out.println("Employee Information");
            System.out.println("----------------------");

            // Display basic employee details
            System.out.println("Employee ID: " + data.employeeId);
            System.out.println("Employee Name: " + data.lastName + ", " + data.firstName);
            System.out.println("Birthday: " + data.birthday);
            System.out.println("Status: " + data.status);
            System.out.println("Position: " + data.position + "\n");
            System.out.println("-------------------------------------------------------");

            // Display section header for salary information
            System.out.println("Salary Details");
            System.out.println("-------------------------------------------------------");
        }
    }
    
    /**
     * Asks the user if they want to continue checking another employee's
     * salary. This validates the input to ensure a valid response is received.
     * @param scanner The Scanner object used to read user input
     * @return true if the user wants to continue, false otherwise
     */
    public static boolean askToContinue(Scanner scanner) {

        System.out.print("Do you want to view other employee's salary? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();

        while (!response.equals("Y") && !response.equals("N")) {
            System.out.print("Invalid input. Please enter 'Y' or 'N': ");
            response = scanner.nextLine().trim().toUpperCase();
        }

        return response.equals("Y");
    }
    
    /**
     * Prompts the user to enter an employee ID and validates it against the
     * employee records.The method will continue to ask for input until a valid
     * employee ID is provided.
     * @param scanner The Scanner object used to read user input
     * @param employeeDataPath The path for employee data table
     * @return A valid employee ID that exists in the employee records
     */
    public static String scanEmployeeId(Scanner scanner, String employeeDataPath) {
        System.out.println("Please Enter Employee ID to Check Pay Details.");

        String targetEmployeeId;
        boolean isEmployeeInTheRecord;

        do {
            // Prompt user for employee ID
            System.out.print("Enter Employee ID: ");
            targetEmployeeId = scanner.nextLine();

            // Validate if the entered ID exists in company records
            isEmployeeInTheRecord = checkEmployeeId(targetEmployeeId, employeeDataPath);

            // Inform user if ID is not found and ask for re-entry
            if (!isEmployeeInTheRecord) {
                System.out.println("Employee ID not found in the record. Please input again.");
            }

            // Continue asking until a valid ID is entered
        } while (!isEmployeeInTheRecord);

        return targetEmployeeId;
    }
    
    
    /**
     * Checks if the provided employee ID exists in the employee data records.This method reads through the employee data file to 
     * verify the existence of the ID.
     * @param targetEmployeeId The employee ID to check
     * @param employeeDataPath The path for employee data table
     * @return true if the employee ID exists in the records, false otherwise
     */
    private static boolean checkEmployeeId(String targetEmployeeId, String employeeDataPath) {

        // Read the employee data
        // Try-with-resources ensures the file is properly closed after reading
        try (BufferedReader br = new BufferedReader(new FileReader(employeeDataPath))) {

            // Skip header
            String line = br.readLine();

            // Process each line in the employee data file
            while ((line = br.readLine()) != null) {
                // Split the line by tab character to get individual fields
                String[] fields = line.split("\t");

                // First field (index 0) contains the employee ID
                String employeeId = fields[0];

                // Return true if the employeeId matches the targetEmployeeId
                if (employeeId.equals(targetEmployeeId)) {
                    return true; // Means mployee found
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }

        // Return false if employee ID was not found after checking entire file
        return false;
    };
    
}

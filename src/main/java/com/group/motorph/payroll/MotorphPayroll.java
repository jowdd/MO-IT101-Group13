/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.group.motorph.payroll;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.WeeklyTotals;
import com.group.motorph.payroll.models.TimeLogDetails;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;
import com.group.motorph.payroll.ui.ConsoleUI;
import com.group.motorph.payroll.services.data_loader.LoadTimeSheet;
import com.group.motorph.payroll.services.data_loader.LoadEmployeeData;
import com.group.motorph.payroll.services.payroll_calculations.PayrollCalculations;
import com.group.motorph.payroll.services.payroll_calculations.CalculateAndDisplay;

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

public class MotorphPayroll {

    /**
     * ---- Constants. ----*
     */
    // Path to the CSV file containing employee attendance records.
    private static final String ATTENDANCE_RECORD_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "attendance-record.csv").toString();

    // Path to the TSV file containing employee information such as name, birthday, salary, allowances, etc.
    private static final String EMPLOYEE_DATA_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();

    // Path  to the TSV file containing SSS contribution table.
    private static final String SSS_TABLE_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "sss-contribution-table.tsv").toString();


    /**
     * ---- Data Structures. ----*
     */
    /*
    Stores all timesheet entries for the selected employee.
    Each entry contains a daily record with date, hours worked, and overtime.
     */
    static ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();

    /*
    Stores employee data such as personal information, allowances, and salary rate.
    Each entry contains a single employee's complete profile.
     */
    static ArrayList<EmployeeData> employeeData = new ArrayList<>();

    /*
    Stores weekly totals for hours worked and overtime.
    Each entry represents one week's data with aggregated hours and pay period dates.
     */
    static ArrayList<WeeklyTotals> weeklyTotals = new ArrayList<>();

    
    /**
     * Clears all data structures to prepare for a new employee data processing.
     * This ensures that no previous employee data remains when processing a new
     * request.
    *
     */
    private static void clearData() {
        timeSheet.clear();
        employeeData.clear();
        weeklyTotals.clear();
    }


    /**
     * The main method that runs the MotorPH Payroll System. It handles the
     * program flow including user input, data processing, and result display.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            boolean continueProgram = true;

            ConsoleUI.displayWelcomeMessage();

            while (continueProgram) {
                // Clear previous data to start fresh
                clearData();

                // Get employee ID and validate it
                String targetEmployeeId = ConsoleUI.scanEmployeeId(scanner, EMPLOYEE_DATA_PATH);

                // Load and process data for the selected employee
                LoadEmployeeData.loadEmployeeData(targetEmployeeId, EMPLOYEE_DATA_PATH, employeeData);
                LoadTimeSheet.loadTimeSheet(targetEmployeeId, ATTENDANCE_RECORD_PATH, timeSheet);
                PayrollCalculations.calculateWeeklyTotals(timeSheet, weeklyTotals);

                // Display employee information and salary details
                ConsoleUI.displayEmployeeDetails(employeeData);
                CalculateAndDisplay.calculateAndDisplaySalary(weeklyTotals, employeeData, SSS_TABLE_PATH);

                // Ask if user wants to continue with another employee
                continueProgram = ConsoleUI.askToContinue(scanner);
            }
        }
        System.out.println("Thank you for using MotorPH Payroll System!");

    }
}

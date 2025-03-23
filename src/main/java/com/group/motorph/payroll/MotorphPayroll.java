/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.group.motorph.payroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.nio.file.Paths;

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

    // Store opening time (8:00 AM).
    private static final LocalTime STORE_OPENING_TIME = LocalTime.of(8, 0);

    // Store closing time (5:00 PM).
    private static final LocalTime STORE_CLOSING_TIME = LocalTime.of(17, 0);

    // Grace period for late arrivals (10 minutes).
    private static final Duration GRACE_PERIOD = Duration.between(LocalTime.of(8, 0), LocalTime.of(8, 10));

    // Grace period expressed in hours for calculations.
    private static final double GRACE_PERIOD_HOURS = GRACE_PERIOD.toMinutes() / 60.0;

    // Date formatter for parsing input dates (MM/dd/yyyy).
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Time formatter for parsing input times (H:mm).
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    // Date formatter for displaying dates (Month dd, yyyy).
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

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
     * Displays the welcome message for the MotorPH Payroll System. This is
     * shown at the start of the program to identify the application.
     */
    private static void displayWelcomeMessage() {
        System.out.println("-------------------------");
        System.out.println("MotorPH Payroll System");
        System.out.println("-------------------------");
    }

    /**
     * Prompts the user to enter an employee ID and validates it against the
     * employee records. The method will continue to ask for input until a valid
     * employee ID is provided.
     *
     * @param scanner The Scanner object used to read user input
     * @return A valid employee ID that exists in the employee records
     */
    private static String scanEmployeeId(Scanner scanner) {
        System.out.println("Please Enter Employee ID to Check Pay Details.");

        String targetEmployeeId;
        boolean isEmployeeInTheRecord;

        do {
            // Prompt user for employee ID
            System.out.print("Enter Employee ID: ");
            targetEmployeeId = scanner.nextLine();

            // Validate if the entered ID exists in company records
            isEmployeeInTheRecord = checkEmployeeId(targetEmployeeId);

            // Inform user if ID is not found and ask for re-entry
            if (!isEmployeeInTheRecord) {
                System.out.println("Employee ID not found in the record. Please input again.");
            }

            // Continue asking until a valid ID is entered
        } while (!isEmployeeInTheRecord);

        return targetEmployeeId;
    }

    /**
     * Checks if the provided employee ID exists in the employee data records.
     * This method reads through the employee data file to verify the existence
     * of the ID.
     *
     * @param targetEmployeeId The employee ID to check
     * @return true if the employee ID exists in the records, false otherwise
     */
    private static boolean checkEmployeeId(String targetEmployeeId) {

        // Read the employee data
        // Try-with-resources ensures the file is properly closed after reading
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_PATH))) {

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
    }

    ;
    
    
    /**
     * Loads employee data for the specified employee ID from the employee data file.
     * This includes personal information, position details, and compensation rates.
     * 
     * @param targetEmployeeId The employee ID for which to load data
     */
    private static void loadEmployeeData(String targetEmployeeId) {

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_PATH))) {
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
                double riceSubsidy = parseDouble(fields[14]);
                double phoneAllowance = parseDouble(fields[15]);
                double clothingAllowance = parseDouble(fields[16]);
                double hourlyRate = parseDouble(fields[18]);

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

    /**
     * Parses a string containing a number with possible commas into a double
     * value. This is necessary because numbers in the data files may include
     * commas as thousand separators.
     *
     * @param number The string representation of the number to parse
     * @return The parsed double value
     */
    private static double parseDouble(String number) {
        return Double.parseDouble(number.replace(",", "").trim());
    }

    /**
     * Parses a string containing a number with possible commas into an integer
     * value. This is necessary because numbers in the data files may include
     * commas as thousand separators.
     *
     * @param number The string representation of the number to parse
     * @return The parsed integer value
     */
    private static int parseInt(String number) {
        return Integer.parseInt(number.replace(",", "").trim());
    }

    /**
     * Loads time sheet data for the specified employee from the attendance
     * record file. This includes calculating hours worked, overtime, and
     * organizing data by week.
     *
     * @param targetEmployeeId The employee ID for which to load time sheet data
     */
    private static void loadTimeSheet(String targetEmployeeId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_PATH))) {
            // Skip header
            br.readLine();

            String line;

            // Process each line in the attendance record
            while ((line = br.readLine()) != null) {

                // Split line by comma to get individual fields
                String[] fields = line.split(",");

                // First field (index 0) contains the employee ID
                String employeeId = fields[0];

                // Skip this record if it's not for the target employee
                if (!employeeId.equals(targetEmployeeId)) {

                    // Move to next line if not our target employee
                    continue;
                }

                // Extract attendance information from specific positions in the CSV
                String lastName = fields[1];
                String firstName = fields[2];

                // Convert string date to LocalDate object using the defined formatter
                LocalDate date = LocalDate.parse(fields[3], DATE_FORMATTER);

                // Convert string time to LocalTime object using the defined formatter
                LocalTime logIn = LocalTime.parse(fields[4], TIME_FORMATTER);
                LocalTime logOut = LocalTime.parse(fields[5], TIME_FORMATTER);

                // Initialize variables for hours calculation
                double hoursWorked;
                double overTime = 0;

                // Calculate late hours by finding time difference between expected arrival and actual arrival
                Duration lateDuration = Duration.between(STORE_OPENING_TIME, logIn);
                // Convert duration to hours for easier comparison
                double lateHours = lateDuration.toMinutes() / 60.0;

                // Calculate working hours based on arrival time
                if (lateHours <= GRACE_PERIOD_HOURS) {

                    // Within grace period - employee arrived within acceptable time window
                    // Count full day from opening time regardless of actual arrival time
                    Duration workedDuration = Duration.between(STORE_OPENING_TIME, logOut);

                    // Convert minutes to hours and subtract 1 hour for lunch break
                    hoursWorked = (workedDuration.toMinutes() / 60.0) - 1; // Subtract 1 hour for lunch

                    // If worked more than standard 8 hours, calculate overtime
                    if (hoursWorked > 8.0) {
                        // Hours beyond 8 are considered overtime
                        overTime = hoursWorked - 8.0;
                    }
                } else {
                    // Beyond grace period - employee was significantly late
                    // Count hours from actual login time instead of opening time
                    Duration workedDuration = Duration.between(logIn, STORE_CLOSING_TIME);

                    // Convert minutes to hours and subtract 1 hour for lunch break
                    hoursWorked = (workedDuration.toMinutes() / 60.0) - 1;
                }

                // Determine which week this date belongs to for weekly reporting
                // Uses the ISO week numbering system
                int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());

                // Create a time log entry with all calculated values
                TimeLogDetails entry = new TimeLogDetails(employeeId, lastName, firstName, date, logIn, logOut, hoursWorked, overTime, weekNumber);

                // Add the entry to our timesheet array list
                timeSheet.add(entry);
            }
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }
    }

    /**
     * Calculates weekly totals for hours worked and overtime from the time
     * sheet data. This aggregates daily values into weekly summaries and
     * establishes pay period dates.
     */
    private static void calculateWeeklyTotals() {

        // Process each time log entry to calculate weekly totals
        for (TimeLogDetails log : timeSheet) {
            // Find existing weekly total for this week or create a new one
            WeeklyTotals weeklyTotal = findOrCreateWeeklyTotal(log.weekNumber, log.date);

            // Update pay period start and end dates to cover all dates in this week
            updatePayPeriodDates(weeklyTotal, log.date);

            // Add this day's hours to the weekly total
            weeklyTotal.hoursWorked += log.hoursWorked;
            // Add this day's overtime to the weekly total
            weeklyTotal.overTime += log.overTime;
        }
    }

    /**
     * Finds an existing weekly total record or creates a new one if none
     * exists. This helps in organizing time sheet data by week for salary
     * calculations.
     *
     * @param weekNumber The week number to find or create
     * @param date The date associated with this week
     * @return The existing or newly created WeeklyTotals object
     */
    private static WeeklyTotals findOrCreateWeeklyTotal(int weekNumber, LocalDate date) {

        // Look through existing weekly totals to find a match
        for (WeeklyTotals wt : weeklyTotals) {

            // If we already have data for this week, return it
            if (wt.weekNumber == weekNumber) {

                // Return existing record for this week
                return wt;
            }
        }

        // If week not found, create a new weekly total record
        // Initialize with the current date as both start and end date
        WeeklyTotals newTotal = new WeeklyTotals(weekNumber, date, date);

        // Add the new weekly total to our collection
        weeklyTotals.add(newTotal);
        return newTotal;
    }

    /**
     * Updates the pay period start and end dates based on the given date. This
     * ensures that the pay period spans all dates within the week.
     *
     * @param weeklyTotal The weekly total record to update
     * @param date The date to consider for updating pay period dates
     */
    private static void updatePayPeriodDates(WeeklyTotals weeklyTotal, LocalDate date) {

        // If this date is earlier than current start date, update start date
        if (date.compareTo(weeklyTotal.payPeriodStart) < 0) {

            // New earliest date in the pay period
            weeklyTotal.payPeriodStart = date;
        }

        // If this date is later than current end date, update end date
        if (date.compareTo(weeklyTotal.payPeriodEnd) > 0) {

            // New latest date in the pay period
            weeklyTotal.payPeriodEnd = date;
        }
    }

    /**
     * Displays basic employee details from the loaded employee data. This
     * includes personal information and position details.
     */
    private static void displayEmployeeDetails() {

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
            System.out.println("---------------");

            // Display section header for salary information
            System.out.println("Salary Details");
            System.out.println("---------------");
        }
    }

    /**
     * Calculates and displays salary details for each pay period. This includes
     * earnings, deductions, and net pay calculations.
     */
    private static void calculateAndDisplaySalary() {

        // Process each week in the data
        for (WeeklyTotals week : weeklyTotals) {

            // Process employee data (usually just one employee)
            for (EmployeeData data : employeeData) {

                // Calculate weekly allowances from monthly values
                // Monthly allowances are divided by 4 to get weekly equivalent
                double weeklyAllowances = calculateWeeklyAllowances(data);

                // Calculate overtime pay with 25% premium rate
                // Overtime is paid at 1.25 times the regular hourly rate
                double overtimePay = calculateOvertimePay(week.overTime, data.hourlyRate);

                // Calculate regular weekly earnings (hours worked * hourly rate)
                // Plus overtime pay for total earnings
                double weeklyEarnings = (week.hoursWorked * data.hourlyRate) + overtimePay;

                // Calculate gross pay (earnings + allowances)
                double grossWeekPay = weeklyEarnings + weeklyAllowances;

                // Calculate mandatory government deductions
                double sss = calculateSss(grossWeekPay);
                double pagIbig = calculatePagIbig(grossWeekPay);
                double philHealth = calculatePhilHealth(grossWeekPay);
                double tax = calculateWithholdingTax(weeklyEarnings);

                // Calculate net pay after all deductions
                double totalDeductions = sss + pagIbig + philHealth + tax;
                double netWeekPay = grossWeekPay - totalDeductions;

                // Display results in a formatted manner
                displayWeeklySalary(week, data, sss, pagIbig, philHealth, tax, grossWeekPay, netWeekPay);
            }
        }
    }

    /**
     * Calculates weekly allowances from monthly allowance values. This divides
     * each monthly allowance by 4 to get weekly equivalent amounts.
     *
     * @param data The employee data containing allowance information
     * @return The total weekly allowances
     */
    private static double calculateWeeklyAllowances(EmployeeData data) {

        // Sum all monthly allowances and divide by 4 to get weekly value
        // Assumes 4 weeks in a month for calculation purposes
        return (data.clothingAllowance + data.phoneAllowance + data.riceSubsidy) / 4;
    }

    /**
     * Calculates overtime pay based on hours and rate. Overtime is paid at 1.25
     * times the regular hourly rate.
     *
     * @param overTime The number of overtime hours
     * @param hourlyRate The regular hourly rate
     * @return The overtime pay amount
     */
    private static double calculateOvertimePay(double overTime, double hourlyRate) {
        return overTime * (hourlyRate + (hourlyRate * 0.25));
    }

    /**
     * Displays detailed weekly salary information in a formatted manner. This
     * includes pay period dates, earnings, allowances, deductions, and net pay.
     *
     * @param week The weekly totals containing hours worked and overtime
     * @param data The employee data containing rates and allowances
     * @param sss The SSS contribution amount
     * @param pagIbig The Pag-IBIG contribution amount
     * @param philHealth The PhilHealth contribution amount
     * @param tax The withholding tax amount
     * @param grossWeekPay The gross weekly pay
     * @param netWeekPay The net weekly pay after deductions
     */
    private static void displayWeeklySalary(WeeklyTotals week, EmployeeData data,
            double sss, double pagIbig, double philHealth, double tax, double grossWeekPay, double netWeekPay) {

        // Display pay period dates in a readable format
        System.out.println("Pay Period: "
                + week.payPeriodStart.format(DISPLAY_DATE_FORMATTER) + " - "
                + week.payPeriodEnd.format(DISPLAY_DATE_FORMATTER));

        // Display weekly earning details
        System.out.println("- Weekly Earning -");
        System.out.println("Hours Worked: " + formatDecimal(week.hoursWorked));
        System.out.println("Hourly Rate: " + formatDecimal(data.hourlyRate));
        System.out.println("Over Time: " + formatDecimal(week.overTime));

        // Display weekly allowances (monthly allowances divided by 4)
        System.out.println("- Weekly Allowances -");
        System.out.println("Rice Subsidy: " + formatDecimal(data.riceSubsidy / 4));
        System.out.println("Phone Allowance: " + formatDecimal(data.phoneAllowance / 4));
        System.out.println("Clothing Allowance: " + formatDecimal(data.clothingAllowance / 4));

        // Display government-mandated deductions
        System.out.println("- Deductions -");
        System.out.println("SSS: " + formatDecimal(sss));
        System.out.println("PAGIbig: " + formatDecimal(pagIbig));
        System.out.println("Phil Health: " + formatDecimal(philHealth));
        System.out.println("Withholding Tax: " + formatDecimal(tax));

        // Display final pay details
        System.out.println("Gross Pay: " + formatDecimal(grossWeekPay));
        System.out.println("Net Pay: " + formatDecimal(netWeekPay));
        System.out.println("---------------");
    }

    /**
     * Formats a decimal value to two decimal places for display purposes.
     *
     * @param value The decimal value to format
     * @return A formatted string with two decimal places
     */
    private static String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    /**
     * Calculates withholding tax based on weekly earnings. This implements the
     * progressive tax rate system according to Philippine tax law. Monthly tax
     * rates are divided by 4 to get weekly equivalent rates.
     *
     * @param weeklyEarnings The weekly earnings amount
     * @return The calculated withholding tax amount
     */
    private static double calculateWithholdingTax(double weeklyEarnings) {

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
    }

    ;
    
    
    /**
     * Calculates SSS contribution based on gross weekly pay.
     * This uses the SSS contribution table to determine the appropriate contribution amount.
     * 
     * @param grossWeekPay The gross weekly pay amount
     * @return The calculated SSS contribution amount
     */
    private static double calculateSss(double grossWeekPay) {
        double sss = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(SSS_TABLE_PATH))) {
            // Skip header
            br.readLine();

            String line;

            // Process each line in the SSS contribution table
            while ((line = br.readLine()) != null) {

                // Split line by tab character to get individual fields
                String[] fields = line.split("\t");

                // Parse salary range and contribution from the table
                int compensationRangeFrom = parseInt(fields[0]);
                int compensationRangeTo = parseInt(fields[2]);
                double contribution = parseDouble(fields[3]);

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

    /**
     * Calculates Pag-IBIG contribution based on gross weekly pay. The
     * contribution rate varies based on the pay amount.
     *
     * @param grossWeekPay The gross weekly pay amount
     * @return The calculated Pag-IBIG contribution amount
     */
    private static double calculatePagIbig(double grossWeekPay) {

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

    /**
     * Calculates PhilHealth contribution based on gross weekly pay. The
     * contribution is calculated as 3% of gross pay, split equally between
     * employer and employee (employee pays half).
     *
     * @param grossWeekPay The gross weekly pay amount
     * @return The calculated PhilHealth contribution amount
     */
    private static double calculatePhilHealth(double grossWeekPay) {
        // 3% divided by 4 (weekly) and by 2 (employee share)
        return (grossWeekPay * (0.03 / 4)) / 2;
    }

    /**
     * Asks the user if they want to continue checking another employee's
     * salary. This validates the input to ensure a valid response is received.
     *
     * @param scanner The Scanner object used to read user input
     * @return true if the user wants to continue, false otherwise
     */
    private static boolean askToContinue(Scanner scanner) {

        System.out.print("Do you want to view other employee's salary? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();

        while (!response.equals("Y") && !response.equals("N")) {
            System.out.print("Invalid input. Please enter 'Y' or 'N': ");
            response = scanner.nextLine().trim().toUpperCase();
        }

        return response.equals("Y");
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

            displayWelcomeMessage();

            while (continueProgram) {
                // Clear previous data to start fresh
                clearData();

                // Get employee ID and validate it
                String targetEmployeeId = scanEmployeeId(scanner);

                // Load and process data for the selected employee
                loadEmployeeData(targetEmployeeId);
                loadTimeSheet(targetEmployeeId);
                calculateWeeklyTotals();

                // Display employee information and salary details
                displayEmployeeDetails();
                calculateAndDisplaySalary();

                // Ask if user wants to continue with another employee
                continueProgram = askToContinue(scanner);
            }
        }
        System.out.println("Thank you for using MotorPH Payroll System!");

    }
}

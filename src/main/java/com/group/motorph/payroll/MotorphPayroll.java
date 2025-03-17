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
 * @author jude
 */

public class MotorphPayroll {
    
     // For storing all timesheet based on selected employee
    static ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();
     // For storing employee data such as employee information, allowances, and hourly rate
    static ArrayList<EmployeeData> employeeData = new ArrayList<>();
    // For storing the total hours worked, lates, overtime per week
    static ArrayList<WeeklyTotals> weeklyTotals = new ArrayList<>();
    

    // Function to read and store time sheet
    static void storeTimeSheet(String filePath, String targetEmployeeId) {
        
        // Date and Time formatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            String line = br.readLine();
            
            // Process each line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                       
                String employeeId = fields[0];
                
                // Only process and store the entry if the employeeId matches the targetEmployeeId
                if (!employeeId.equals(targetEmployeeId)) {
                    continue;
                }
                
                String lastName = fields[1]; // Last Name
                String firstName = fields[2]; // First Name
                
                // Parse the string to date
                // Date of Time log formatted in mm/dd/yyyy format
                LocalDate date = LocalDate.parse(fields[3], dateFormatter);
                
                // Parse the string to LocalTime
                // Time in and time out formatted in HH:mm format
                LocalTime logIn = LocalTime.parse(fields[4], timeFormatter);
                LocalTime logOut = LocalTime.parse(fields[5], timeFormatter);
                
                // Initialize hoursworked and over time
                double hoursWorked;
                double overTime = 0;
                
                // Initialize worked duration
                Duration workedDuration;
                
                LocalTime storeOpeningTime = LocalTime.of(8, 00); // Identify the opening time of motorph - 08:00 or 8am
                LocalTime storeClosingTime = LocalTime.of(17, 00); // Identify the closing time of motorph - 17:00 or 5pm
                
                // Calculated duration of grace period to be used in identifying lates
                // Converting the duration to hours - the output will be 0.166
                // Store it as gracePeriodHours
                Duration gracePeriodDuration = Duration.between(LocalTime.of(8,0), LocalTime.of(8,10));
                double gracePeriodHours = gracePeriodDuration.toMinutes() / 60.0;
                
                // Identifying the late duration of employee in hours format
                // To identify, get the duration between the store opening time and the log in time
                // Store it as the lateHours
                Duration lateDuration = Duration.between(storeOpeningTime, logIn);
                double lateHours = lateDuration.toMinutes() / 60.0;

                // Check if late is within grace period
                // If lateHours is less than gracePeriodHours, then late is within the grace period and eligible for over time
                // otherwise, not eligible for over time and will subtract the late in hours worked
                if (lateHours <= gracePeriodHours) {
                    
                    // worked duration will be from the opening time and logout time
                    workedDuration = Duration.between(storeOpeningTime, logOut);
                    
                    // Subtracted by one hour for the unpaid lunch
                    hoursWorked = (workedDuration.toMinutes() / 60.0) -1;
                    
                    // If hours worked is greater than 8, then considered as over time
                    if (hoursWorked > 8.0) {
                        
                        overTime = hoursWorked - 8.0;
                    }
                    
                } else {
                    
                    // hours worked should only be from log in time and the store closing time (5pm) only since its not eligible for overtime
                    // any hours rendered beyond 5pm is not counted.
                    workedDuration = Duration.between(logIn, storeClosingTime);
                    hoursWorked = (workedDuration.toMinutes() / 60.0) -1;
                }
                
                // Get the week number of each time log to use in storing it in the weekly logs array
                int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                             
                
                //Create time log entry
                TimeLogDetails entry = new TimeLogDetails(employeeId, lastName, firstName, date, logIn, logOut, hoursWorked, overTime, weekNumber);
                
                // Store each entry in the timeSheet array list
                timeSheet.add(entry);
            }
            
        // Catch error when reading the file
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }
    }
    
    // function to read and store time sheet
    static void storeEmployeeData(String filePath, String targetEmployeeId) {
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            String line = br.readLine();
            
            // Process each line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                       
                String employeeId = fields[0]; // Employee ID
                
                // Only process and store the entry if the employeeId matches the targetEmployeeId
                if (!employeeId.equals(targetEmployeeId)) {
                    continue;
                }
                
                String lastName = fields[1]; // Last Name
                String firstName = fields[2]; // First Name
                String birthday = fields[3]; // Birthday
                String status = fields[10]; // Status
                String position = fields[11]; // Position
                
                // Removing the "," and parse it as double - example: 1,000 will be parse to 1000.00
                double riceSubsidy = Integer.parseInt(fields[14].replace(",", "").trim());
                double phoneAllowance = Integer.parseInt(fields[15].replace(",", "").trim());
                double clothingAllowance = Integer.parseInt(fields[16].replace(",", "").trim());
                double hourlyRate = Double.parseDouble(fields[18]);
                
                //Create employee data entry
                EmployeeData entry = new EmployeeData(employeeId, lastName, firstName, birthday, status, position, riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);
                
                // Add it in the employeeData array list
                employeeData.add(entry);
            }
            
        // Catch error while reading file
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }
    }
    
    // Function to check if Inputted Employee ID is in the record
    static boolean checkEmployeeId(String filePath, String targetEmployeeId) {
        
        // initialize isEmployeeIdInTheRecord to false
        boolean isEmployeeIdInTheRecord = false;
        
        // Read the employee data
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            String line = br.readLine();
            
            // Process each line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                       
                String employeeId = fields[0];
                
                // return true if the employeeId matches the targetEmployeeId
                if (employeeId.equals(targetEmployeeId)) {
                    isEmployeeIdInTheRecord = true;
                    break; // to stop the loop
                }
            }
        
        // Catch any error while reading the file
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }
                
        return isEmployeeIdInTheRecord;
    };
    
    
    // Function to calculate the weekly total of hours worked, and overtime
    static void calculateWeeklyTotals (ArrayList<TimeLogDetails> timeSheet) {
        
        timeSheet.forEach(log -> {
        // Find the week in the weeklyTotals list
            WeeklyTotals weeklyTotal = null;
            for (WeeklyTotals wt : weeklyTotals) {
                if (wt.weekNumber == log.weekNumber) {
                    weeklyTotal = wt;
                    break;
                }
            }
        
        // If the week isn't found, create a new WeeklyTotals entry
        if (weeklyTotal == null) {
            
            LocalDate payPeriodStart = log.date; // Initially set the current log's date as the start date
            LocalDate payPeriodEnd = log.date;   // Initially set the current log's date as the end date
            
            weeklyTotal = new WeeklyTotals(log.weekNumber, payPeriodStart, payPeriodEnd);
            weeklyTotals.add(weeklyTotal);
        }
        
        // Update the pay period start and end dates
        if (log.date.compareTo(weeklyTotal.payPeriodStart) < 0) {
            weeklyTotal.payPeriodStart = log.date; // Update start date
        }
        if (log.date.compareTo(weeklyTotal.payPeriodEnd) > 0) {
            weeklyTotal.payPeriodEnd = log.date;   // Update end date
        }
        
        // Accumulate the totals for this week
        weeklyTotal.hoursWorked += log.hoursWorked;
        weeklyTotal.overTime += log.overTime;
        

        });
        
    };
    
    // Functions to calculate withholding tax
    static double calculateWithholdingTax(double weeklyEarnings) {
    
        // Initializing tax as double
        double tax;
        
        // To identify tax rate by weekly, the withholding monthly rate should be divided by 4      
        if (weeklyEarnings <= 20832 / 4) {
            tax = 0.0;
        } else if (weeklyEarnings <= 33333 / 4) {
            tax = (weeklyEarnings - (20833 / 4)) * 0.20;
        } else if(weeklyEarnings <= 66667 / 4) {
            tax = (2500 / 4) + ((weeklyEarnings - (33333 / 4)) * 0.25);
        } else if (weeklyEarnings <= 166667 / 4) {
            tax = (10833 / 4) + ((weeklyEarnings - (66667 / 4)) * 0.30);
        } else if (weeklyEarnings <= 666667 / 4) {
            tax = (40833.33 / 4) + ((weeklyEarnings - (166667 / 4)) * 0.32);
        } else {
            tax = (200833.33 / 4) + ((weeklyEarnings - (666667 / 4)) * 0.35);
        }
        
        return tax;
    };
    
    // Function to calculate SSS contribution
    static double calculateSss(double grossWeekPay) {
        
        double sss = 0.0;
        
        // Path for the sss contribution range file
        String sssTablePath = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "sss-contribution-table.tsv").toString();
        
        // Read the file
        try (BufferedReader br = new BufferedReader(new FileReader(sssTablePath))) {
            // Skip header
            String line = br.readLine();
            
            // Process each line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                       
                // Parse the compensationRangeFrom and compensationRangeTo to int
                // Parse the contribution to double
                int compensationRangeFrom = Integer.parseInt(fields[0].replace(",", "").trim()); // The compensation range from, example: 3250
                int compensationRangeTo = Integer.parseInt(fields[2].replace(",", "").trim()); // The compensation range to, example: 3750
                double contribution = Double.parseDouble(fields[3].replace(",", "").trim()); // Contributio value, example: 157.50
                
                // Check if the gross week pay is within the range, looping again until match found
                // If it's within the specific range, the sss value will be the corresponding contribution value
                // Divided by 4 since it weekly
                if (grossWeekPay > compensationRangeFrom / 4 && grossWeekPay < compensationRangeTo / 4) {
                    sss = contribution / 4;
                    break; // Exit the loop when a match is found
                }
            }
            
            // If it's over 24750 , set SSS to 1125.00
            if (grossWeekPay > 24750 / 4) {
                sss = 1125.00 / 4;
            
            // If it's not on the range and not over 24750, then probably zero
            } else {
                sss = 0;
            }
            
        // Catch any error when reading the file    
        } catch (IOException e) {
            System.err.println("Error reading time log file: " + e.getMessage());
        }
        
        return sss;
    };

    // Function to calculate Pag-Ibig
    static double calculatePagIbig (double grossWeekPay) {
        
        double pagIbig;
        
        // Check if gross week pay if less or equal to 1500 and greater than 1000, if true pag contribution rate will be 1%
        
        if (grossWeekPay <= 1500 / 4 && grossWeekPay >= 1000 / 4) {
            
            // 1% divided 4 since it's weekly
            pagIbig = grossWeekPay * (.01/4);
        } else if (grossWeekPay > 1500 / 4) {
            
            // 1% divided 4 since it's weekly
            pagIbig = grossWeekPay * (.02/4);
        } else {
            
            // Zero if gross week pay is zero
            pagIbig = 0;
        }
        
        return pagIbig;
    };
    
    // Function to calculate philhealth contribution
    static double calculatePhilHealth (double grossWeekPay) {
      
        // 3% divided by 4 since it's weekly and divided by 2 because it's equally shared between employee and employeer
        double philHealth = (grossWeekPay * (.03/4)) / 2;
        
        return philHealth;

    };
    
    // Function to print employee details such as employee id, name, birthay, status, position
    static void printEmployeeDetails() {
        
        // Loop through the employeeData array list to print the employee information
        employeeData.forEach(data -> {
            
            System.out.println("\n----------------------");
            System.out.println("Employee Information");
            System.out.println("----------------------");
            System.out.println("Emloyee ID: " + data.employeeId);
            System.out.println("Employee Name: " + data.lastName + ", " + data.firstName);
            System.out.println("Birthday: " + data.birthday);
            System.out.println("Status: " + data.status);
            System.out.println("Position: " + data.position + "\n");
            System.out.println("---------------");
            System.out.println("Salary Details");
            System.out.println("---------------");
            
        });
        
    }
    
    // Function to print caculate and print salary details
    static void caculateAndPrintSalary() {
        
        // Date formatter for printing in the console
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        
        // Loop through the weekly totals array list to compute the pay
        weeklyTotals.forEach(week -> {
            
            // Loop also through the employee data array list to get the allowances and hourly rate
            employeeData.forEach(data -> {
                
                // Total of all allowances divided by 4, since it's weekly
                double allowances = (data.clothingAllowance + data.phoneAllowance + data.riceSubsidy) / 4;
                
                // Overtime = overtime x (hourly rate + (hourly rate x 25%)
                double overTime = week.overTime * (data.hourlyRate + (data.hourlyRate * .25));
                
                // weekly earnings = total weekly hours worked x hourly rate + over time
                double weeklyEarnings = (week.hoursWorked * data.hourlyRate) + overTime;

                // gross week pay = weekly earnings + allowances
                double grossWeekPay = weeklyEarnings + allowances;
                
                // Get the amount of government contributions
                double sss = calculateSss(grossWeekPay);
                double pagIbig = calculatePagIbig(grossWeekPay);
                double philHealth = calculatePhilHealth(grossWeekPay);

                // Calculate the witholding tax
                double tax = calculateWithholdingTax(weeklyEarnings);
                
                // Calculate the net pay within the week by subtracting first the gross week pay by sss, pagibig, and philhealth and subtract the tax.
                double netWeekPay = grossWeekPay - (sss + pagIbig + philHealth + tax);

                // Print all the necesssary information in the console
                
                // Date formatter is used to print in MMMM DD, YYYY format
                System.out.println("Pay Period: " + week.payPeriodStart.format(dateFormatter) + " - " + week.payPeriodEnd.format(dateFormatter));
                System.out.println("- Weekly Earning -");
                System.out.println("Hours Worked: " + String.format("%.2f",week.hoursWorked));
                System.out.println("Hourly Rate: " + String.format("%.2f", data.hourlyRate));
                System.out.println("Overe Time: " + String.format("%.2f", week.overTime));
                System.out.println("- Weekly Allowances -");
                System.out.println("Rice Subsidy: " + String.format("%.2f", data.riceSubsidy / 4));
                System.out.println("Phone Allowance: " + String.format("%.2f", data.phoneAllowance / 4));
                System.out.println("Clothing Allowance: " + String.format("%.2f", data.clothingAllowance / 4));
                System.out.println("- Deductions -");
                System.out.println("SSS: " + String.format("%.2f", sss)); // Format to 2 decimal places only
                System.out.println("PAGIbig: " + String.format("%.2f", pagIbig)); // Format to 2 decimal places only
                System.out.println("Phil Health: " + String.format("%.2f", philHealth)); // Format to 2 decimal places only
                System.out.println("Withholding Tax: " + String.format("%.2f", tax)); // Format to 2 decimal places only
                System.out.println("Gross Pay: " + String.format("%.2f", grossWeekPay)); // Format to 2 decimal places only
                System.out.println("Net Pay: " + String.format("%.2f", netWeekPay)); // Format to 2 decimal places only
                System.out.println("---------------");
            });
            
        });
    }
    
    // Function to scan and store the targeted employee id
    static String scanEmployeeId() {
        
        String employeeDataPath = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();
        
        Scanner scanner = new Scanner(System.in); // Scanner to scan user input
        boolean isEmployeeInTheRecord; // To store bollean if employee Id is in the record
        String targetEmployeeId; // To store the value of what the user inputted for the employee id
        System.out.println("-------------------------");
        System.out.print("MotorPH Payroll System\n");
        System.out.println("-------------------------");
        System.out.print("Please Enter Employee ID to Check Pay Details.\n");
        
        do {
            System.out.print("Enter Employee ID: ");
            targetEmployeeId = scanner.nextLine(); // Scan the users input
            
            // Check if employee ID is in the employee data
            isEmployeeInTheRecord = checkEmployeeId(employeeDataPath, targetEmployeeId);
            
            // If employee ID is not in the the record, ask again the user, else stop the loop
            if (!isEmployeeInTheRecord) {
                System.out.println("Employee ID not found in the record. Please input again.");
            } else {
                break;
            }
        } while (!isEmployeeInTheRecord);
        
        return targetEmployeeId;
    }
    
    public static void main(String[] args) {
        
        // Path for attenance record and employee data
        String attendanceRecordPath = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "attendance-record.csv").toString();
        String employeeDataPath = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();

        boolean isLoggedIn = true;

        Scanner scanner = new Scanner(System.in);
        
        
        while (isLoggedIn) {
            
            // Ask user to input employee id, scan it and store as the target employee id
            String targetEmployeeId = scanEmployeeId();
            
            // Use the storeTimeSheet to store the all the time sheet
            storeTimeSheet(attendanceRecordPath, targetEmployeeId);
            // Use the storeEmployeeData to store the employee data
            storeEmployeeData(employeeDataPath, targetEmployeeId); 
    
            // Print employee details such as employee id, name, birthay, status, position
            printEmployeeDetails();
            
            // Calculate weekly totals
            calculateWeeklyTotals(timeSheet);
            
            //  Print caculate and print salary details
            caculateAndPrintSalary();
            
                System.out.print("Do you want to view other employee's salary? (Y/N): ");
                String userResponse = scanner.nextLine().trim(); // Trim to remove unnecessary spaces
                
                if (userResponse.equalsIgnoreCase("Y")) {
                    isLoggedIn = true;
                } else if (userResponse.equalsIgnoreCase("N")) {
                    isLoggedIn = false;
                } else {
                    while (!userResponse.equalsIgnoreCase("Y") && !userResponse.equalsIgnoreCase("N")) {
                        System.out.print("Invalid input. Please enter 'Y' or 'N': ");
                        userResponse = scanner.nextLine().trim(); // Scan and trim input again
                    }
                    isLoggedIn = userResponse.equalsIgnoreCase("Y");
                }   
                
        }
        
    }
}

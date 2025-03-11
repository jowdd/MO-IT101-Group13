/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll;

/**
 *
 * @author jude
 */

// Class to represent a the employee data
public class EmployeeData {

    // Fields to store employee-related information and benefits
    public String employeeId; // Unique identifier for the employee
    public String lastName; // Last Name
    public String firstName; // First Name
    public String birthday; // Birthday
    public String status; // Employement status (Regular, Probationary)
    public String position; // Job Position
    public double riceSubsidy; // Monthly rice subsidy
    public double phoneAllowance; // Monthly phone allowance
    public double clothingAllowance; // Monthly Clothing Allowance
    public double hourlyRate; // Hourly rate pay
    
    // Constructor to initialize all fields of the EmployeeData class
    public EmployeeData(String employeeId, String lastName, String firstName, String birthday, String status, String position,
            double riceSubsidy, double phoneAllowance, double clothingAllowance, double hourlyRate) {

        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.status = status;
        this.position = position;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
    }

    // Overriding the toString() method to provide a readable format for printing EmployeeData
    @Override
    public String toString() {
        return String.format("Employee ID: %s, Last Name: %s, First Name: %s, Birthday: %s, Status: %s, Position: %s, Rice Subsidy: %.2f, Phone Allowance: %.2f, Clothing Allowance: %.2f, Hourly Rate: %.2f",
                employeeId, lastName, firstName, birthday, status, position, riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);
    }
}

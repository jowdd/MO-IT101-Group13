/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author jude
 */

// Classes to represent a time log entry
public class TimeLogDetails {
    
    // Fields to store employee information and time log details
    public String employeeId; // Unique identifier for the employee
    public String lastName; // Last name of the employee
    public String firstName; // First name of the employee
    public LocalDate date; // Date of the time log
    public LocalTime logIn; // Time the employee logged in
    public LocalTime logOut; // Time the employee logged out
    public double hoursWorked; // Total hours worked for the day
    public double lates; // Total late in hours
    public double overTime; // Total overtime hours
    public int weekNumber; // Week number of the year
        
    // Constructor to initialize all fields of the TimeLogDetails class
    public TimeLogDetails(String employeeId, String lastName, String firstName, 
    LocalDate date, LocalTime logIn, LocalTime logOut, double hoursWorked, double lates, double overTime, int weekNumber) {
            
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        this.hoursWorked = hoursWorked;
        this.lates = lates;
        this.overTime = overTime;
        this.weekNumber = weekNumber;
        
    }
        
    // Overriding the toString() method to provide a readable format for printing TimeLogDetails
    @Override
    public String toString() {
        return String.format("Employee ID: %s, Last Name: %s, First Name: %s, Date: %s, Log In: %s, Log Out: %s, Hours Worked: %.2f, Lates: %.2f, Over Time: %.2f, Week Number: %d",
        employeeId, lastName, firstName, date, logIn, logOut, hoursWorked, lates, overTime, weekNumber);
    }
}

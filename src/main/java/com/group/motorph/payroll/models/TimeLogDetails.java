/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.models;

import java.time.LocalDate;
import java.time.LocalTime;

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

/**
 * The TimeLogDetails class tracks and manages employee attendance records. It
 * stores individual time entries including login and logout times, calculates
 * hours worked, and tracks overtime for payroll processing.
 */
public class TimeLogDetails {

    // Employee identification information
    /**
     * Unique identifier for the employee. This ID links the time log entry to
     * the corresponding employee record.
     */
    public String employeeId;

    /**
     * Last name of the employee associated with this time log entry. Stored for
     * easier reporting and data validation.
     */
    public String lastName;

    /**
     * First name of the employee associated with this time log entry. Stored
     * for easier reporting and data validation.
     */
    public String firstName;

    // Time tracking data
    /**
     * The calendar date for this attendance record. Stored as a LocalDate
     * object for accurate date calculations.
     */
    public LocalDate date;

    /**
     * The time when the employee checked in or started work. Used to calculate
     * total hours worked and any late penalties.
     */
    public LocalTime logIn;

    /**
     * The time when the employee checked out or ended work. Used to calculate
     * total hours worked and any overtime.
     */
    public LocalTime logOut;

    /**
     * Total number of regular hours worked for this day. This excludes overtime
     * hours and is capped at standard workday length.
     */
    public double hoursWorked;

    /**
     * Total number of overtime hours worked for this day. These are hours
     * worked beyond the standard workday length.
     */
    public double overTime;

    /**
     * The week number of the year (1-52) for this time log entry. Used to group
     * entries for weekly payroll processing.
     */
    public int weekNumber;

    /**
     * Constructs a new TimeLogDetails object with the specified parameters.
     * This constructor initializes a complete time log entry for an employee.
     *
     * @param employeeId The unique identifier for the employee
     * @param lastName The employee's last name
     * @param firstName The employee's first name
     * @param date The date of the time log entry
     * @param logIn The time the employee started work
     * @param logOut The time the employee ended work
     * @param hoursWorked The total regular hours worked for the day
     * @param overTime The total overtime hours for the day
     * @param weekNumber The week number within the year
     */
    public TimeLogDetails(String employeeId, String lastName, String firstName,
            LocalDate date, LocalTime logIn, LocalTime logOut, double hoursWorked, double overTime, int weekNumber) {

        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        this.hoursWorked = hoursWorked;
        this.overTime = overTime;
        this.weekNumber = weekNumber;
    }

    /**
     * Returns a string representation of the TimeLogDetails object. This method
     * formats the time log data into a human-readable format that can be used
     * for display, logging, or debugging purposes.
     *
     * @return A formatted string containing all time log data fields
     */
    @Override
    public String toString() {
        return String.format("Employee ID: %s, Last Name: %s, First Name: %s, Date: %s, Log In: %s, Log Out: %s, Hours Worked: %.2f, Over Time: %.2f, Week Number: %d",
                employeeId, lastName, firstName, date, logIn, logOut, hoursWorked, overTime, weekNumber);
    }
}

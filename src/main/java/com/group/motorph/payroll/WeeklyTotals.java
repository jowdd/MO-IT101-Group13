/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll;

import java.time.LocalDate;

/**
 *
 * @author jude
 */

// Class to represent a the weekly for hours worked, lates, and over time
public class WeeklyTotals {

    // Fields to store information for week's time log
    public int weekNumber; // Week number within year
    public LocalDate payPeriodStart; // Start date of the pay period for the week
    public LocalDate payPeriodEnd; // End date of the pay period for the week
    public double hoursWorked; // Total hours worked during the week
    public double lates; // Total late in hours during the week
    public double overTime; // Total overtime hours worked during the week

    public WeeklyTotals(int weekNumber, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        this.weekNumber = weekNumber;
        this.payPeriodStart = payPeriodStart; // Assign the start date of the pay period
        this.payPeriodEnd = payPeriodEnd; // Assign the end date of the pay period
        this.hoursWorked = 0.0; // Initialize hours worked to 0.0
        this.lates = 0.0; // Initialize total lates to 0.0
        this.overTime = 0.0; // Initialize total overtime to 0.0
    }

    // Overriding the toString() method to provide a readable format for WeeklyTotals
    @Override
    public String toString() {
        return String.format("Week %d:, Pay Period Start Date: %s, Pay Period End Date: %s, Total Hours Worked: %.2f, Total Lates: %.2f, Total Over Time: %.2f",
                weekNumber, payPeriodStart, payPeriodEnd, hoursWorked, lates, overTime);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll;

import java.time.LocalDate;

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
 * The WeeklyTotals class aggregates and manages employee work time data on a weekly basis.
 * It tracks total regular and overtime hours for payroll calculation and reporting.
 * Each instance represents one week's worth of time data for processing payments.
 */
public class WeeklyTotals {

    /**
     * The week number within the year (1-52). This identifies which specific
     * week these totals represent.
     */
    public int weekNumber;

    /**
     * The first day of the pay period for this week. This date marks the
     * beginning of the time period for which these totals apply.
     */
    public LocalDate payPeriodStart;

    /**
     * The last day of the pay period for this week. This date marks the end of
     * the time period for which these totals apply.
     */
    public LocalDate payPeriodEnd;

    /**
     * The total number of regular hours worked during this week. This sum
     * excludes overtime hours and is used for calculating base pay.
     */
    public double hoursWorked;

    /**
     * The total number of overtime hours worked during this week. These hours
     * are typically paid at a premium rate compared to regular hours.
     */
    public double overTime;

    /**
     * Constructs a new WeeklyTotals object for the specified week and pay
     * period. This constructor initializes the week details and sets hour
     * totals to zero. The totals are expected to be populated later by adding
     * individual day records.
     *
     * @param weekNumber The week number within the year
     * @param payPeriodStart The start date of the pay period
     * @param payPeriodEnd The end date of the pay period
     */
    public WeeklyTotals(int weekNumber, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        this.weekNumber = weekNumber;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.hoursWorked = 0.0;  // Initialize with zero hours worked
        this.overTime = 0.0;     // Initialize with zero overtime hours
    }

    /**
     * Returns a string representation of the WeeklyTotals object. This method
     * formats the weekly data into a human-readable string that can be used for
     * reporting, display, or debugging purposes.
     *
     * @return A formatted string containing all weekly total fields
     */
    @Override
    public String toString() {
        return String.format("Week %d:, Pay Period Start Date: %s, Pay Period End Date: %s, Total Hours Worked: %.2f, Total Over Time: %.2f",
                weekNumber, payPeriodStart, payPeriodEnd, hoursWorked, overTime);
    }
}

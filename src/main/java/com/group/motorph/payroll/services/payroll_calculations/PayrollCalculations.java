/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.payroll_calculations;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.TimeLogDetails;
import com.group.motorph.payroll.models.WeeklyTotals;
import java.time.LocalDate;
import java.util.ArrayList;

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


public class PayrollCalculations {
    
    /**
     * Calculates weekly totals for hours worked and overtime from the time
     * sheet data.This aggregates daily values into weekly summaries and establishes pay period dates.
     * @param timeSheet The list where the loaded employee time logs are stored 
     * @param weeklyTotals The list where the weekly totals are being stored
     */
    public static void calculateWeeklyTotals(ArrayList<TimeLogDetails> timeSheet, ArrayList<WeeklyTotals> weeklyTotals) {

        // Process each time log entry to calculate weekly totals
        for (TimeLogDetails log : timeSheet) {
            // Find existing weekly total for this week or create a new one
            WeeklyTotals weeklyTotal = findOrCreateWeeklyTotal(log.weekNumber, log.date, weeklyTotals);

            // Update pay period start and end dates to cover all dates in this week
            updatePayPeriodDates(weeklyTotal, log.date);

            // Add this day's hours to the weekly total
            weeklyTotal.hoursWorked += log.hoursWorked;
            // Add this day's overtime to the weekly total
            weeklyTotal.overTime += log.overTime;
        }
    }
    
        /**
     * Calculates weekly allowances from monthly allowance values. This divides
     * each monthly allowance by 4 to get weekly equivalent amounts.
     *
     * @param data The employee data containing allowance information
     * @return The total weekly allowances
     */
    public static double calculateWeeklyAllowances(EmployeeData data) {

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
    public static double calculateOvertimePay(double overTime, double hourlyRate) {
        return overTime * (hourlyRate + (hourlyRate * 0.25));
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
    private static WeeklyTotals findOrCreateWeeklyTotal(int weekNumber, LocalDate date, ArrayList<WeeklyTotals> weeklyTotals) {

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
    

    
}

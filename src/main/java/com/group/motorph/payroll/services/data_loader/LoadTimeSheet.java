/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.data_loader;

import com.group.motorph.payroll.models.TimeLogDetails;
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

public class LoadTimeSheet {
    
    // Date formatter for parsing input dates (MM/dd/yyyy).
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    // Time formatter for parsing input times (H:mm).
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    
    // Store opening time (8:00 AM).
    private static final LocalTime STORE_OPENING_TIME = LocalTime.of(8, 0);
    
    // Store closing time (5:00 PM).
    private static final LocalTime STORE_CLOSING_TIME = LocalTime.of(17, 0);
    
    // Grace period for late arrivals (10 minutes).
    private static final Duration GRACE_PERIOD = Duration.between(LocalTime.of(8, 0), LocalTime.of(8, 10));

    // Grace period expressed in hours for calculations.
    private static final double GRACE_PERIOD_HOURS = GRACE_PERIOD.toMinutes() / 60.0;

    /**
     * Loads time sheet data for the specified employee from the attendance
     * record file.This includes calculating hours worked, overtime, and
 organizing data by week.
     *
     * @param targetEmployeeId The employee ID for which to load time sheet data
     * @param attendanceRecordPath The file path of attendance record table
     * @param timeSheet The list where the loaded employee time logs will be stored
     */
    public static void loadTimeSheet(String targetEmployeeId, String attendanceRecordPath, ArrayList<TimeLogDetails> timeSheet) {
        try (BufferedReader br = new BufferedReader(new FileReader(attendanceRecordPath))) {
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
    
}

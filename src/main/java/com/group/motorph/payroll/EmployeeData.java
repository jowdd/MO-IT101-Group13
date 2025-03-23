/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll;

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
 * The EmployeeData class stores and manages employee information in the MotorPH
 * payroll system. It contains personal details and compensation information for
 * each employee.
*
 */
public class EmployeeData {

    // Employee identification and personal information
    /**
     * Unique identifier assigned to each employee within the organization. This
     * ID is used as the primary reference in all payroll calculations and
     * reports.
     */
    public String employeeId;

    /**
     * Employee's legal last name as it appears on official documents.
     */
    public String lastName;

    /**
     * Employee's legal first name as it appears on official documents.
     */
    public String firstName;

    /**
     * Employee's date of birth in the format YYYY-MM-DD. This is used for age
     * verification and benefits eligibility.
     */
    public String birthday;

    // Employment details
    /**
     * Current employment status of the employee. Valid values include "Regular"
     * and "Probationary". This status affects benefit eligibility and job
     * security.
     */
    public String status;

    /**
     * Employee's current job title or position within the company. This
     * determines salary range and responsibilities.
     */
    public String position;

    // Employee benefits and compensation details
    /**
     * Monthly rice subsidy amount in Philippine Pesos. This is a standard
     * benefit provided to eligible employees.
     */
    public double riceSubsidy;

    /**
     * Monthly phone allowance in Philippine Pesos. This amount is provided to
     * cover work-related communication expenses.
     */
    public double phoneAllowance;

    /**
     * Monthly clothing allowance in Philippine Pesos. This is provided to
     * maintain professional appearance according to company standards.
     */
    public double clothingAllowance;

    /**
     * The employee's base pay rate per hour in Philippine Pesos. This rate is
     * used to calculate regular wages before deductions and benefits.
     */
    public double hourlyRate;

    /**
     * Constructs a new EmployeeData object with the specified parameters. This
     * constructor initializes all fields with the provided values.
     *
     * @param employeeId The unique identifier for the employee
     * @param lastName The employee's last name
     * @param firstName The employee's first name
     * @param birthday The employee's date of birth
     * @param status The employee's employment status
     * @param position The employee's job position or title
     * @param riceSubsidy The monthly rice subsidy amount
     * @param phoneAllowance The monthly phone allowance amount
     * @param clothingAllowance The monthly clothing allowance amount
     * @param hourlyRate The employee's hourly pay rate
     */
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

    /**
     * Returns a string representation of the EmployeeData object. This method
     * formats all employee information into a readable string that can be used
     * for logging, debugging, or display purposes.
     *
     * @return A formatted string containing all employee data fields
     */
    @Override
    public String toString() {
        return String.format("Employee ID: %s, Last Name: %s, First Name: %s, Birthday: %s, Status: %s, Position: %s, Rice Subsidy: %.2f, Phone Allowance: %.2f, Clothing Allowance: %.2f, Hourly Rate: %.2f",
                employeeId, lastName, firstName, birthday, status, position, riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.utilities;

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

public class ParseNumber {
    
    /**
     * Parses a string containing a number with possible commas into a double
     * value. This is necessary because numbers in the data files may include
     * commas as thousand separators.
     *
     * @param number The string representation of the number to parse
     * @return The parsed double value
     */
    public static double parseDouble(String number) {
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
    public static int parseInt(String number) {
        return Integer.parseInt(number.replace(",", "").trim());
    }
    
}

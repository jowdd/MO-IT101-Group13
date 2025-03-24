# 🏢 MotorPH Payroll System

## 📋 Overview

The MotorPH Payroll System is a Java-based application designed to calculate and display employee salary details based on attendance records, employee information, and government-mandated deduction rates. The system processes time logs, calculates working hours, overtime, allowances, and various deductions to provide comprehensive pay information for each employee.

## ✨ Features

- 👤 Employee Verification: Validates employee IDs against company records
- ⏰ Time Tracking: Processes attendance logs with check-in and check-out times
- 💰 Salary Calculation: Computes weekly earnings based on hours worked and hourly rates
- ⏱️ Overtime Management: Calculates overtime pay with premium rates (125% of regular pay)
- 🎁 Allowance Distribution: Prorates monthly allowances (rice subsidy, phone, clothing) to weekly amounts
- 📑Government Deductions: Calculates mandatory contributions:
    - SSS (Social Security System)
    - PhilHealth
    - Pag-IBIG Fund
    - Withholding Tax



## 💻 System Requirements
- Java Development Kit (JDK) 8 or higher
- Command-line interface for program interaction

## 📁 File Structure
The system relies on several data files:

- **employee-data.tsv**: Employee personal information, rates, and allowances
- **attendance-record.csv**: Daily time logs with check-in and check-out times
- **sss-contribution-table.tsv**: SSS contribution rates based on salary brackets

## 🚀 Installation

1. Clone the repository or download the source code files
2. Ensure the data files are placed in the correct directory:
```bash
src/main/java/com/group/motorph/resources/
```

3. Compile the Java files:
```bash
javac com/group/motorph/payroll/*.java
```

4. Run the application:
```bash
java com.group.motorph.payroll.MotorphPayroll
```


## 🔍 Usage

1. Launch the application
2. Enter a valid employee ID when prompted
3. View the employee information and detailed salary breakdown
4. Choose to check another employee or exit the system

## 📝 Business Rules

- ⏱️ Working Hours: Standard workday is 8 AM to 5 PM
- ⌛ Grace Period: 10-minute grace period for late arrivals (arrivals before 8:10 AM treated as 8:00 AM)
- 🍽️ Break Time: 1-hour lunch break deducted from total hours
- 💼 Overtime Calculation: Hours worked beyond 8 hours per day at 125% of regular rate
- ⏰ Late Arrival Processing: Arrivals after grace period count hours from actual arrival time and not eligible for overtime

## 🧮 Contribution Calculations

- SSS: Based on official SSS contribution table, prorated weekly
- Pag-IBIG: 1% for salary range ₱1,000-₱1,500 monthly, 2% for above ₱1,500
- PhilHealth: 3% of salary, with employee paying half (1.5%)
- Withholding Tax: Based on progressive tax rate system, prorated weekly

## 🖥️ Sample Output
```bash
-------------------------
MotorPH Payroll System
-------------------------
Please Enter Employee ID to Check Pay Details.
Enter Employee ID: 10001

----------------------
Employee Information
----------------------
Employee ID: 10001
Employee Name: Doe, John
Birthday: 01/15/1990
Status: Regular
Position: Sales Manager

---------------
Salary Details
---------------
Pay Period: January 01, 2023 - January 07, 2023
- Weekly Earning -
Hours Worked: 40.00
Hourly Rate: 625.00
Over Time: 2.50
- Weekly Allowances -
Rice Subsidy: 1500.00
Phone Allowance: 500.00
Clothing Allowance: 250.00
- Deductions -
SSS: 200.00
PAGIbig: 100.00
Phil Health: 75.00
Withholding Tax: 1250.00
Gross Pay: 27750.00
Net Pay: 26125.00
---------------
```

## 👨‍💻 Developers
MO-IT101-Group 13 | CP1 | S1101
**Members:**
- Arais, Jude
- Silva, John Ferd
- Ponteres, Kaselyn Cates
- Gabinete, Kristel
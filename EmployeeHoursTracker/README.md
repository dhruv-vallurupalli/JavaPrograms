Employee Work Hours Tracker

This Java program allows employees to record their work hours for a two-week period. The application captures employee details, supervisor details, and allows the user to select a two-week period to input daily hours worked. Once the "Submit Hours" button is clicked, the system sends an email summarizing the hours worked to both the employee and their supervisor.

Key Features:
Employee Details: Collects the employee's name, phone number, email address, and project name.
Supervisor Details: Collects the supervisor's name, phone number, and email address.
Two-Week Period Selection: Allows the employee to choose a specific two-week period for which hours are being entered.
Daily Hours Entry: Provides a table for employees to input their daily hours worked, with automatic calculation of total hours.
Email Notification: After submitting, the program sends a summary email to both the employee and the supervisor, detailing:
   1. Employee and supervisor details.
   2. The selected two-week period.
   3. A breakdown of hours worked per day and total hours.

Setup Instructions:
Email Credentials: Replace the placeholders for email address and app password in the EmployeeHoursTracker.java program with valid credentials. This is required to enable email sending functionality.

Dependencies:
Ensure the following libraries are included in the project:
   1. jakarta.mail-2.0.1.jar
   2. jakarta.activation-api-2.1.3.jar
These libraries are necessary for handling email functionality using the Jakarta Mail API.


This program ensures seamless tracking and reporting of employee work hours while notifying both the employee and supervisor in a professional and automated manner.

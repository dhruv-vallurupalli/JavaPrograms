# Mortgage Calculator UI

This Java program provides a graphical user interface (GUI) to calculate monthly mortgage payments. It allows users to input loan details, such as loan amount, annual interest rate, and loan term, and displays a detailed breakdown of payments for each month, including principal, interest, total payment, and remaining balance.

## Features
- **User Input**: Input fields for:
  - Loan Amount
  - Annual Interest Rate (%)
  - Loan Term (Years)
- **Monthly Breakdown**:
  - Displays a table showing:
    - Month number
    - Principal payment
    - Interest payment
    - Total monthly payment
    - Remaining loan balance
- **Error Handling**:
  - Validates input to ensure all fields are numeric and positive.
  - Provides error messages for invalid inputs.
- **UI**:
  - Results are displayed in a scrollable table.
- **Exit Button**:
  - Allows users to close the application.

## How to Use
1. **Enter Loan Details**:
   - Input the loan amount, annual interest rate, and loan term in the respective fields.
2. **Calculate Payments**:
   - Click the `Calculate` button to compute the monthly payment and see the detailed breakdown.
3. **View Results**:
   - The table will display monthly details for the entire loan term, including:
     - Principal payment
     - Interest payment
     - Total monthly payment
     - Remaining balance after each month.
4. **Exit the Program**:
   - Click the `Exit` button to close the application.

**Technologies Used**
   Java Swing: For GUI design and layout.
   Java AWT: For event handling.
   DefaultTableModel: To manage the table displaying payment details.

**How to complie and run:**
   javac MortgageCalculatorUI.java
   java MortgageCalculatorUI.java


   


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class MortgageCalculatorUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MortgageCalculatorUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Mortgage Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Create main panel for input fields and buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input fields
        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        JTextField loanAmountField = new JTextField();
        JLabel interestRateLabel = new JLabel("Annual Interest Rate (%):");
        JTextField interestRateField = new JTextField();
        JLabel loanTermLabel = new JLabel("Loan Term (Years):");
        JTextField loanTermField = new JTextField();

        // Add input fields to the main panel
        mainPanel.add(loanAmountLabel);
        mainPanel.add(loanAmountField);
        mainPanel.add(interestRateLabel);
        mainPanel.add(interestRateField);
        mainPanel.add(loanTermLabel);
        mainPanel.add(loanTermField);

        // Buttons
        JButton calculateButton = new JButton("Calculate");
        JButton exitButton = new JButton("Exit");
        mainPanel.add(calculateButton);
        mainPanel.add(exitButton);

        frame.add(mainPanel, BorderLayout.NORTH);

        // Table to display the monthly breakdown
        String[] columnNames = {"Month", "Principal Payment", "Interest Payment", "Total Payment", "Remaining Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Action listener for the Calculate button
        calculateButton.addActionListener(e -> {
            try {
                // Parse input values
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                double annualInterestRate = Double.parseDouble(interestRateField.getText());
                int loanTerm = Integer.parseInt(loanTermField.getText());

                // Validate input
                if (loanAmount <= 0 || annualInterestRate <= 0 || loanTerm <= 0) {
                    JOptionPane.showMessageDialog(frame, "All input values must be positive numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform calculations
                double monthlyInterestRate = annualInterestRate / 100 / 12;
                int totalPayments = loanTerm * 12;
                double monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, totalPayments);

                // Format for displaying monetary values
                DecimalFormat df = new DecimalFormat("#,##0.00");

                // Clear previous results from the table
                tableModel.setRowCount(0);

                double remainingBalance = loanAmount;

                // Populate table with monthly breakdown
                for (int month = 1; month <= totalPayments; month++) {
                    double interestPayment = remainingBalance * monthlyInterestRate;
                    double principalPayment = monthlyPayment - interestPayment;
                    remainingBalance -= principalPayment;

                    tableModel.addRow(new Object[]{
                            month,
                            "$" + df.format(principalPayment),
                            "$" + df.format(interestPayment),
                            "$" + df.format(monthlyPayment),
                            "$" + df.format(Math.max(remainingBalance, 0)) // Ensure no negative balance
                    });
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for the Exit button
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static double calculateMonthlyPayment(double loanAmount, double monthlyInterestRate, int totalPayments) {
        return (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -totalPayments));
    }
}

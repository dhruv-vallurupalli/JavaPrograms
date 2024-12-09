import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmployeeHoursTracker {
    private JLabel totalHoursLabel;
    private boolean updatingTable = false; // Guard flag to prevent recursion

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeHoursTracker().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Employee Hours Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000); // Adjusted height for better layout
        frame.setLayout(new BorderLayout());

        // Main Panel with Vertical Layout to hold all sections
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Employee Details Panel
        JPanel employeePanel = createSectionPanel("Employee Details");
        employeePanel.setLayout(new GridLayout(4, 2, 10, 10));
        JTextField empNameField = createTextField();
        JTextField empPhoneField = createTextField();
        JTextField empEmailField = createTextField();
        JTextField projectField = createTextField();
        employeePanel.add(new JLabel("Employee Name:"));
        employeePanel.add(empNameField);
        employeePanel.add(new JLabel("Phone Number:"));
        employeePanel.add(empPhoneField);
        employeePanel.add(new JLabel("Email Address:"));
        employeePanel.add(empEmailField);
        employeePanel.add(new JLabel("Project Name:"));
        employeePanel.add(projectField);
        mainPanel.add(employeePanel);

        // Supervisor Details Panel
        JPanel supervisorPanel = createSectionPanel("Supervisor Details");
        supervisorPanel.setLayout(new GridLayout(3, 2, 10, 10));
        JTextField supervisorNameField = createTextField();
        JTextField supervisorPhoneField = createTextField();
        JTextField supervisorEmailField = createTextField();
        supervisorPanel.add(new JLabel("Supervisor Name:"));
        supervisorPanel.add(supervisorNameField);
        supervisorPanel.add(new JLabel("Phone Number:"));
        supervisorPanel.add(supervisorPhoneField);
        supervisorPanel.add(new JLabel("Email Address:"));
        supervisorPanel.add(supervisorEmailField);
        mainPanel.add(supervisorPanel);

        // Select Two-Week Period Panel
        JPanel datePanel = createSectionPanel("Select Two-Week Period");
        JComboBox<String> dateComboBox = new JComboBox<>();
        dateComboBox.addItem("Mon Dec 2 2024 - Sun Dec 15 2024"); // Example period
        datePanel.add(new JLabel("Select Period:"));
        datePanel.add(dateComboBox);
        mainPanel.add(datePanel);

        // Hours Table Panel
        JPanel hoursPanel = createSectionPanel("Enter Hours");
        String[] columnNames = {"Week", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Total"};
        Object[][] data = {{"Week 1", 0, 0, 0, 0, 0, 0, 0, 0}, {"Week 2", 0, 0, 0, 0, 0, 0, 0, 0}};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 8; // Prevent editing "Week" and "Total" columns
            }
        };
        JTable hoursTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(hoursTable);
        scrollPane.setPreferredSize(new Dimension(750, 100));
        hoursPanel.add(scrollPane);
        mainPanel.add(hoursPanel);

        // Add Listener to Calculate Totals
        tableModel.addTableModelListener(e -> {
            if (updatingTable) return;

            int row = e.getFirstRow();
            int total = 0;

            try {
                updatingTable = true;
                for (int col = 1; col <= 7; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    try {
                        total += Integer.parseInt(value.toString());
                    } catch (NumberFormatException ex) {
                        tableModel.setValueAt(0, row, col);
                    }
                }
                tableModel.setValueAt(total, row, 8);
                updateTotalHoursLabel(tableModel);
            } finally {
                updatingTable = false;
            }
        });

        // Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));

        totalHoursLabel = new JLabel("Total Hours Worked: 0", SwingConstants.CENTER);
        totalHoursLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        summaryPanel.add(totalHoursLabel);

        JButton submitButton = new JButton("Submit Hours");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            String employeeEmail = empEmailField.getText();
            String supervisorEmail = supervisorEmailField.getText();
            String selectedPeriod = dateComboBox.getSelectedItem().toString();
            String messageContent = generateEmailContent(empNameField.getText(), empPhoneField.getText(), empEmailField.getText(),
                                                         projectField.getText(), supervisorNameField.getText(),
                                                         supervisorPhoneField.getText(), supervisorEmailField.getText(),
                                                         hoursTable, selectedPeriod);

            try {
                sendEmail(employeeEmail, supervisorEmail, messageContent);
                JOptionPane.showMessageDialog(frame, "Hours submitted and emailed successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Failed to send email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        summaryPanel.add(submitButton);
        mainPanel.add(summaryPanel);

        // Add Main Panel to Frame
        frame.add(new JScrollPane(mainPanel)); // Add scroll pane for better visibility in small windows
        frame.setVisible(true);
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title, TitledBorder.CENTER, TitledBorder.TOP));
        return panel;
    }

    private JTextField createTextField() {
        return new JTextField(15);
    }

    private void updateTotalHoursLabel(DefaultTableModel tableModel) {
        int grandTotal = 0;
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            grandTotal += Integer.parseInt(tableModel.getValueAt(row, 8).toString());
        }
        totalHoursLabel.setText("Total Hours Worked: " + grandTotal);
    }

    private String generateEmailContent(String empName, String empPhone, String empEmail, String project,
                                        String supervisorName, String supervisorPhone, String supervisorEmail,
                                        JTable hoursTable, String selectedPeriod) {
        StringBuilder sb = new StringBuilder();

        // Add Employee Details
        sb.append("Employee Details:\n")
          .append("Name: ").append(empName).append("\n")
          .append("Phone: ").append(empPhone).append("\n")
          .append("Email: ").append(empEmail).append("\n")
          .append("Project: ").append(project).append("\n\n");

        // Add Supervisor Details
        sb.append("Supervisor Details:\n")
          .append("Name: ").append(supervisorName).append("\n")
          .append("Phone: ").append(supervisorPhone).append("\n")
          .append("Email: ").append(supervisorEmail).append("\n\n");

        // Add Selected Period
        sb.append("Hours Entered for the Period:\n").append(selectedPeriod).append("\n\n");

        // Add Hours Table
        sb.append("Hours Worked:\n");
        for (int row = 0; row < hoursTable.getRowCount(); row++) {
            for (int col = 0; col < hoursTable.getColumnCount(); col++) {
                sb.append(hoursTable.getValueAt(row, col)).append("\t");
            }
            sb.append("\n");
        }

        // Add Total Hours Worked
        sb.append("\n").append(totalHoursLabel.getText());

        return sb.toString();
    }

    private void sendEmail(String employeeEmail, String supervisorEmail, String messageContent) throws Exception {
        String host = "smtp.gmail.com";
        String from = "xxxxxxxx@gmail.com"; // Replace with your email
        String password = "xxxx xxxx xxxx xxxx"; // Replace with your app password

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(from, password);
            }
        });

        jakarta.mail.Message message = new jakarta.mail.internet.MimeMessage(session);
        message.setFrom(new jakarta.mail.internet.InternetAddress(from));
        message.setRecipients(jakarta.mail.Message.RecipientType.TO,
                jakarta.mail.internet.InternetAddress.parse(employeeEmail + "," + supervisorEmail));
        message.setSubject("Hours Submitted");
        message.setText(messageContent);

        jakarta.mail.Transport.send(message);
        System.out.println("Email sent successfully!");
    }
}

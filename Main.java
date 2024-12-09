public class Main {
    public static void main(String[] args) {
        // Launch the EmployeeHoursTracker GUI
        System.out.println("Current classpath: " + System.getProperty("java.class.path"));    
        EmployeeHoursTracker tracker = new EmployeeHoursTracker();
        tracker.main(args);
    }
}


//public class Main {
    // public static void main(String[] args) {
        // Delegate directly to the EmployeeHoursTracker main method
//       EmployeeHoursTracker.main(args);
//    }
//}
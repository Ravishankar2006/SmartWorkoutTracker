import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Workout {
    private LocalDate date;
    private int durationMinutes;
    private String type;

    public Workout(LocalDate date, int durationMinutes, String type) {
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.type = type;
    }

    public LocalDate getDate() { 
        return date; 
    }
    
    public int getDuration() { 
        return durationMinutes; 
    }
    
    public String getType() { 
        return type; 
    }

    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + 
               " - " + type + " (" + durationMinutes + " min)";
    }
}

public class WorkoutTracker {
    private static int totalWorkouts = 0;
    private static int totalMinutes = 0;
    private static int streak = 0;
    private static LocalDate lastWorkoutDate = null; // Track streak properly
    private static ArrayList<Workout> history = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcome();
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Choose option (1-6): ");

            switch (choice) {
                case 1 -> startWorkout();
                case 2 -> viewStats();
                case 3 -> viewHistory();
                case 4 -> timedWorkout();
                case 5 -> resetData();
                case 6 -> {
                    running = false;
                    displayGoodbye();
                }
                default -> System.out.println("Invalid option. Please select 1-6.");
            }
        }
        scanner.close();
    }

    private static void displayWelcome() {
        System.out.println("==========================================");
        System.out.println("          WORKOUT TRACKER                ");
        System.out.println("        Terminal Prototype v1.0          ");
        System.out.println("==========================================");
    }

    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("Current Stats: " + totalWorkouts + " workouts, " + 
                          totalMinutes + " min total, " + streak + " day streak");
        System.out.println("========================================");
        System.out.println("1. Start Workout (Manual)");
        System.out.println("2. View Stats");
        System.out.println("3. View History");
        System.out.println("4. Timed Workout");
        System.out.println("5. Reset All Data");
        System.out.println("6. Exit");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void startWorkout() {
        System.out.println("\nSTARTING WORKOUT");
        System.out.println("Available workout types:");
        System.out.println("1. Upper Body");
        System.out.println("2. Lower Body");
        System.out.println("3. Cardio");
        System.out.println("4. Full Body");

        int typeChoice = getIntInput("Select workout type (1-4): ");
        String[] types = {"Upper Body", "Lower Body", "Cardio", "Full Body"};
        String workoutType = (typeChoice >= 1 && typeChoice <= 4) ? 
                            types[typeChoice - 1] : "General";

        int duration = getIntInput("Enter workout duration in minutes: ");
        if (duration <= 0) {
            System.out.println("Invalid duration. Workout cancelled.");
            return;
        }

        recordWorkout(duration, workoutType);
        
        System.out.println(workoutType + " workout completed!");
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Great job!");
    }

    private static void timedWorkout() {
        System.out.println("\nTIMED WORKOUT");
        int seconds = getIntInput("Enter workout duration in seconds (e.g., 30): ");
        
        if (seconds <= 0) {
            System.out.println("Invalid duration.");
            return;
        }

        System.out.println("Press Enter when ready to begin!");
        scanner.nextLine(); // Wait for Enter

        // Simple countdown
        for (int i = seconds; i > 0; i--) {
            System.out.println("Time remaining: " + i + "s");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("TIME'S UP! Workout completed!");
        
        // Convert to minutes (minimum 1)
        int minutes = Math.max(1, seconds / 60);
        recordWorkout(minutes, "Timed Workout");
    }

    private static void recordWorkout(int duration, String type) {
        totalWorkouts++;
        totalMinutes += duration;
        
        LocalDate today = LocalDate.now();
        if (lastWorkoutDate == null) {
            streak = 1;
        } else {
            if (today.equals(lastWorkoutDate)) {
                // Same day → streak unchanged
            } else if (today.equals(lastWorkoutDate.plusDays(1))) {
                streak++;
            } else {
                streak = 1;
            }
        }
        lastWorkoutDate = today;
        history.add(new Workout(today, duration, type));
    }

    private static void viewStats() {
        System.out.println("\nWORKOUT STATISTICS");
        System.out.println("===================================");
        System.out.println("Total Workouts: " + totalWorkouts);
        System.out.println("Total Time: " + totalMinutes + " minutes");
        System.out.println("Average Duration: " + 
                          (totalWorkouts == 0 ? 0 : totalMinutes / totalWorkouts) + " minutes");
        System.out.println("Current Streak: " + streak + " days");
        
        if (!history.isEmpty()) {
            Workout lastWorkout = history.get(history.size() - 1);
            System.out.println("Last Workout: " + lastWorkout);
        }
    }

    private static void viewHistory() {
        System.out.println("\nWORKOUT HISTORY");
        System.out.println("===================================");
        
        if (history.isEmpty()) {
            System.out.println("No workouts recorded yet.");
            System.out.println("Start your first workout!");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }

        System.out.println("\nTotal: " + history.size() + " workouts recorded");
    }

    private static void resetData() {
        System.out.print("\nAre you sure you want to reset all data? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            totalWorkouts = 0;
            totalMinutes = 0;
            streak = 0;
            lastWorkoutDate = null;
            history.clear();
            System.out.println("All data has been reset!");
        } else {
            System.out.println("Reset cancelled.");
        }
    }

    private static void displayGoodbye() {
        System.out.println("\nThanks for using Workout Tracker!");
        System.out.println("Keep up the great work!");
        System.out.println("Your progress: " + totalWorkouts + " workouts, " + 
                          totalMinutes + " minutes total");
        System.out.println("Stay strong!");
    }
}

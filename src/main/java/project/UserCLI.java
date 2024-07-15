package project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UserCLI {

    private static final String BASE_URL = "http://localhost:8080";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Register User");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            int choice = getIntInput();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter account type (integer):");
        int accountType = getIntInput();

        String jsonInputString = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"accountType\":%d}",
                username, password, firstName, lastName, accountType
        );

        sendPostRequest("/users/register", jsonInputString);
    }

    private static void loginUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        String jsonInputString = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\"}",
                username, password
        );

        boolean success = sendPostRequest("/users/login", jsonInputString);
        if (success) {
            boolean loggedIn = true;
            while (loggedIn) {
                loggedIn = displayMenu();
            }
        }
    }

    private static boolean sendPostRequest(String path, String jsonInputString) {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            if (code == 200) {
                System.out.println("Operation successful.");
                return true;
            } else {
                System.out.println("Operation failed: HTTP " + code);
                return false;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    private static boolean displayMenu() {
        System.out.println("---- Dummy Menu ----");
        System.out.println("1. Option A");
        System.out.println("2. Option B");
        System.out.println("3. Logout");
        int choice = getIntInput();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
            case 2:
                System.out.println("Option " + choice + " selected.");
                break;
            case 3:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option, please try again.");
        }
        return true;
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid input. Please enter an integer.");
            scanner.next(); // Consume the non-integer input
        }
        int input = scanner.nextInt();
        return input;
    }
}
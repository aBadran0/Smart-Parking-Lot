package com.simplews404.Simple.WS.CMP404;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;


public class UserCLI {

    private static final String BASE_URL = "https://webapp-usersapp.azurewebsites.net/users";
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Register User");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            int choice = getIntInput();
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
    
    private static String extractPasswordFromResponse(String jsonResponse) {
        JSONObject user = new JSONObject(jsonResponse);
        return user.optString("password", ""); // This will return an empty string if the password field is not found.
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

        try {
            URL url = new URL(BASE_URL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            if (code == 200 || code == 201) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("Registration failed: HTTP " + code);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void loginUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            URL url = new URL(BASE_URL + "/" + username);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int code = con.getResponseCode();
            if (code == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Assume the server sends back a user object with a password field
                // Here you'd parse the JSON to extract the password and compare it
                String serverPassword = extractPasswordFromResponse(response.toString()); // You need to implement this method based on your JSON structure

                if (serverPassword.equals(password)) {
                    System.out.println("Login Successful!");
                    boolean loggedIn = true;
                    while (loggedIn) {
                        loggedIn = displayMenu();
                    }
                } else {
                    System.out.println("Login failed: Incorrect password.");
                }
            } else {
                System.out.println("Login failed: User not found or error occurred.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static boolean displayMenu() {
        System.out.println("---- Dummy Menu ----");
        System.out.println("1. Option A");
        System.out.println("2. Option B");
        System.out.println("3. Logout");
        int choice = getIntInput();
        scanner.nextLine(); // Consume the rest of the line
        switch (choice) {
            case 1:
            case 2:
                // Placeholder for real functionality
                break;
            case 3:
                return false; // Log out and return to the main menu
            default:
                System.out.println("Invalid option, please try again.");
        }
        return true; // Stay in the dummy menu
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid input. Please enter an integer.");
            scanner.next(); // Consume the non-integer input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return input;
    }
}
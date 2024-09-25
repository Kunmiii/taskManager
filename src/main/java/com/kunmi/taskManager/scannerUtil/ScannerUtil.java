package com.kunmi.taskManager.scannerUtil;

import lombok.NoArgsConstructor;

import java.util.Scanner;

@NoArgsConstructor
public class ScannerUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String userInput) {
        System.out.print(userInput);
        return scanner.nextLine();
    }

    public static int getInt(String userInput) {
        int userValue;
        while (true) {
            System.out.print(userInput);
            try {
                userValue = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer");
            }
        }
        return userValue;
    }

    public static void closeScanner() {
        scanner.close();
    }

}

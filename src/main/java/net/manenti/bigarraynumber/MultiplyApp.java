package net.manenti.bigarraynumber;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Entry point for the to multiply integers without using multiplication operator.
 */

public class MultiplyApp {

    private static final Logger LOG = Logger.getLogger(MultiplyApp.class.getName());

    public static void main(String[] args) {
        // --- Prompt user for configuration ---
        Scanner scanner = new Scanner(System.in);

        System.out.print("Specify the first number: ");
        String firstNumber = scanner.nextLine().trim();
        int[] firstNumberDigits = new int[firstNumber.length()];
        for (int i = 0; i < firstNumber.length(); i++) {
            char  c = firstNumber.charAt(firstNumber.length() - 1 - i);
            if (c < '0' || c > '9') {
                System.out.println("Invalid input. Please enter a non-negative integer.");
                return;
            }
            firstNumberDigits[i] = c - '0';
        }
        var first = new BigArrayNumber(firstNumberDigits);

        System.out.print("Specify the second number: ");
        String secondNumber = scanner.nextLine().trim();
        int[] secondNumberDigits = new int[secondNumber.length()];
        for (int i = 0; i < secondNumber.length(); i++) {
            char  c = secondNumber.charAt(secondNumber.length() - 1 - i);
            if (c < '0' || c > '9') {
                System.out.println("Invalid input. Please enter a non-negative integer.");
                return;
            }
            secondNumberDigits[i] = c - '0';
        }
        var second = new BigArrayNumber(secondNumberDigits);

        var resultBigArrayNumber = first.multiply(second);
        var result = "";
        for (int i = resultBigArrayNumber.getDigits().length - 1; i >= 0; i--) {
            result += resultBigArrayNumber.getDigits()[i];
        }

        LOG.info("Result of : " + firstNumber + " x " + secondNumber + " is " + result);

    }

}

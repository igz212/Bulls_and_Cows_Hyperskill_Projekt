package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String randomSecretCode = prepareSecretCode();
        bullsAndCowsGame(randomSecretCode);


    }

    private static String prepareSecretCode() {
        int length = 0;
        int possibleSymbolsAmount = 0;
        int digits = 0;
        int alphabets = 0;
        Scanner scanner;
        scanner = new Scanner(System.in);
        System.out.println("Input the length of the Secret code:");
        if (!scanner.hasNextInt()) {
            System.out.printf("Error: \"%s\" isn't a valid number.", scanner.nextLine());
            System.exit(0);
        } else {
            length = scanner.nextInt();
        }
        System.out.println("Input the number of possible symbols in the code:");
        if (!scanner.hasNextInt()) {
            System.out.printf("Error: \"%s\" isn't a valid number.", scanner.nextLine());
            System.exit(0);
        } else {
            possibleSymbolsAmount = scanner.nextInt();
        }

        if (length > possibleSymbolsAmount) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, possibleSymbolsAmount);
            System.exit(0);
        }
        if (length < 1) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d.", length);
            System.exit(0);
        }
        if (possibleSymbolsAmount > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        } else if (possibleSymbolsAmount <= 10) {
            digits = possibleSymbolsAmount;
            //alphabets = 0;
        } else {
            digits = 10;
            alphabets = possibleSymbolsAmount - 10;
        }


        return generateSecretCode(length, digits, alphabets);

    }

    private static String generateSecretCode(int length,int digits, int alphabets) {
        StringBuilder randomSecretCode = new StringBuilder();
        StringBuilder digitsList = new StringBuilder("0123456789").delete(digits, 10);
        StringBuilder alphabetsList = new StringBuilder();
        if (alphabets > 0) {
            for (int i = 0; i < alphabets; i++) {
                alphabetsList.append((char) (i + 97));
            }
        }
        StringBuilder possibleSymbols = new StringBuilder(digitsList.toString() + alphabetsList.toString());

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(possibleSymbols.length());
            randomSecretCode.append(possibleSymbols.charAt(index));
            possibleSymbols.deleteCharAt(index);
        }
        preparationMessage(digits, alphabets);
        return randomSecretCode.toString();
    }

    private static void preparationMessage(int digits, int alphabets) {
        String stars = "*";
        stars = stars.repeat(digits + alphabets);

        if (alphabets == 0) {
            System.out.printf("The secret is prepared: %s (%d-%d).\n", stars, 0, digits - 1);
        } else if (alphabets == 1) {
            System.out.printf("The secret is prepared: %s (%d-%d, %s).\n", stars, 0, 10, 'a');
        }else {
            char startCharacter = 97; // ASCII Code as a
            char endCharacter = (char) (97 + alphabets - 1); // maximum 122 as z
            System.out.printf("The secret is prepared: %s (%d-%d, %s-%s).\n", stars, 0, 10, startCharacter, endCharacter);
        }

    }



    private static void bullsAndCowsGame(String randomSecretCode) {
        System.out.println("Okay, let's start a game!");
        String secretCode = String.valueOf(randomSecretCode);
        String guess;
        int[] gameResults ;
        Scanner scanner = new Scanner(System.in);

        boolean continueGameCondition = true;
        for (int turnCounter = 1; continueGameCondition; turnCounter++) {
            System.out.printf("Turn %d:\n", turnCounter);
            guess = scanner.next();

             gameResults = checkBullsAndCows(guess, secretCode);
             continueGameCondition = checkResults(gameResults, secretCode.length());
        }
    }

    private static boolean checkResults(int[] gameResults, int secretCodeLength) {
        int bulls = gameResults[0];
        int cows  = gameResults[1];
        String pluralBulls = "";
        String pluralCows = "";
        if (bulls > 2) {
            pluralBulls = "s";
        }
        if (cows > 2) {
            pluralCows = "s";
        }

        System.out.print("Grade: ");

        if (bulls == secretCodeLength) {
            System.out.printf("%d bull%s\nCongratulations! You guessed the secret code.\n", bulls, pluralBulls);
            return false;
        } else if (bulls == 0 && cows == 0){
            System.out.println("None.");
        } else if (bulls == 0) {
            System.out.printf("%d cow%s\n", cows, pluralCows);
        } else {
            System.out.printf("%d bull%s", bulls, pluralBulls);
            if (cows > 0) {
                System.out.printf("and %d cow%s\n", cows, pluralCows);
            }
            if (cows == 0) {
                System.out.print("\n");
            }
        }

        return true;
    }


    private static int[] checkBullsAndCows(String guess, String secretCode) {
        int[] cowsAndBulls = new int[2];
        int bulls = 0;
        int cows  = 0;
        for (int index = 0; index < secretCode.length(); index++) {
            if (guess.charAt(index) == secretCode.charAt(index)) {
                bulls++;
            } else if (secretCode.contains(String.valueOf(guess.charAt(index)))) {
                cows++;
            }
        }
        cowsAndBulls[0] = bulls;
        cowsAndBulls[1] = cows;
        return cowsAndBulls;
    }

    private static Long createPositiveRandomNumber(long seed, int requestedLength) {

        StringBuilder str = new StringBuilder(String.valueOf(seed)).reverse();
        makeFirstDigitNonZero(str);
        makeUniqueCode(str);
        makeCodeEnoughLong(str, requestedLength);
        return Long.valueOf(String.valueOf(str));

    }

    private static void makeFirstDigitNonZero(StringBuilder str) {
        if (str.charAt(0) == '0') {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '0') {
                    str.replace(0,1,String.valueOf(str.charAt(i)));
                    str.replace(i,i + 1,"0");
                    break;
                }
            }
        }
    }

    private static void makeCodeEnoughLong(StringBuilder str, int requestedLength) {
        int strLength = str.length();
        if (strLength > requestedLength) {
            str.delete(requestedLength - 1, strLength - 1);
        } else if (strLength < requestedLength) {
            int neededChars = strLength - requestedLength;
            StringBuilder tempStrBld = new StringBuilder(String.valueOf(System.nanoTime())).reverse();
            makeUniqueCode(tempStrBld);
            for (int i = 0; i < tempStrBld.length() && neededChars >0; i++) {
                for (int j = 0; j < strLength; j++) {
                    if (str.charAt(i) == tempStrBld.charAt(j)) {
                        break;
                    }
                    str.append(tempStrBld.charAt(i));
                    neededChars--;
                }
            }
        }
    }

    private static void makeUniqueCode(StringBuilder str) {
        StringBuilder tempStrBld = new StringBuilder(String.valueOf(str.charAt(0)));
        boolean unique;
        for (int i = 1; i < str.length(); i++) {
            unique = true;
            for (int j = 0; j < tempStrBld.length(); j++) {
                if (str.charAt(i) == tempStrBld.charAt(j)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                tempStrBld.append(str.charAt(i));
            }
        }
        str.replace(0, str.length(),tempStrBld.toString());
    }
}

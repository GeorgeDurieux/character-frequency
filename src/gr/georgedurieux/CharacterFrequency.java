package gr.georgedurieux;

import java.io.*;
import java.util.Arrays;

public class CharacterFrequency {

    public static void main(String[] args) {
        String inputFilePath = "src/gr/georgedurieux/ex3/input.txt";
        String outputFilePath = "src/gr/georgedurieux/ex3/output.txt";

        Object[][] charArray = initializeCharArray();
        int totalCharacters = processInputFile(inputFilePath, charArray);

        if (totalCharacters == 0) {
            System.err.println("The input file is empty or contains only whitespace.");
            return;
        }

        sortCharArray(charArray);
        writeOutputFile(outputFilePath, charArray, totalCharacters);
    }

    private static Object[][] initializeCharArray() {
        Object[][] charArray = new Object[128][2];
        for (int i = 0; i < charArray.length; i++) {
            charArray[i][0] = null;
            charArray[i][1] = 0;
        }
        return charArray;
    }

    private static int processInputFile(String filePath, Object[][] charArray) {
        int totalCharacters = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;

                if (Character.isWhitespace(character)) {
                    continue;
                }

                totalCharacters++;
                updateCharArray(charArray, character);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return totalCharacters;
    }

    private static void updateCharArray(Object[][] charArray, char character) {
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i][0] == null) {
                charArray[i][0] = character;
                charArray[i][1] = 1;
                break;
            } else if (charArray[i][0].equals(character)) {
                charArray[i][1] = (int) charArray[i][1] + 1;
                break;
            }
        }
    }

    private static void sortCharArray(Object[][] charArray) {
        Arrays.sort(charArray, (a, b) -> {
            if (a[0] == null && b[0] == null) return 0;
            if (a[0] == null) return 1;
            if (b[0] == null) return -1;

            int freqA = (int) a[1];
            int freqB = (int) b[1];

            if (freqA != freqB) {
                return Integer.compare(freqB, freqA);
            }

            return Character.compare((char) a[0], (char) b[0]);
        });
    }

    private static void writeOutputFile(String filePath, Object[][] charArray, int totalCharacters) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Character | Frequency | Percentage\n");
            writer.write("---------------------------------\n");
            for (Object[] entry : charArray) {
                if (entry[0] != null) {
                    char character = (char) entry[0];
                    int frequency = (int) entry[1];
                    double percentage = (frequency / (double) totalCharacters) * 100;

                    writer.write(String.format("%-9s | %-9d | %.2f%%%n", character, frequency, percentage));
                }
            }
            System.out.println("Output successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
}

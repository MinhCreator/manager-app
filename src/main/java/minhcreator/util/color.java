package minhcreator.util;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * color util class provide medthod for random color generator
 *
 * @author MinhCreatorVN
 */
public class color {
    private static final Random RAND = new Random();

    public static Color generateRandomColorRGB() {
        Random rand = RAND;
        // Generate random integers between 0 and 255 for RGB values
        int r = rand.nextInt(256); // nextInt(256) returns a value from 0 (inclusive) to 256 (exclusive)
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);

        // Create and return a new Color object
        return new Color(r, g, b);
    }

    public static Color generateLightColor() {
        Random rand = RAND;
        int r = rand.nextInt(128) + 128; // Range 128-255
        int g = rand.nextInt(128) + 128;
        int b = rand.nextInt(128) + 128;
        return new Color(r, g, b);
    }

    public static Color generateDarkColor() {
        Random rand = RAND;
        int r = rand.nextInt(128); // Range 0-127
        int g = rand.nextInt(128);
        int b = rand.nextInt(128);
        return new Color(r, g, b);
    }

    public static String generateRandomHexColor() {
        Random rand = RAND;
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        // Use String.format to convert the integers to a hexadecimal string
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public static List<String> ReadColFromFile() {
        String filePath = "src/main/resources/minhcreator/colorPalette/color.txt";
        // The try-with-resources statement automatically closes the BufferedReader and FileReader
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> listColor = new ArrayList<>();
            String line;
//            System.out.println("Reading file content:");
            // Read file line by line until a line is null (end of file)
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                listColor.add(line);
            }
            return listColor;
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void WriteColToFile(int GeneratorRange) {
        String filePath = "src/main/resources/minhcreator/colorPalette/color.txt";
        // The try-with-resources statement automatically closes the BufferedWriter and FileWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int index = 0; index < GeneratorRange; index++) {
                writer.write(generateRandomHexColor() + "\n");
            }

            System.out.println("Successfully wrote to the file: " + filePath);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // testing color code generator
    public static void main(String[] arg) {
//        WriteColToFile(30);
//
//        System.out.println(ReadColFromFile());
//
////        Color col = new Color(Color.decode("#ae1edc"));
    }
}
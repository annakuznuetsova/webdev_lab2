package org.example;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static void saveToFile(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            saveToWriter(writer, data);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<String> loadFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            lines = loadFromReader(reader);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    public static void clearFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.print("");
        } catch (IOException e) {
            System.out.println("Error clearing data in " + fileName + ": " + e.getMessage());
        }
    }

    public static void appendToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending data in: " + e.getMessage());
        }
    }

    public static void saveToWriter(BufferedWriter writer, String data) throws IOException {
        writer.write(data);
        writer.newLine();
    }

    public static List<String> loadFromReader(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
}

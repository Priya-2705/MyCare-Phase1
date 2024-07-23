package com.humber.project;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtNBinary {

    // Load patient data from a text file
    public static List<String> loadPatientDataFromTextFile() {
        List<String> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                patients.add(line);
            }
        } catch (IOException e) {
            showErrorDialog("Error reading patient data", "An error occurred while reading patient data from the text file.");
        }
        return patients;
    }

    // Load patient data from a binary file
    public static List<String> loadPatientDataFromBinaryFile() {
        List<String> patients = new ArrayList<>();
        File file = new File("patients.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                patients = (List<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                showErrorDialog("Error reading patient data", "An error occurred while reading patient data from the binary file.");
            }
        }
        return patients;
    }

    // Load appointment data from a text file
    public static List<String> loadAppointmentDataFromTextFile() {
        List<String> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointments.add(line);
            }
        } catch (IOException e) {
            showErrorDialog("Error reading appointment data", "An error occurred while reading appointment data from the text file.");
        }
        return appointments;
    }

    // Load appointment data from a binary file
    public static List<String> loadAppointmentDataFromBinaryFile() {
        List<String> appointments = new ArrayList<>();
        File file = new File("appointments.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                appointments = (List<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                showErrorDialog("Error reading appointment data", "An error occurred while reading appointment data from the binary file.");
            }
        }
        return appointments;
    }

    // Show an error dialog
    private static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

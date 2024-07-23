package com.humber.project;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.List;

public class WriteTxtNBinary {

    // Save patient data to a text file
    public static void savePatientDataToTextFile(List<String> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("patients.txt"))) {
            for (String patientData : patients) {
                writer.write(patientData);
                writer.newLine();
            }
        } catch (IOException e) {
            showErrorDialog("Error writing patient data", "An error occurred while writing patient data to the text file.");
        }
    }

    // Save patient data to a binary file
    public static void savePatientDataToBinaryFile(List<String> patients) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("patients.dat"))) {
            oos.writeObject(patients);
        } catch (IOException e) {
            showErrorDialog("Error writing patient data", "An error occurred while writing patient data to the binary file.");
        }
    }

    // Save appointment data to a text file
    public static void saveAppointmentDataToTextFile(List<String> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointments.txt"))) {
            for (String appointmentData : appointments) {
                writer.write(appointmentData);
                writer.newLine();
            }
        } catch (IOException e) {
            showErrorDialog("Error writing appointment data", "An error occurred while writing appointment data to the text file.");
        }
    }

    // Save appointment data to a binary file
    public static void saveAppointmentDataToBinaryFile(List<String> appointments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("appointments.dat"))) {
            oos.writeObject(appointments);
        } catch (IOException e) {
            showErrorDialog("Error writing appointment data", "An error occurred while writing appointment data to the binary file.");
        }
    }

    // Show an error dialog if an exception occurs during file operations
    private static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

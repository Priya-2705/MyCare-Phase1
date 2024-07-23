package com.humber.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Application {

    private static Dashboard instance;
    private ListView<String> patientListView = new ListView<>();
    private ListView<String> appointmentListView = new ListView<>();

    // Constructor to initialize the Dashboard instance
    public Dashboard() {
        instance = this;
    }

    // Get the singleton instance of Dashboard
    public static Dashboard getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setStyle("-fx-background-color: #009688;"); // Teal background color

        // Create and style buttons
        Button managePatientsButton = createStyledButton("Manage Patients");
        Button manageAppointmentsButton = createStyledButton("Manage Appointments");
        Button logoutButton = createStyledButton("Logout");

        // Set button actions
        managePatientsButton.setOnAction(e -> showPatientRecordsForm(primaryStage));
        manageAppointmentsButton.setOnAction(e -> showAppointmentForm(primaryStage));
        logoutButton.setOnAction(e -> primaryStage.close());

        // Style labels
        Label patientLabel = createStyledLabel("Patient Records:");
        Label appointmentLabel = createStyledLabel("Appointments:");

        // Style list views
        patientListView.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: #004D40; -fx-border-width: 2px;");
        appointmentListView.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: #004D40; -fx-border-width: 2px;");

        vbox.getChildren().addAll(
            managePatientsButton,
            manageAppointmentsButton,
            logoutButton,
            patientLabel,
            patientListView,
            appointmentLabel,
            appointmentListView
        );

        Scene scene = new Scene(vbox, 700, 400); // Set to match the size of PatientRecordsForm
        primaryStage.setScene(scene);
        primaryStage.show();

        updateDashboard();
    }

    // Create a styled button with hover effects
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
        button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #E64A19; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"));
        return button;
    }

    // Create a styled label with bold and white text
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Bold font
        label.setTextFill(Color.WHITE); // White text color
        return label;
    }

    // Show Patient Records Form
    private void showPatientRecordsForm(Stage primaryStage) {
        new PatientRecordsForm().start(primaryStage);
    }

    // Show Appointment Form
    private void showAppointmentForm(Stage primaryStage) {
        new AppointmentForm().start(primaryStage);
    }

    // Update Dashboard with patient and appointment data
    public void updateDashboard() {
        patientListView.getItems().clear();
        appointmentListView.getItems().clear();

        List<String> patients = loadDataFromTextFile("patients.txt");
        patientListView.getItems().add(formatPatientHeader());
        patientListView.getItems().addAll(formatPatientData(patients));

        List<String> appointments = loadDataFromTextFile("appointments.txt");
        appointmentListView.getItems().add(formatAppointmentHeader());
        appointmentListView.getItems().addAll(formatAppointmentData(appointments));
    }

    // Format header for patient data
    private String formatPatientHeader() {
        return String.format("%-18s  %-15s  %-38s  %-27s  %-55s", "Name", "Age", "Contact", "DOB", "Medical History");
    }

    // Format patient data for display
    private List<String> formatPatientData(List<String> data) {
        List<String> formattedData = new ArrayList<>();
        for (String line : data) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String formattedLine = String.format("%-20s  %-15s  %-30s  %-25s  %-55s", parts[0], parts[1], parts[2], parts[4], parts[3]);
                formattedData.add(formattedLine);
            }
        }
        return formattedData;
    }

    // Format header for appointment data
    private String formatAppointmentHeader() {
        return String.format("%-30s  %-50s  %-20s  %-50s", "Name", "Date", "Time", "Reason");
    }

    // Format appointment data for display
    private List<String> formatAppointmentData(List<String> data) {
        List<String> formattedData = new ArrayList<>();
        for (String line : data) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                String formattedLine = String.format("%-30s  %-45s  %-20s  %-50s", parts[0], parts[1], parts[2], parts[3]);
                formattedData.add(formattedLine);
            }
        }
        return formattedData;
    }

    // Load data from a text file
    private List<String> loadDataFromTextFile(String fileName) {
        List<String> data = new ArrayList<>();
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.humber.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentForm extends Application {

    private List<String> appointments;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Appointment Form");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Title Label
        Label titleLabel = new Label("Appointment Form");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold font
        titleLabel.setTextFill(Color.web("#FFFFFF")); // White color for title

        // Form Labels
        Label patientNameLabel = new Label("Patient Name:");
        Label dateLabel = new Label("Date:");
        Label timeLabel = new Label("Time:");
        Label detailsLabel = new Label("Details:");

        // Styling Labels
        styleLabel(patientNameLabel);
        styleLabel(dateLabel);
        styleLabel(timeLabel);
        styleLabel(detailsLabel);

        // Form Controls
        TextField patientNameField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        TextArea detailsArea = new TextArea();
        detailsArea.setPrefRowCount(5);
        detailsArea.setWrapText(true);

        // Placeholder text and validation
        timeField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9:]*")) {
                return change;
            } else {
                return null;
            }
        }));

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Adding controls to GridPane
        grid.add(patientNameLabel, 0, 0);
        grid.add(patientNameField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(timeLabel, 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(detailsLabel, 0, 3);
        grid.add(detailsArea, 1, 3);

        // Buttons
        Button submitButton = new Button("Submit");
        Button updateButton = new Button("Update");
        Button backButton = new Button("Back to Dashboard");

        // Apply styling to buttons
        styleButton(submitButton);
        styleButton(updateButton);
        styleButton(backButton);

        // Buttons layout
        HBox buttonBox = new HBox(20, submitButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER); // Center align buttons
        buttonBox.setPadding(new Insets(10, 0, 10, 0)); // Padding around buttons

        // Adding buttons to GridPane
        grid.add(buttonBox, 0, 4, 2, 1); // Span across two columns
        grid.add(backButton, 0, 5, 2, 1); // Span across two columns

        // Button Actions
        submitButton.setOnAction(e -> handleSubmit(patientNameField.getText(), datePicker.getValue(), timeField.getText(), detailsArea.getText()));
        updateButton.setOnAction(e -> handleUpdate(patientNameField.getText(), datePicker.getValue(), timeField.getText(), detailsArea.getText()));
        backButton.setOnAction(e -> showDashboard(primaryStage));

        // Load existing appointment data
        appointments = ReadTxtNBinary.loadAppointmentDataFromBinaryFile();

        // Root Pane
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(10));
        rootPane.setStyle("-fx-background-color: #009688;"); // Teal background color
        rootPane.add(titleLabel, 0, 0);
        rootPane.add(grid, 0, 1);

        // Scene Setup
        Scene scene = new Scene(rootPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Styling for Labels
    private void styleLabel(Label label) {
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        label.setTextFill(Color.web("#FFFFFF")); // White color
    }

    // Styling for Buttons
    private void styleButton(Button button) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"); // Deep Orange background
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #E64A19; -fx-text-fill: white;")); 
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;")); 
    }

    // Handle form submission
    private void handleSubmit(String patientName, LocalDate date, String time, String details) {
        if (!isValidName(patientName)) {
            showErrorDialog("Invalid Name", "Name should only contain letters.");
            return;
        }
        if (!isValidTime(time)) {
            showErrorDialog("Invalid Time", "Time should be in HH:MM 24-hour format.");
            return;
        }
        if (date == null || !isFutureDate(date)) {
            showErrorDialog("Invalid Date", "Date should be a future date.");
            return;
        }

        String appointmentData = patientName + "," + date + "," + time + "," + details;
        try {
            appointments.add(appointmentData);
            WriteTxtNBinary.saveAppointmentDataToTextFile(appointments);
            WriteTxtNBinary.saveAppointmentDataToBinaryFile(appointments);
            System.out.println("Appointment data saved.");
        } catch (Exception e) {
            showErrorDialog("Error", "An error occurred while saving appointment data.");
        }
    }

    // Handle form update
    private void handleUpdate(String patientName, LocalDate date, String time, String details) {
        if (!isValidName(patientName)) {
            showErrorDialog("Invalid Name", "Name should only contain letters.");
            return;
        }
        if (!isValidTime(time)) {
            showErrorDialog("Invalid Time", "Time should be in HH:MM 24-hour format.");
            return;
        }
        if (date == null || !isFutureDate(date)) {
            showErrorDialog("Invalid Date", "Date should be a future date.");
            return;
        }

        boolean updated = false;
        List<String> updatedAppointments = new ArrayList<>();
        for (String appointmentData : appointments) {
            String[] parts = appointmentData.split(",");
            if (parts[0].equals(patientName) && parts[1].equals(date.toString()) && parts[2].equals(time)) {
                updatedAppointments.add(patientName + "," + date + "," + time + "," + details);
                updated = true;
            } else {
                updatedAppointments.add(appointmentData);
            }
        }
        if (updated) {
            try {
                appointments = updatedAppointments;
                WriteTxtNBinary.saveAppointmentDataToTextFile(appointments);
                WriteTxtNBinary.saveAppointmentDataToBinaryFile(appointments);
                System.out.println("Appointment data updated.");
            } catch (Exception e) {
                showErrorDialog("Error", "An error occurred while updating appointment data.");
            }
        } else {
            System.out.println("No matching appointment found to update.");
        }
    }

    // Validation Methods
    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z]+");
    }

    private boolean isValidTime(String time) {
        return time != null && time.matches("([01]\\d|2[0-3]):[0-5]\\d");
    }

    private boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    // Show Dashboard
    private void showDashboard(Stage primaryStage) {
        new Dashboard().start(primaryStage);
    }

    // Show error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

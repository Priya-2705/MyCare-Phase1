package com.humber.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRecordsForm extends Application {

    private List<String> patients;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Patient Records");

        // Layout setup
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setStyle("-fx-background-color: #009688;");

        // Title Label
        Label titleLabel = new Label("Patient Records Form");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#FFFFFF"));

        // Form layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Form fields and labels
        Label nameLabel = createStyledLabel("Name:");
        Label ageLabel = createStyledLabel("Age:");
        Label contactLabel = createStyledLabel("Contact Info:");
        Label historyLabel = createStyledLabel("Medical History:");
        Label dobLabel = createStyledLabel("Date of Birth:");

        // Create and style form controls
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField contactField = new TextField();
        TextArea historyArea = new TextArea();
        historyArea.setPrefRowCount(5);
        historyArea.setWrapText(true);
        DatePicker dobPicker = new DatePicker();

        // Add controls to grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(ageLabel, 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(contactLabel, 0, 2);
        grid.add(contactField, 1, 2);
        grid.add(dobLabel, 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(historyLabel, 0, 4);
        grid.add(historyArea, 1, 4);

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
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));

        // Add buttons to the grid
        grid.add(buttonBox, 0, 5, 2, 1);
        grid.add(backButton, 0, 6, 2, 1);

        // Button actions
        submitButton.setOnAction(e -> handleSubmit(nameField.getText(), ageField.getText(), contactField.getText(), historyArea.getText(), dobPicker.getValue()));
        updateButton.setOnAction(e -> handleUpdate(nameField.getText(), ageField.getText(), contactField.getText(), historyArea.getText(), dobPicker.getValue()));
        backButton.setOnAction(e -> showDashboard(primaryStage));

        // Load patient data
        patients = ReadTxtNBinary.loadPatientDataFromBinaryFile();

        vbox.getChildren().addAll(titleLabel, grid);

        Scene scene = new Scene(vbox, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create and style labels
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#FFFFFF"));
        return label;
    }

    // Helper method to style buttons
    private void styleButton(Button button) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"); // Deep Orange background
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #E64A19; -fx-text-fill: white;")); 
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;")); 
    }

    // Handle form submission
    private void handleSubmit(String name, String age, String contact, String history, LocalDate dob) {
        if (!isValidName(name)) {
            showErrorDialog("Invalid Name", "Name should only contain letters.");
            return;
        }
        if (!isValidAge(age)) {
            showErrorDialog("Invalid Age", "Age should be a 1 or 2-digit integer.");
            return;
        }
        if (!isValidContact(contact)) {
            showErrorDialog("Invalid Contact Info", "Contact number should be in the format +1-nnn-nnn-nnnn.");
            return;
        }
        if (dob == null || dob.isAfter(LocalDate.now())) {
            showErrorDialog("Invalid Date", "Date of birth should be a past date.");
            return;
        }

        String patientData = name + "," + age + "," + contact + "," + history + "," + dob;
        try {
            patients.add(patientData);
            WriteTxtNBinary.savePatientDataToTextFile(patients);
            WriteTxtNBinary.savePatientDataToBinaryFile(patients);
            System.out.println("Patient data saved.");
        } catch (Exception e) {
            showErrorDialog("Error", "An error occurred while saving patient data.");
        }
    }

    // Handle form update
    private void handleUpdate(String name, String age, String contact, String history, LocalDate dob) {
        if (!isValidName(name)) {
            showErrorDialog("Invalid Name", "Name should only contain letters.");
            return;
        }
        if (!isValidAge(age)) {
            showErrorDialog("Invalid Age", "Age should be a 1 or 2-digit integer.");
            return;
        }
        if (!isValidContact(contact)) {
            showErrorDialog("Invalid Contact Info", "Contact number should be in the format +1-nnn-nnn-nnnn.");
            return;
        }
        if (dob == null || dob.isAfter(LocalDate.now())) {
            showErrorDialog("Invalid Date", "Date of birth should be a past date.");
            return;
        }

        boolean updated = false;
        List<String> updatedPatients = new ArrayList<>();
        for (String patientData : patients) {
            String[] parts = patientData.split(",");
            if (parts[0].equals(name)) {
                updatedPatients.add(name + "," + age + "," + contact + "," + history + "," + dob);
                updated = true;
            } else {
                updatedPatients.add(patientData);
            }
        }
        if (updated) {
            try {
                patients = updatedPatients;
                WriteTxtNBinary.savePatientDataToTextFile(patients);
                WriteTxtNBinary.savePatientDataToBinaryFile(patients);
                System.out.println("Patient data updated.");
            } catch (Exception e) {
                showErrorDialog("Error", "An error occurred while updating patient data.");
            }
        } else {
            System.out.println("No matching patient found to update.");
        }
    }

    // Validate methods
    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z]+");
    }

    private boolean isValidAge(String age) {
        return age != null && age.matches("\\d{1,2}");
    }

    private boolean isValidContact(String contact) {
        return contact != null && contact.matches("\\+1-\\d{3}-\\d{3}-\\d{4}");
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

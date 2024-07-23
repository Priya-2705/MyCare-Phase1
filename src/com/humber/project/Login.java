package com.humber.project;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create form elements
        // Heading label for the login form
        Label headingLabel = new Label("MyCare");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36)); 
        headingLabel.setTextFill(Color.web("#FFFFFF")); 
        headingLabel.setPadding(new Insets(60, 0, -20, 0)); 

        // Label and text field for username input
        Label userNameLabel = new Label("Username:");
        userNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
        userNameLabel.setTextFill(Color.web("#FFFFFF")); 
        
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        userNameField.setPrefWidth(200); 
        userNameField.setStyle("-fx-background-color: #E0E0E0;"); 

        // Label and password field for password input
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
        passwordLabel.setTextFill(Color.web("#FFFFFF")); 

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(200); 
        passwordField.setStyle("-fx-background-color: #E0E0E0;"); 

        // Login button with styling and hover effects
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
        loginButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"); 
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #E64A19; -fx-text-fill: white;")); 
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;")); 

        // Create layout for form with labels and fields beside each other
        GridPane formLayout = new GridPane();
        formLayout.setHgap(10); 
        formLayout.setVgap(20); 
        formLayout.setPadding(new Insets(20)); 
        formLayout.setAlignment(Pos.CENTER);

        // Add form elements to the grid
        formLayout.add(userNameLabel, 0, 0); 
        formLayout.add(userNameField, 1, 0); 

        formLayout.add(passwordLabel, 0, 1); 
        formLayout.add(passwordField, 1, 1); 

        formLayout.add(loginButton, 1, 2); 
        GridPane.setHalignment(loginButton, HPos.CENTER); 

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headingLabel); 
        BorderPane.setAlignment(headingLabel, Pos.CENTER); 

        mainLayout.setCenter(formLayout); 
        mainLayout.setStyle("-fx-background-color: #009688;"); 

        // Set button action
        loginButton.setOnAction(e -> handleLogin(userNameField.getText(), passwordField.getText(), primaryStage));

        // Create scene with consistent screen size and set the stage
        Scene scene = new Scene(mainLayout, 700, 400);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle login action
    private void handleLogin(String username, String password, Stage primaryStage) {
        // Check if the username and password match the required values
        if ((username.equals("Priya") || username.equals("Shubham") || username.equals("Devwrat")) && "123456".equals(password)) {
            Dashboard dashboard = new Dashboard();
            try {
                dashboard.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            showErrorDialog("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    // Show an error dialog if login fails
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}

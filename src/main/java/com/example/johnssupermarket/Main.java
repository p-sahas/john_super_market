package com.example.johnssupermarket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366, 800);
        //Connects styles.css file
        String css = Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm();
        scene.getStylesheets().add(css);

        // Set Application Icon
        try {
            //Logo file path src/main/resources/icons/
            Image appIcon = new Image(Objects.requireNonNull(getClass().getResource
                    ("/icons/logo.png")).toExternalForm());
            stage.getIcons().add(appIcon);
        }
        catch (NullPointerException e)
        {
            System.err.println("Error: Application icon 'logo.png' not found." +
                    "Please ensure it's in src/main/resources/icons/.");
        } catch (Exception e) {
            System.err.println("Warning: Could not load application icon: " + e.getMessage());
        }

        stage.setTitle("John's Super Market Inventory Management");
        stage.setScene(scene);

        MainController controller = fxmlLoader.<MainController>getController();
        stage.setOnCloseRequest(event -> {
            event.consume(); // Consume the event to allow custom exit handling
            controller.handleExit();
        });

        stage.show();
        // Run the initial low stock check Alert AFTER the UI is visible.
        Platform.runLater(controller::handleShowLowStock);

    }
    //Run main Method
    public static void main(String[] args) {
        launch(args);
    }
}

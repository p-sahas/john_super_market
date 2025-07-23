package com.example.johnssupermarket;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Paths;

public class ImageTableCell<S> extends TableCell<S, String> {
    private final ImageView imageView = new ImageView();

    public ImageTableCell() {
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
        setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(String imageFileName, boolean empty) {
        super.updateItem(imageFileName, empty);
        if (empty || imageFileName == null || imageFileName.isEmpty() || imageFileName.equals("null")) {
            imageView.setImage(null);
        } else {
            try {
                File file = Paths.get(/*InventoryManager.RESOURCES_DIR, */InventoryManager.IMAGE_DIR,
                        imageFileName).toFile();
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString(), true));
                } else {
                    System.err.println("Image file not found: " + file.getAbsolutePath());
                    imageView.setImage(null);
                }
            } catch (Exception e) {
                System.err.println("Error loading image for " + imageFileName + ": " + e.getMessage());
                imageView.setImage(null);
            }
        }
    }
}

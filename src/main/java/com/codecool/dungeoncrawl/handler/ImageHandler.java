package com.codecool.dungeoncrawl.handler;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageHandler {

    private Image createImageToMenu() throws FileNotFoundException {
        return new Image(new FileInputStream("src/main/resources/menu.jpeg"));
    }

    public Group setTheImage() throws FileNotFoundException {
        Image image = createImageToMenu();
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(550);
        imageView.setFitWidth(800);
        return new Group(imageView);
    }
}

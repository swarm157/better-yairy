package ru.swarm.dairy.yairy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.swarm.dairy.yairy.control.controllers.PasswordsController;
import ru.swarm.dairy.yairy.model.DataProxy;


import java.io.IOException;
import java.security.Security;

public class Boot extends Application {

    @Override
    public void init() {

    }
    @Override
    public void start(Stage stage) {
        DataProxy.load();
        try {
            PasswordsController.Model.load();
        } catch (Exception e) {
            DataProxy.log(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("main-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 800);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Yairy");
        stage.setScene(scene);
        //stage.getIcons().add(new Image("resources/icon.png"));
        Application.setUserAgentStylesheet(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        DataProxy.save();
        PasswordsController.Model.save();
        System.exit(0);
    }
}
package ru.swarm.dairy.yairy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.swarm.dairy.yairy.model.DataProxy;

import java.io.IOException;
import java.security.Security;

public class Boot extends Application {
    @Override
    public void init() {

    }
    @Override
    public void start(Stage stage) throws IOException {
        DataProxy.load();
        FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        stage.setTitle("Yairy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        DataProxy.save();
    }
}
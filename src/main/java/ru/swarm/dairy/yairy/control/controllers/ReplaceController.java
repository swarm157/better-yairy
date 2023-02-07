package ru.swarm.dairy.yairy.control.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.swarm.dairy.yairy.control.history.History;
import ru.swarm.dairy.yairy.control.history.subjects.Replaced;

public class ReplaceController {
    @FXML
    public TextField from;
    @FXML
    public TextField to;
    @FXML
    public Button cancel;
    @FXML
    public Button ok;
    @FXML
    public CheckBox globalReplacing;

    public static Stage stage = new Stage();
    public static int index;

    @FXML
    public void initialize() {
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (globalReplacing.isSelected()) index = -1;
                History.make(new Replaced(MainController.getPass(), from.getText(), to.getText(), index));
                MainController.mainController.reloadPage();
                stage.close();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
    }
}

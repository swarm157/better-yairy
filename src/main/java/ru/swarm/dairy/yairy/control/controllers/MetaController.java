package ru.swarm.dairy.yairy.control.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.swarm.dairy.yairy.control.history.subjects.Changed;
import ru.swarm.dairy.yairy.control.history.subjects.ChangedFake;
import ru.swarm.dairy.yairy.control.history.subjects.ChangedName;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;

import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.swarm.dairy.yairy.control.history.History.make;

public class MetaController {

    @FXML
    public TextField name;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        MetaController.stage = stage;
    }

    public static Stage stage;
    public static Page getPage() {
        return page;
    }

    public static void setPage(Page page) {
        MetaController.page = page;
    }

    private static Page page;

    public static int getPageNum() {
        return pageNum;
    }

    public static void setPageNum(int pageNum) {
        MetaController.pageNum = pageNum;
    }

    private static int pageNum;
    @FXML
    public Label creationTime;
    @FXML
    public Label uid;
    @FXML
    public Label textVersion;
    @FXML
    public Label version;
    @FXML
    public Label author;
    @FXML
    private TextArea area;
    @FXML
    private Button ok;
    @FXML
    private Button cancel ;

    @FXML
    public void initialize() {
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                make(new ChangedFake(page.getFake(), area.getText(), getPageNum()));
                if (!name.getText().equals(page.getName())) {
                    make(new ChangedName(page.getName(), name.getText(), getPageNum()));
                }
                if (!area.getText().equals(page.getFake())) {
                    make(new ChangedFake(page.getFake(), area.getText(), getPageNum()));
                }
                stage.close();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        area.setText(page.getFake());
        name.setText(page.getName());
        uid.setText(page.getUID());
        version.setText(String.valueOf(page.getVersion()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(page.getCreationDate());
        creationTime.setText(formatter.format(date));
        date = new Date(page.getTextVersion());
        textVersion.setText(formatter.format(date));
        author.setText(page.getCreator());
    }

}

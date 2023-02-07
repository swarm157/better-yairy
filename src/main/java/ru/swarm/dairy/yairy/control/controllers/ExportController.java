package ru.swarm.dairy.yairy.control.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.BookV1;
import ru.swarm.dairy.yairy.model.data.page.Page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExportController {

    @FXML
    public static Stage stage = new Stage();
    @FXML
    public Button ok;
    @FXML
    public Button cancel;
    @FXML
    public CheckBox separatelySaving;
    @FXML
    public TextField path;
    @FXML
    public ListView list;

    @FXML
    public void initialize() {

        list.setEditable(false);
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> items = FXCollections.observableArrayList ();
        for (int i = 0; i < DataProxy.getBook().getPages().size(); i++) {
            Page page = DataProxy.getBook().getPages().get(i);
            if (page.getName()==null||page.getName().equals("")) {
                items.add("page: "+i);
            } else {
                items.add(page.getName());
            }
        }
        list.setItems(items);

        //scroll.contentProperty().
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                var selected = list.getSelectionModel().getSelectedIndices();
                if (!path.getText().equals(""))
                    if (separatelySaving.isSelected()) {
                        File dir = new File(path.getText());
                        if(!dir.exists())
                            dir.mkdirs();
                        if (dir.isDirectory()) {
                            for (Object i : selected) {
                                Integer index = (Integer) i;
                                Page page = DataProxy.getBook().getPages().get(index);
                                try {
                                    File file;
                                    if (page.getName()==null||page.getName().equals("")) {
                                        file = new File(dir, "page"+i+".page");
                                    } else {
                                        file = new File(dir, page.getName()+".page");
                                    }
                                    FileUtils.touch(file);
                                    FileUtils.writeStringToFile(file, gson.toJson(page));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                    } else {
                        File file = new File(path.getText()+".book");
                        BookV1 bookV1 = new BookV1(file.getName());
                        for (Object i : selected) {
                            Integer index = (Integer) i;
                            bookV1.getPages().add(DataProxy.getBook().getPages().get(index));
                        }
                        try {
                            FileUtils.touch(file);
                            bookV1.prepareToGson();
                            FileUtils.writeStringToFile(file, gson.toJson(bookV1));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
    }
}

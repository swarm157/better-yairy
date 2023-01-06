package ru.swarm.dairy.yairy.control.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.swarm.dairy.yairy.Boot;
import ru.swarm.dairy.yairy.control.history.History;
import ru.swarm.dairy.yairy.control.history.subjects.Changed;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;
import ru.swarm.dairy.yairy.model.data.page.PageV1;

import java.io.IOException;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static ru.swarm.dairy.yairy.control.history.History.*;

public class MainController {
    @FXML
    private TextArea area;
    @FXML
    private Menu actions;
    @FXML
    private Button undo;
    @FXML
    private Button make;
    @FXML
    private MenuItem exitWithoutSaving;
    @FXML
    private MenuItem reloadFile;
    @FXML
    private MenuItem saveChanges;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem update;
    @FXML
    private MenuItem export;
    @FXML
    private MenuItem add;
    @FXML
    private MenuItem remove;
    @FXML
    private MenuItem delete;
    @FXML
    private MenuItem replace;
    @FXML
    private MenuItem about;
    @FXML
    private MenuItem tutorial;
    @FXML
    private MenuItem passwords;
    @FXML
    private MenuItem metadata;
    @FXML
    private MenuItem setFakedText;
    @FXML
    private MenuItem setPassword;
    @FXML
    private MenuItem enterPassword;
    @FXML
    private Button prevPage;
    @FXML
    private Label pagesNumber;
    @FXML
    private Button nextPage;

    String pass = "123123";

    private int getPageNum() {
        return Integer.parseInt(pagesNumber.getText());
    }

    private Page getPage() {
        return DataProxy.getBook().getPages().get(getPageNum());
    }
    @FXML
    public void initialize() {
        if(DataProxy.getBook().getPages().size()==0) {
            DataProxy.getBook().getPages().add(new PageV1());
        }
        pagesNumber.setText(String.valueOf((DataProxy.getBook().getPages().size()-1)));
        area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
        area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                var page = DataProxy.getBook().getPages().get(getPageNum());
                if (!observableValue.getValue().equals(page.getFake()))
                    make(new Changed(page.getText(pass), observableValue.getValue(), getPageNum(), pass));
            }
        });

        nextPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(getPageNum()+1==DataProxy.getBook().getPages().size()) {
                    DataProxy.getBook().getPages().add(new PageV1());
                }
                pagesNumber.setText(String.valueOf(getPageNum()+1));
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
            }
        });
        prevPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(getPageNum()>0) {
                    pagesNumber.setText(String.valueOf(getPageNum()-1));
                    area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
                    //DataProxy.getBook().getPages().add(new PageV1());
                }
            }
        });
        enterPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog dialog = new TextInputDialog();
                dialog.setTitle("Enter password");
                dialog.setHeaderText("please, enter password for page opening");
                dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent dialogEvent) {
                        if (dialog.getResult()!=null) {
                            pass = dialog.getResult().toString();
                            area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
                        }
                    }
                });
                dialog.show();
            }
        });
        setPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog dialog = new TextInputDialog();
                dialog.setTitle("Setting up password");
                dialog.setHeaderText("please, enter password for page current page");
                dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent dialogEvent) {
                        if (dialog.getResult()!=null) {
                            getPage().setText(dialog.getResult().toString(), getPage().getText(pass));
                        }
                    }
                });
                dialog.show();
            }
        });
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DataProxy.save();
            }
        });
        setFakedText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getPage().setFake(area.getText());
            }
        });
        metadata.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage metadataWindow = new Stage() {{
                    FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("meta-view.fxml"));
                    Scene scene = null;
                    MetaController.setPage(getPage());
                    MetaController.setPageNum(getPageNum());
                    MetaController.setStage(this);
                    try {
                        scene = new Scene(fxmlLoader.load(), 400, 550);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    setTitle("METADATA");
                    setScene(scene);
                    setAlwaysOnTop(true);
                    show();
                }};
            }
        });
        passwords.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        tutorial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        replace.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog dialog = new Dialog();
                dialog.setTitle("WARNING,UNDOABLE ACTION");
                dialog.setHeaderText("This action could not be canceled");
                dialog.show();
            }
        });
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DataProxy.save();
                System.exit(0);
            }
        });
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                History.undo();
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
            }
        });
        actions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        make.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                History.make();
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
            }
        });
        reloadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DataProxy.load();
                if(DataProxy.getBook().getPages().size()==0) {
                    DataProxy.getBook().getPages().add(new PageV1());
                }
                pagesNumber.setText(String.valueOf((DataProxy.getBook().getPages().size()-1)));
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
                History.reset();
            }
        });
        exitWithoutSaving.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }



    //@FXML
    //protected void onHelloButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
    //}
}
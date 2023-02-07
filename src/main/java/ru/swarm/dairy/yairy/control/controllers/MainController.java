package ru.swarm.dairy.yairy.control.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.swarm.dairy.yairy.Boot;
import ru.swarm.dairy.yairy.control.history.History;
import ru.swarm.dairy.yairy.control.history.subjects.*;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.Book;
import ru.swarm.dairy.yairy.model.data.book.BookV1;
import ru.swarm.dairy.yairy.model.data.page.Page;
import ru.swarm.dairy.yairy.model.data.page.PageV1;
import ru.swarm.dairy.yairy.model.saves.SaveV1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static ru.swarm.dairy.yairy.control.history.History.*;

public class MainController {
    @FXML
    private TextArea area;

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

    public static MainController mainController;

    public static String getPass() {
        String passs = PasswordsController.Model.passReborn(getPage());
        if (passs==null)
            return pass;
        else {
            pass = passs;
            return passs;
        }
    }

    private static String pass = "123123";


    public static Page getPage() {
        return DataProxy.getBook().getPages().get(mainController.getPageNum());
    }

    private int getPageNum() {
        return Integer.parseInt(pagesNumber.getText());
    }

    /*private Page getPage() {
        return DataProxy.getBook().getPages().get(getPageNum());
    }*/
    public void reloadPage() {
        area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
    }
    @FXML
    public void initialize() {
        mainController = this;


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
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(getPass()));
            }
        });
        prevPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(getPageNum()>0) {
                    pagesNumber.setText(String.valueOf(getPageNum()-1));
                    area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(getPass()));
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
                            //PasswordsController.Model.append(pass);
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
                            //PasswordsController.Model.append(dialog.getResult().toString());
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
                Stage passwordsWindow = new Stage() {{
                    FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("passwords-view.fxml"));
                    Scene scene = null;
                    PasswordsController.stage = this;
                    try {
                        scene = new Scene(fxmlLoader.load(), 485, 405);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    setTitle("PASSWORDS CONTROL CENTRE");
                    setScene(scene);
                    setAlwaysOnTop(true);
                    show();
                }};
            }
        });
        tutorial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                TextArea tutorial = new TextArea(
                        "Данное приложение предназначено для хранения любой персональной информации\n" +
                        ", требующей защиты от чтения третьеми лицами. Внимание, владелец за надежность не ручается,\n" +
                                "алгоритм шифрования не подразумевает высокой надежности, любой криптоспециалист\n" +
                                "в короткие сроки сможет открыть данное хранилище. Оно предназначено для использования\n" +
                                "лишь против неквалифецированных лиц.\n" +
                                "Вся ответственность за использование лежит, только на пользователе\n");
                tutorial.setEditable(false);
                tutorial.setWrapText(true);
                var pane = new GridPane();
                pane.add(tutorial, 0, 0);
                Scene scene = new Scene(pane);
                stage.setTitle("TUTORIAL");
                stage.setScene(scene);
                stage.show();
            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("ADD");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAIRY Files", "*.save", "*.book"
                        , "*.page"));

                if (OSValidator.isUnix())
                    fileChooser.setInitialDirectory(new File("/home/"+System.getProperty("user.name")));
                else
                    fileChooser.setInitialDirectory(new File("C:/"));
                var file = fileChooser.showOpenDialog(stage);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                var s = file.getName().split("\\.");
                switch (s[s.length-1]) {
                    case "page":
                        Book book;
                        try {
                            var page = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), PageV1.class);
                            if (page==null) {
                                book = new BookV1("");
                            } else {
                                book = new BookV1(page.getName());
                                book.getPages().add(page);
                            }
                            DataProxy.log(book);
                            History.make(new Loaded(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "book":
                        try {
                            book = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), BookV1.class);
                            if (book==null) {
                                book = new BookV1("");
                            }
                            book.prepareAfterGson();
                            History.make(new Loaded(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "save":
                        book = new SaveV1().load(file);
                        if (book==null) {
                            book = new BookV1("");
                        }
                        History.make(new Loaded(book));
                    default: break;
                }
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                History.make(new Removed(getPage()));
                int t = getPageNum()-1;
                if (t < 0) t = 0;
                pagesNumber.setText(String.valueOf(t));
                reloadPage();
            }
        });
        replace.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                    FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("replace-view.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 280, 240);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ReplaceController.index = getPageNum();
                    ReplaceController.stage.setTitle("REPLACING");
                    ReplaceController.stage.setScene(scene);
                    ReplaceController.stage.setAlwaysOnTop(true);
                    ReplaceController.stage.show();
            }
        });
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                TextArea about = new TextArea("Автор и единственный владелец приложения: Курбатов Юрий Михайлович" +
                        "\nВсе права принадлежат владельцу\n" +
                        "Закрытая альфа версия доступна ближнему кругу друзей");
                about.setEditable(false);
                var pane = new GridPane();
                pane.add(about, 0, 0);
                Scene scene = new Scene(pane);
                stage.setTitle("ABOUT");
                stage.setScene(scene);
                stage.show();
            }
        });
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("REMOVE");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAIRY Files", "*.save", "*.book"
                        , "*.page"));

                if (OSValidator.isUnix())
                    fileChooser.setInitialDirectory(new File("/home/"+System.getProperty("user.name")));
                else
                    fileChooser.setInitialDirectory(new File("C:/"));
                var file = fileChooser.showOpenDialog(stage);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                switch (file.getName()) {
                    case "page":
                        Book book;
                        try {
                            var page = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), PageV1.class);
                            if (page==null) {
                                book = new BookV1("");
                            } else {
                                book = new BookV1(page.getName());
                            }
                            History.make(new Unloaded(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "book":
                        try {
                            book = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), BookV1.class);
                            book.prepareAfterGson();
                            if (book==null) {
                                book = new BookV1("");
                            }
                            History.make(new Unloaded(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "save":
                        book = new SaveV1().load(file);
                        if (book==null) {
                            book = new BookV1("");
                        }
                        History.make(new Unloaded(book));
                    default: break;
                }
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
                Stage stage = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("UPDATE");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAIRY Files", "*.save", "*.book"
                        , "*.page"));

                if (OSValidator.isUnix())
                    fileChooser.setInitialDirectory(new File("/home/"+System.getProperty("user.name")));
                else
                    fileChooser.setInitialDirectory(new File("C:/"));
                var file = fileChooser.showOpenDialog(stage);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                var s = file.getName().split("\\.");
                switch (s[s.length-1]) {
                    case "page":
                        Book book;
                        try {
                            var page = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), PageV1.class);
                            if (page==null) {
                                book = new BookV1("");
                            } else {
                                book = new BookV1(page.getName());
                                book.getPages().add(page);
                            }
                            History.make(new Updated(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "book":
                        try {
                            book = gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), BookV1.class);
                            book.prepareAfterGson();
                            if (book==null) {
                                book = new BookV1("");
                            }
                            History.make(new Updated(book));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "save":
                        book = new SaveV1().load(file);
                        if (book==null) {
                            book = new BookV1("");
                        }
                        History.make(new Updated(book));
                    default: break;
                }
            }
        });
        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(Boot.class.getResource("export-view.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 323, 465);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ExportController.stage.setTitle("EXPORT");
                ExportController.stage.setScene(scene);
                ExportController.stage.setAlwaysOnTop(true);
                ExportController.stage.show();
            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                History.undo();
                area.setText(DataProxy.getBook().getPages().get(getPageNum()).getText(pass));
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
class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return OS.contains("sunos");
    }

    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

}
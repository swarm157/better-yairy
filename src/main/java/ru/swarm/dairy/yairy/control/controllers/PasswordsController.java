package ru.swarm.dairy.yairy.control.controllers;

import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.CommonMethods;
import ru.swarm.dairy.yairy.model.data.page.Page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class PasswordsController {

    @FXML
    public TextArea area;
    @FXML
    public Button append;
    @FXML
    public PasswordField password;
    @FXML
    public Button ok;
    @FXML
    public Button cancel;
    @FXML
    public Button save;
    @FXML
    public Button load;
    @FXML
    public Label entered;
    @FXML
    public Button reset;
    @FXML
    public Button popLast;
    @FXML
    public CheckBox isVisible;
    @FXML
    public Button generatePass;
    @FXML
    public Button apply;
    @FXML
    public Button add;
    @FXML
    public TextArea generatedPass;
    @FXML
    public Button toCurrentPage;

    ArrayList<String> passwords;
    static Stage stage = null;

    void reloadArea(boolean visible) {
        if(visible) {
            StringBuilder passes = new StringBuilder();
            for(String pass : passwords) {
                passes.append(pass);
                passes.append("\n");
            }
            area.setText(passes.toString());
        } else {
            StringBuilder passes = new StringBuilder();
            for(String pass : passwords) {
                StringBuilder fake = new StringBuilder();
                for (int i = 0; i < pass.length(); i++) {
                    fake.append("*");
                }
                passes.append(fake);
                passes.append("\n");
            }
            area.setText(passes.toString());
        }
    }
    @FXML
    public void initialize() {

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Model.passwords = passwords;
                stage.close();
            }
        });

        passwords = new ArrayList<String>();
        passwords.addAll(Model.passwords);

        reloadArea(isVisible.isSelected());
        append.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                passwords.add(password.getText());
                password.setText("");
                reloadArea(isVisible.isSelected());
            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(generatedPass.getText()!="") {
                    passwords.add(generatedPass.getText());
                    generatedPass.setText("");
                    reloadArea(isVisible.isSelected());
                }
            }
        });
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Model.passwords = passwords;
            }
        });
        generatePass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Random rand = new Random(System.nanoTime());
                byte[] key = new byte[256];
                rand.nextBytes(key);
                generatedPass.setText(new String(key));
            }
        });
        toCurrentPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(generatedPass.getText()!="") {
                    passwords.add(generatedPass.getText());
                    MainController.getPage().setText(generatedPass.getText(), MainController.getPage().getText(MainController.getPass()));
                    generatedPass.setText("");
                    reloadArea(isVisible.isSelected());
                }
            }
        });
        isVisible.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reloadArea(isVisible.isSelected());
            }
        });
        popLast.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                password.setText(Model.passwords.get(Model.passwords.size()-1));
                passwords.remove(Model.passwords.size()-1);
                reloadArea(isVisible.isSelected());
            }
        });
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Model.passwords = new ArrayList<>();
            }
        });
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Model.load();
                passwords = new ArrayList<>();
                passwords.addAll(Model.passwords);
                reloadArea(isVisible.isSelected());
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Model.save();
            }
        });
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Model.passwords = passwords;
                stage.close();
            }
        });
    }

    public static class Model {
        static ArrayList<String> passwords = new ArrayList<String>();
        public static void save() {
            DataProxy.log("Saving passwords...");
            StringBuilder password = new StringBuilder();
            for (int i = 0; i < passwords.size(); i++) {
                password.append(passwords.get(i));
                if(i < passwords.size()-1) password.append("dddddddd");
            }

            try {
                FileUtils.delete(new File("passwords.txt"));
                FileUtils.touch(new File("passwords.txt"));
                FileUtils.write(new File("passwords.txt"), CommonMethods.encrypt(password.toString(), "123123"));
                DataProxy.log("Successfully");
            } catch (IOException e) {
                DataProxy.log("Cannot save passwords");
                throw new RuntimeException(e);
            }
        }
        public static void load() {
            try {
                var temp = CommonMethods.decrypt(FileUtils.readFileToString(new File("passwords.txt")), "123123").split("dddddddd");
                if (temp!=null) {passwords = new ArrayList<>();Collections.addAll(passwords, temp);} else if(passwords.size()==0) {passwords = new ArrayList<>();passwords.add("123123");}
            } catch (IOException e) {
                try {
                    FileUtils.touch(new File("passwords.txt"));
                    passwords = new ArrayList<>();
                    passwords.add("123123");
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public static String passReborn(Page page) {
            for(String password : passwords) {
                try {
                    if (passwords.size()==0) passwords.add("123123");
                    if (passwords.size()==1&&passwords.get(0).equals("")) {passwords.remove(0);passwords.add("123123");}
                    if(!CommonMethods.decrypt(page.getEncryptedText(), password).equals(page.getFake())) return password;
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
        public static void append(String password) {
            if (!passwords.contains("123123")) passwords.add("123123");
            passwords.add(password);
            HashSet<String> set = new HashSet<>(passwords);
            passwords = new ArrayList<>(set);
        }
    }
}

package com.example.controlejava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class S1ControllerJA {
    @FXML
    Label userIDJA;
    @FXML
    TextField usernameJA;
    @FXML
    PasswordField passwordJA;
    @FXML
    Button btnReset;
    @FXML
    Hyperlink hyperLinkId;

    String newPassword="admin";

    public void onchangePassword(ActionEvent event) {
        hyperLinkId.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Change Password");
            PasswordField newpwd=new PasswordField();
            newpwd.setPromptText("New password");
            alert.getDialogPane().setContent(newpwd);
            StageStyle stageStyle = StageStyle.UNDECORATED;
            alert.initStyle(stageStyle);

            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    newPassword=newpwd.getText();
                }
            });
        });
    }
    @FXML
    void Login() throws IOException {
        if(usernameJA.getText().equals("admin") && passwordJA.getText().equals(newPassword)){
        Stage st=(Stage) userIDJA.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader(HelloApplication.class.getResource("S2JA.fxml")); // charger la scene
        Scene s2= new Scene(fx.load()); // Création de la scene
        st.setScene(s2);
        }
        else{
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentification Error");
            alert.setHeaderText("Username or password are not valid");
            alert.show();
        }
    }

    public void clear(ActionEvent event) {
        btnReset.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->{
            usernameJA.clear();
            passwordJA.clear();
        } );
    }

}

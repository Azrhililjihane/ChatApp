package com.example.controlejava;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class s2ControllerJA {
    @FXML
    private TextField PortId;
    @FXML
    private TextField msgID;
    @FXML
    private TextField hostId;
    @FXML
    private ListView testviewJA;
    private PrintWriter pw;
    @FXML
    private Button SignOutBtn;
    @FXML
    private Hyperlink gameId;

    @FXML
    protected void onConnect() throws IOException {
        String host=hostId.getText();
        int port= Integer.parseInt(PortId.getText());
        Socket s=new Socket(host,port);
        InputStream is= s.getInputStream(); // permet de lire un octet
        InputStreamReader isR= new InputStreamReader(is); // permet de lire un caractère
        BufferedReader br=new BufferedReader(isR); // permet de lire une chaîne de caractères, prend en paramètre le caractère(InputStreamReader)
        OutputStream os= s.getOutputStream();
         pw=new PrintWriter(os,true);
        new Thread(()->{
            while (true) {
                try {
                    String reponse = br.readLine();
                    Platform.runLater(() -> {
                        testviewJA.getItems().add(reponse);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    public void onsubmitJA() {
        String message=msgID.getText();
        pw.println(message);
    }

    public void onSignOut(ActionEvent event) {
        SignOutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("S1JA.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scene.getStylesheets().add(this.getClass().getResource("Scene1.css").toExternalForm());
            Stage stage=(Stage) SignOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }

    public void onGaming(ActionEvent event) {
        gameId.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Scene3.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage=(Stage) SignOutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
    }
}

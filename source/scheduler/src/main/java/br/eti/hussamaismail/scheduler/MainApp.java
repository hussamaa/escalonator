package br.eti.hussamaismail.scheduler;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception { 	
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        log.info("Iniciando aplicacao Escalonator");

        String fxmlFile = "/fxml/main.fxml";
        FXMLLoader loader = new FXMLLoader();
     
        log.info("Carregando arquivo de i18n com linguagem: " + Locale.getDefault());
        loader.setResources(ResourceBundle.getBundle("message"));
  
        log.info("Carregando arquivo fxml principal");      
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        
        Scene scene = new Scene(rootNode, 800, 600);
        stage.setTitle("Escalonator - Hussama Ismail");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

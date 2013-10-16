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
    public static Stage STAGE = null;

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
        rootNode.setStyle("-fx-padding: 0; -fx-background-color: radial-gradient(center 70% 5%, radius 60%, #767a7b,#2b2f32); -fx-background-image: url(\"/styles/images/noise.png\") , url(\"title.png\"); -fx-background-repeat: repeat, no-repeat; -fx-background-position: left top, left 19px top 15px ;");
        
        Scene scene = new Scene(rootNode, 1024, 768);
        scene.getStylesheets().add("/styles/styles.css");

        System.out.println("STAGE " + STAGE);
        
        STAGE = stage;
        stage.setTitle("Escalonator");        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

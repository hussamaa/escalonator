/*
This file is part of Escalonator.

Escalonator is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Escalonator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Escalonator. If not, see <http://www.gnu.org/licenses/>.
*/
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

        String fxmlFile = "/fxml/tabs/generator.fxml";
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

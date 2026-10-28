package ProjetFinale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// extends pour dire a java que c'est pas une classe classique
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        //charge le fxml et crée tout pour lier avec controller et le / obligatoire
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue-principale.fxml"));

        Scene scene = new Scene(loader.load(),900,600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Projet Final  -  Gestionnaire d'images");
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}

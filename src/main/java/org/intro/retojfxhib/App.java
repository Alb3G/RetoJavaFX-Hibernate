package org.intro.retojfxhib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        loadFXML("login-view.fxml", "Login", 900, 500);
    }

    public static void loadFXML(String view, String title, int width, int height)  {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + view));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
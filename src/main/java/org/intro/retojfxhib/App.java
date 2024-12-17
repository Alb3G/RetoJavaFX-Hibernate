package org.intro.retojfxhib;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.intro.retojfxhib.dao.UserDAO;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.models.User;
import org.intro.retojfxhib.services.BackGroundCleaner;
import org.intro.retojfxhib.services.SessionService;

import java.io.IOException;

public class App extends Application {
    @Getter
    private static Stage stage;
    private UserDAO userDAO = new UserDAO(HibUtils.getSessionFactory());
    private SessionService sessionService = new SessionService(HibUtils.getSessionFactory());

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        if(sessionService.sessionTokenByUserIdExists() && !sessionService.sessionTokenIsExpired()) {
            SessionToken sessionToken = sessionService.getSessionToken();
            User user = userDAO.findById(sessionToken.getUserId());
            SessionManager.getInstance().setCurrentUser(user);
            loadFXML("main-view.fxml", "Login", 1080, 700);
        } else {
            loadFXML("login-view.fxml", "Login", 1080, 700);
        }

        // Forzamos al Cleaner que se detenga cuando cerramos la app.
        stage.setOnCloseRequest(event -> {
            BackGroundCleaner.stopBackgroundCleaner();
            Platform.exit();
            System.exit(0);
        });
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
        BackGroundCleaner.startBackGroundCleaner();
        launch();
    }
}
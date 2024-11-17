package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.UserDAO;
import org.intro.retojfxhib.models.User;
import org.intro.retojfxhib.services.SessionService;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final UserDAO userDAO = new UserDAO(HibUtils.getSessionFactory());
    private String securityCode = SessionManager.getInstance().getVerificationCode();
    private SessionService sessionService = new SessionService(HibUtils.getSessionFactory());

    @FXML
    private Button registerBtn;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passInput;
    @FXML
    private TextField emailInput;
    @FXML
    private CheckBox rememberCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SessionManager.getInstance().isRememberUser()) {
            emailInput.setText(SessionService.getUserEmail());
            rememberCheckBox.setSelected(true);
        } else {
            rememberCheckBox.setSelected(false);
        }
    }

    @FXML
    public void onClick() {
        String email = emailInput.getText();
        User user = userDAO.validateUser(email, passInput.getText());
        if(user != null) {
            if(user.isVerified()) {
                SessionManager.getInstance().setCurrentUser(user);
                SessionManager.getInstance().getCurrentUser().setVerified(true);
                // Aqui genero token para la persistencia de la sesion
                sessionService.saveSessionToken(SessionManager.getInstance().getCurrentUser().getId());
                SessionManager.getInstance().setSessionToken(sessionService.getSessionToken());
                App.loadFXML("main-view.fxml", "Movies" , 1080, 700);
            } else {
                registerConfirmationDialog(user);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
            alert.setTitle("LogIn Error");
            alert.setHeaderText("Email or password incorrect");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onRegister(ActionEvent actionEvent) {
        App.loadFXML("register-view.fxml", "Register",1080, 700);
    }

    private void registerConfirmationDialog(User user) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Register Confirmation");
        dialog.setHeaderText("Verification Code Confirmation");

        TextField codeField = new TextField();
        codeField.setPromptText("Enter your verification code");

        VBox content = new VBox();
        content.getChildren().add(codeField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return codeField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(code -> {
            // Process the entered code here
            if(code.equals(securityCode)) {
                user.setVerified(true);
                userDAO.update(user);
            }
        });
    }

    @FXML
    public void onSelect(ActionEvent actionEvent) {
        SessionManager.getInstance().setRememberUser(rememberCheckBox.isSelected());
    }
}
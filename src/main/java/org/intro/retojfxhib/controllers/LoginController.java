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

/**
 * Clase controladora de la vista Login
 */
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

    /**
     * Comprobamos que el usuario eligió recordar usuario para setear el
     * input del email con el usuario o no.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(SessionManager.getInstance().isRememberUser()) {
            emailInput.setText(SessionService.getUserEmail());
            rememberCheckBox.setSelected(true);
        } else {
            rememberCheckBox.setSelected(false);
        }
    }

    /**
     * Método encargado de iniciar sesion, gestiona toda la lógica del proceso
     * de inicio de sesion, si la validación del email y de la contraseña es correcta
     * se verificará el usuario y se creará su token de sesion, después se viaja a la vista
     * principal.
     */
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

    /**
     * Método para la navegación a la vista de Registro de usuarios.
     */
    @FXML
    public void onRegister(ActionEvent actionEvent) {
        App.loadFXML("register-view.fxml", "Register",1080, 700);
    }

    /**
     * Método que muestra diálogo para introducir el codigo de verificación
     * depués del registro para confirmar el proceso completo de registro.
     * @param user Al que se le modificará el estado de verificado si el código es correcto.
     */
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

    /**
     * Método para establecer el atributo de la sesion que determina si el usuario
     * quiere que se recuerde su email o no.
     */
    @FXML
    public void onSelect(ActionEvent actionEvent) {
        SessionManager.getInstance().setRememberUser(rememberCheckBox.isSelected());
    }
}
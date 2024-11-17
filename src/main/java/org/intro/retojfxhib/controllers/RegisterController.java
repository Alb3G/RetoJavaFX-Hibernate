package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.UserDAO;
import org.intro.retojfxhib.models.User;
import org.intro.retojfxhib.services.MailService;
import org.intro.retojfxhib.utils.Util;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Clase controladora de la vista Register.
 */
public class RegisterController implements Initializable {
    private UserDAO userDAO = new UserDAO(HibUtils.getSessionFactory());
    private String securityCode = Util.randomRegisterCode(5);

    @FXML
    private Button registerBtn;
    @FXML
    private TextField emailInputText;
    @FXML
    private Button cancelRegisterBtn;
    @FXML
    private PasswordField passInputText;
    @FXML
    private TextField userNameInput;

    /**
     * Método para nada más entrar en la vista registro crear código de verificación
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionManager.getInstance().setVerificationCode(securityCode);
    }

    /**
     * Método para cancelar proceso de registro, el código de verificación se establece a null
     * en caso de cancelar proceso.
     */
    @FXML
    public void onCancel(ActionEvent actionEvent) {
        SessionManager.getInstance().setVerificationCode(null);
        App.loadFXML("login-view.fxml", "Login", 1080, 700);
    }

    /**
     * Método que gestiona el proceso de registrar usuario, se establecen
     * propiedades como fecha de creación del usuario, admin = false, desde aquí no se
     * crea ningún administrador, nombre de usuario, se lanza un MailService en un hilo secundario
     * y por último se guarda en la db.
     */
    @FXML
    public void onRegister(ActionEvent actionEvent) {
        if(Util.validEmail(emailInputText.getText()) && Util.validPassword(passInputText.getText())) {
            User u = new User();
            u.setEmail(emailInputText.getText());
            u.setPassword(BCrypt.hashpw(passInputText.getText(), BCrypt.gensalt()));
            u.setCreatedAt(LocalDateTime.now());
            u.setAdmin(false);
            u.setVerified(false);
            u.setUserName(userNameInput.getText());
            userDAO.save(u);
            try {
                MailService emailService = new MailService(emailInputText.getText(), securityCode);
                new Thread(emailService).start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                securityCode = null;
                SessionManager.getInstance().setVerificationCode(securityCode);
            }
            App.loadFXML("login-view.fxml", "Login", 1080, 700);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
            alert.setTitle("Register Error");
            alert.setHeaderText("Email or password format error");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }
}

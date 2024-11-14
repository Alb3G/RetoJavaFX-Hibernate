package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionManager.getInstance().setVerificationCode(securityCode);
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        SessionManager.getInstance().setVerificationCode(null);
        App.loadFXML("login-view.fxml", "Login", 1080, 700);
    }

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
            System.out.println("Invalid email or password. Please try again.");
        }
    }
}

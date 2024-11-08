package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.UserDAO;
import org.intro.retojfxhib.models.User;

public class LoginController {
    private final UserDAO userDAO = new UserDAO(HibUtils.getSessionFactory());

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

    @FXML
    public void onClick(ActionEvent actionEvent) {
        String email = emailInput.getText();
        User u = userDAO.validateUser(email, passInput.getText());
        if(u != null) {
            SessionManager.getInstance().setCurrentUser(u);
            App.loadFXML("main-view.fxml", "Movies" , 1080, 700);
        }
    }

    @FXML
    public void onRegister(ActionEvent actionEvent) {}
}
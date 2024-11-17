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
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.services.MailService;
import org.intro.retojfxhib.services.SessionService;
import org.intro.retojfxhib.utils.Util;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clase controladora de la vista de Información del usuario.
 */
public class InfoController implements Initializable {
    private UserDAO userDAO = new UserDAO(HibUtils.getSessionFactory());
    private String securityCode = Util.randomRegisterCode(5);
    private boolean deleteProcessVerified = false;
    private SessionService sessionService = new SessionService(HibUtils.getSessionFactory());

    @FXML
    private Label userNameLabel;
    @FXML
    private Label userAcdLabel;
    @FXML
    private Button deleteAccountBtn;
    @FXML
    private Label userEmailLabel;
    @FXML
    private MenuItem moviesMenuBtn;
    @FXML
    private MenuItem logOutMenuItem;
    @FXML
    private MenuItem copiesMenuBtn;

    /**
     * Establecemos la información del usuario.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUserInfo();
    }

    /**
     * Método para establecer los labels con la información del usuario.
     */
    private void setUserInfo() {
        userNameLabel.setText(SessionManager.getInstance().getCurrentUser().getUserName());
        userEmailLabel.setText(SessionManager.getInstance().getCurrentUser().getEmail());
        userAcdLabel.setText(SessionManager.getInstance().getCurrentUser().getCreatedAt().toString());
    }

    /**
     * Método para eliminar la cuenta del usuario, se instancia un servicio
     * de email y se envía un email al correo del usuario para que introduzca
     * el código y si es correcto se eliminará la cuenta de la Db junto con toda
     * su información y de la sesion también.
     */
    @FXML
    public void onDeleteAcc(ActionEvent actionEvent) {
        MailService mailService = new MailService(SessionManager.getInstance().getCurrentUser().getEmail(), securityCode);
        try {
            new Thread(mailService).start();
        } catch (Exception e) {
            System.out.println("Error sending mail confirmation");
        }
        deleteAccDialog();
        if(deleteProcessVerified) {
            userDAO.delete(SessionManager.getInstance().getCurrentUser());
            SessionManager.getInstance().logout();
            sessionService.deleteSessionTokenById(sessionService.getSessionToken().getSessionTokenId());
            SessionManager.getInstance().setRememberUser(false);
            App.loadFXML("login-view.fxml", "Login", 1080, 700);
        }
    }

    /**
     * Creación de diálogo para introducir código de verificación de la cuenta.
     */
    private void deleteAccDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete Account Confirmation");
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
            if(code.equals(securityCode))
                deleteProcessVerified = true;
        });
    }

    /**
     * Método para la navegación a la vista principal de Movies.
     */
    @FXML
    public void onMoviesNav(ActionEvent actionEvent) {
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    /**
     * Método que ejecutaremos por defecto cada vez que el usuario decida cerrar sesion de la app.
     * Recuperamos Token de la sesion y si existe, lo eliminamos y redirigimos al usuario al login de nuevo.
     */
    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        SessionToken sessionToken = sessionService.getSessionToken();
        if (sessionToken != null) {
            sessionService.deleteSessionTokenById(sessionToken.getSessionTokenId());
        }
        SessionManager.getInstance().logout();
        App.loadFXML("login-view.fxml", "Login", 1080, 700);
    }

    /**
     * Método para la navegación a la vista copias.
     */
    @FXML
    public void onCopiesNav(ActionEvent actionEvent) {
        App.loadFXML("copies-view.fxml", "Copies", 1080, 700);
    }
}

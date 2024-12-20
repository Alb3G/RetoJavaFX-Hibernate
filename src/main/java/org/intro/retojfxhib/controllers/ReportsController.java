package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.models.SessionToken;
import org.intro.retojfxhib.services.ReportService;
import org.intro.retojfxhib.services.SessionService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * Controlador que nos permitirá gestionar la descarga y envío de reportes al usuario.
 * @author Alberto Guzman
 */
public class ReportsController implements Initializable {

    private SessionService sessionService = new SessionService(HibUtils.getSessionFactory());
    private ReportService reportService = new ReportService();

    @FXML
    private Button moreThanOneCopyBtn;
    @FXML
    private Button badConditionBtn;
    @FXML
    private Button allMoviesInfoBtn;
    @FXML
    private MenuItem logOutMenuBtn;
    @FXML
    private MenuItem moviesMenuBtn;
    @FXML
    private MenuItem copiesBtn;
    @FXML
    private MenuItem profileInfoBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Método que genera un reporte de todas las películas y permite al usuario
     * guardarlo como un archivo PDF en su sistema local.
     */
    @FXML
    public void onAllMoviesReport(ActionEvent actionEvent) {
        File report = reportService.generateReportForAllMovies();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        fileChooser.setInitialFileName(report.getName());

        File fileToSave = fileChooser.showSaveDialog(App.getStage());

        if (fileToSave != null) {
            try {
                Files.copy(report.toPath(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF saved in: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error downloading the PDF");
                e.printStackTrace();
            }
        } else {
            System.out.println("User cancelled the operation");
        }
    }

    /**
     * Método que genera un reporte de las películas con más de una copia y permite al usuario
     * guardarlo como un archivo PDF en su sistema local.
     */
    @FXML
    public void onMoreThanOneCopyReport(ActionEvent actionEvent) {
        File report = reportService.reportForMoviesWithMoreThanOneCopy();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        fileChooser.setInitialFileName(report.getName());

        File fileToSave = fileChooser.showSaveDialog(App.getStage());

        if (fileToSave != null) {
            try {
                Files.copy(report.toPath(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF saved in: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error downloading the PDF");
                e.printStackTrace();
            }
        } else {
            System.out.println("User cancelled the operation");
        }
    }

    /**
     * Método que genera un reporte de las películas en mal estado y permite al usuario
     * guardarlo como un archivo PDF en su sistema local.
     */
    @FXML
    public void onBadConditionReport(ActionEvent actionEvent) {
        File report = reportService.generateReportForMoviesInBadConditon();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        fileChooser.setInitialFileName(report.getName());

        File fileToSave = fileChooser.showSaveDialog(App.getStage());

        if (fileToSave != null) {
            try {
                Files.copy(report.toPath(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF saved in: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error downloading the PDF");
                e.printStackTrace();
            }
        } else {
            System.out.println("User cancelled the operation");
        }
    }

    /**
     * Navegacion a vista copias.
     */
    @FXML
    public void navToCopiesView(ActionEvent actionEvent) {
        App.loadFXML("copies-view.fxml", "User Copies", 1080, 700);
    }

    /**
     * Cierre de sesion
     */
    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        SessionToken sessionToken = sessionService.getSessionToken();
        if (sessionToken != null)
            sessionService.deleteSessionTokenById(sessionToken.getSessionTokenId());
        if(SessionManager.getInstance().isRememberUser()) {
            SessionService.setUserEmail(SessionManager.getInstance().getCurrentUser().getEmail());
        }
        SessionManager.getInstance().logout();
        App.loadFXML("login-view.fxml", "Login", 1080, 700);
    }

    /**
     * Navegacion a vista principal.
     */
    @FXML
    public void navToMoviesView(ActionEvent actionEvent) {
        App.loadFXML("main-view.fxml", "Movies" , 1080, 700);
    }

    /**
     * Navegacion a vista perfil con informacion del usuario.
     */
    @FXML
    public void navToProfile(ActionEvent actionEvent) {
        App.loadFXML("user-info-view.fxml", "Account Info" , 1080, 700);
    }
}

package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.dto.CopyDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase controladora de la vista Copias
 */
public class CopiesController implements Initializable {
    private MovieDAO movieDAO = new MovieDAO(HibUtils.getSessionFactory());
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());

    @FXML
    private TableView<CopyDTO> movieTable;
    @FXML
    private TableColumn<CopyDTO, String> titleCol;
    @FXML
    private TableColumn<CopyDTO, String> conditionCol;
    @FXML
    private TableColumn<CopyDTO, String> platformCol;
    @FXML
    private MenuItem copiesBtn;
    @FXML
    private MenuItem profileInfoBtn;
    @FXML
    private MenuItem logOutMenuBtn;
    @FXML
    private TextField txtFilterInput;
    @FXML
    private ComboBox<String> platformCombo;
    @FXML
    private ComboBox<String> conditionCombo;
    @FXML
    private Button refreshTableBtn;
    @FXML
    private Button filterBtn;
    @FXML
    private MenuItem reportsMenuBtn;

    /**
     * Método para inicializar datos básicos de la vista como los comboBoxes
     * o las columnas de la tabla
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBoxes();
    }

    /**
     * Método que inicializa los combos con sus datos provenientes de la DB.
     */
    private void initializeComboBoxes() {
        copiesBtn.setText("Movies");
        setTableData();
        conditionCombo.getItems().add("");
        conditionCombo.getItems().addAll(movieCopyDAO.getCopiesCondition());
        conditionCombo.setValue(conditionCombo.getItems().getFirst());
        platformCombo.getItems().add("");
        platformCombo.getItems().addAll(movieCopyDAO.getCopiesPlatform());
        platformCombo.setValue(platformCombo.getItems().getFirst());
    }

    /**
     * Método que inicializa los datos de la tabla mapeando la clase CopyDTO.
     */
    private void setTableData() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        platformCol.setCellValueFactory(new PropertyValueFactory<>("platform"));
        movieTable.getItems().addAll(movieDAO.getDtoObjOfUser(SessionManager.getInstance().getCurrentUser().getId()));
        movieTable.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue == null) return;
            SessionManager.getInstance().setSelectedCopyDTO(newValue);
            App.loadFXML("movie-copie-detail-view.fxml", newValue.getTitle(), 1080, 700);
        });
    }

    /**
     * Método que establece la lógica de filtrado de la tabla copias, en función
     * de los parámetros de filtrado.
     */
    private void filterTable() {
        String textInput = txtFilterInput.getText();
        String condition = conditionCombo.getValue();
        String platform = platformCombo.getValue();

        List<CopyDTO> filteredTable = movieDAO.getDtoObjOfUser(SessionManager.getInstance().getCurrentUser().getId()).stream()
                .filter(dto -> (condition.isBlank() || dto.getCondition().equals(condition)))
                .filter(dto -> (platform.isBlank() || dto.getPlatform().equals(platform)))
                .filter(dto -> (textInput.isBlank() || dto.getMovie().getTitle().contains(textInput)))
                .toList();
        movieTable.getItems().clear();
        movieTable.getItems().addAll(filteredTable);
        // Reset de inputs
        txtFilterInput.setText("");
        conditionCombo.setValue("");
        platformCombo.setValue("");
    }

    /**
     * Método que ejecutaremos por defecto cada vez que el usuario decida cerrar sesion de la app.
     * Recuperamos Token de la sesion y si existe, lo eliminamos y redirigimos al usuario al login de nuevo.
     */
    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        SessionManager.getInstance().logout();
        App.loadFXML("login-view.fxml", "Login", 1080, 700);
    }

    @FXML
    public void navToProfile(ActionEvent actionEvent) {
        App.loadFXML("user-info-view.fxml", "Account Info" , 1080, 700);
    }

    /**
     * Método para la navegación a la vista Principal
     */
    @FXML
    public void navToMainView(ActionEvent actionEvent) {
        App.loadFXML("main-view.fxml", "Movies" , 1080, 700);
    }

    /**
     * Método de ejecución del filtrado.
     */
    @FXML
    public void onFilterAction(ActionEvent actionEvent) {
        filterTable();
    }

    /**
     * Método para refrescar la lista de copias.
     */
    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        List<CopyDTO> dtos = movieDAO.getDtoObjOfUser(SessionManager.getInstance().getCurrentUser().getId());
        movieTable.getItems().clear();
        movieTable.getItems().addAll(dtos);
    }

    @FXML
    public void navToReportsView(ActionEvent actionEvent) {
        App.loadFXML("reports-view.fxml", "Reports" , 1080, 700);
    }
}

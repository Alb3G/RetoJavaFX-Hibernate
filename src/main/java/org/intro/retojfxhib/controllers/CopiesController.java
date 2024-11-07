package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.DataSession;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.dto.CopyDTO;

import java.util.List;

public class CopiesController {
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
    public void initialize() {
        copiesBtn.setText("Movies");
        setTableData();
        conditionCombo.getItems().add("");
        conditionCombo.getItems().addAll(movieCopyDAO.getCopiesCondition());
        conditionCombo.setValue(conditionCombo.getItems().getFirst());
        platformCombo.getItems().add("");
        platformCombo.getItems().addAll(movieCopyDAO.getCopiesPlatform());
        platformCombo.setValue(platformCombo.getItems().getFirst());
    }

    private void setTableData() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        platformCol.setCellValueFactory(new PropertyValueFactory<>("platform"));
        movieTable.getItems().addAll(movieDAO.getDtoObjOfUser(DataSession.currentUser.getId()));
        movieTable.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue == null) return;
            DataSession.selectedCopyDTO = newValue;
            App.loadFXML("movie-copie-detail-view.fxml", newValue.getTitle(), 1080, 700);
        });
    }

    private void filterTable() {
        String textInput = txtFilterInput.getText();
        String condition = conditionCombo.getValue();
        String platform = platformCombo.getValue();

        List<CopyDTO> filteredTable = movieDAO.getDtoObjOfUser(DataSession.currentUser.getId()).stream()
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

    @FXML
    public void onLogOut(ActionEvent actionEvent) {}

    @FXML
    public void navToProfile(ActionEvent actionEvent) {}

    @FXML
    public void navToMainView(ActionEvent actionEvent) {
        App.loadFXML("main-view.fxml", "Movies" , 1080, 700);
    }

    @FXML
    public void onFilterAction(ActionEvent actionEvent) {
        filterTable();
    }

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        List<CopyDTO> dtos = movieDAO.getDtoObjOfUser(DataSession.currentUser.getId());
        movieTable.getItems().clear();
        movieTable.getItems().addAll(dtos);
    }
}

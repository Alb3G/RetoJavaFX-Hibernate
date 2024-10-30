package org.intro.retojfxhib.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.DataSession;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;

import java.util.List;


public class MainController {
    private final MovieDAO movieDAO = new MovieDAO(HibUtils.getSessionFactory());

    @FXML
    private Button filterBtn;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private ComboBox<String> genreCombo;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleCol;
    @FXML
    private TableColumn<Movie, String> movieIdCol;
    @FXML
    private TableColumn<Movie, String> yearCol;
    @FXML
    private TableColumn<Movie, String> genreCol;
    @FXML
    private TableColumn<Movie, String> directorCol;
    @FXML
    private ComboBox<String> directorCombo;
    @FXML
    private MenuItem logOutMenuBtn;
    @FXML
    private TextField txtFilterInput;
    @FXML
    private Button refreshTableBtn;
    @FXML
    private MenuItem copiesBtn;
    @FXML
    private MenuItem profileInfoBtn;

    @FXML
    public void initialize() {
        setTableData();
        setSearchFieldsData();
    }

    private void setSearchFieldsData() {
        genreCombo.getItems().add("");
        genreCombo.getItems().addAll(movieDAO.getGenres());
        genreCombo.setValue(genreCombo.getItems().getFirst());
        directorCombo.getItems().add("");
        directorCombo.getItems().addAll(movieDAO.getDirectors());
        directorCombo.setValue(directorCombo.getItems().getFirst());
        // Orden args min, max, start value, step
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990, 2024, 2000, 1));
    }

    private void setTableData() {
        movieIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        movieTable.getItems().addAll(movieDAO.findAll());
        movieTable.getSelectionModel().selectedItemProperty().addListener( (_,_,newValue) -> {
            if(newValue == null) return;
            DataSession.selectedMovie = newValue;
            App.loadFXML("movie-detail-view.fxml", newValue.getTitle(), 1080, 700);
        });
    }

    @FXML
    public void onFilterAction(ActionEvent actionEvent) {
        filterTableByParams();
    }

    private void filterTableByParams() {
        String genre = genreCombo.getValue();
        Integer year = yearSpinner.getValue();
        String director = directorCombo.getValue();
        String searchInput = txtFilterInput.getText();

        List<Movie> filteredMovies = movieDAO.findAll().stream()
                .filter(movie -> (genre.isBlank() || movie.getGenre().equals(genre)))
                .filter(movie -> (year == null || movie.getReleaseYear().toString().equals(year.toString())))
                .filter(movie -> (director.isBlank() || movie.getDirector().equals(director)))
                .filter(movie -> (searchInput.isBlank() || movie.getTitle().contains(searchInput)))
                .toList();

        movieTable.getItems().clear();
        movieTable.getItems().addAll(filteredMovies);
        // Inputs reset.
        txtFilterInput.setText("");
        genreCombo.setValue("");
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990, 2024, 2000, 1));
        directorCombo.setValue("");
    }

    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        DataSession.currentUser = null;
        App.loadFXML("login-view.fxml", "Login", 900, 500);
    }

    private void refreshTable() {
        movieTable.getItems().clear();
        movieTable.getItems().addAll(movieDAO.findAll());
    }

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        refreshTable();
    }

    @FXML
    public void navToCopiesView(ActionEvent actionEvent) {
        App.loadFXML("copies-view.fxml", "User Copies", 1080, 700);
    }

    @FXML
    public void navToProfile(ActionEvent actionEvent) {}
}
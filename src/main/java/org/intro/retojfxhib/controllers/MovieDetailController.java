package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.DataSession;
import org.intro.retojfxhib.models.Movie;

public class MovieDetailController {
    private Movie movie = DataSession.selectedMovie;

    @FXML
    private TextField yearInput;
    @FXML
    private TextField genreInput;
    @FXML
    private WebView teaserWebView;
    @FXML
    private TextField directorInput;
    @FXML
    private TextArea descInput;
    @FXML
    private Label titleLabel;
    @FXML
    private ImageView moviePoster;
    @FXML
    private Button backToMainBtn;

    @FXML
    public void initialize() {
        setDetailData();
    }

    private void setDetailData() {
        titleLabel.setText(movie.getTitle());
        descInput.setText(movie.getDescription());
        moviePoster.setImage(new Image(String.valueOf(getClass().getResource("/org/intro/retojfxhib/media/" + movie.getPoster()))));
        yearInput.setText(movie.getReleaseYear().toString());
        genreInput.setText(movie.getGenre());
        directorInput.setText(movie.getDirector());
        teaserWebView.getEngine().load(movie.getTeaserUrl());
    }

    @FXML
    public void navMain(ActionEvent actionEvent) {
        movie = null;
        DataSession.selectedMovie = null;
        teaserWebView.getEngine().load("https://google.com");
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }
}

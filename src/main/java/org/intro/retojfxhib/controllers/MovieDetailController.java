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
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.Random;

public class MovieDetailController {
    private Movie movie = DataSession.selectedMovie;
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());

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
    private Button addMovieCopyBtn;

    @FXML
    public void initialize() {
        setDetailData();
        if(DataSession.currentUser.getIsAdmin())
            addMovieCopyBtn.setVisible(false);
    }

    private void setDetailData() {
        titleLabel.setText(movie.getTitle());
        descInput.setText(movie.getDescription());
        moviePoster.setImage(new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + movie.getPoster()));
        yearInput.setText(movie.getReleaseYear().toString());
        genreInput.setText(movie.getGenre());
        directorInput.setText(movie.getDirector());
        teaserWebView.getEngine().load(movie.getTeaserUrl());
    }

    @FXML
    public void navMain(ActionEvent actionEvent) {
        DataSession.selectedMovie = null;
        teaserWebView.getEngine().load("https://google.com");
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    @FXML
    public void addMovieToCopies(ActionEvent actionEvent) {
        if(!DataSession.currentUser.getIsAdmin()) {
            MovieCopy movieCopy = new MovieCopy(
                    null,
                    DataSession.selectedMovie.getId(),
                    DataSession.currentUser,
                    getRandomCondition(),
                    getRandomPlatform()
            );
            System.out.println(movieCopy);
            movieCopyDAO.save(movieCopy);
        }
    }

    private String getRandomCondition() {
        Random rand = new Random(System.currentTimeMillis());
        Long conditionsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesCondition().get(rand.nextInt(0, conditionsLen.intValue()));
    }

    private String getRandomPlatform() {
        Random rand = new Random(System.currentTimeMillis());
        Long platformsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesPlatform().get(rand.nextInt(0, platformsLen.intValue()));
    }
}

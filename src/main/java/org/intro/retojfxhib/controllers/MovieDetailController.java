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
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.Random;

public class MovieDetailController {
    private Movie movie = SessionManager.getInstance().getSelectedMovie();
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());

    @FXML
    private TextField yearInput;
    @FXML
    private TextField genreInput;
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
    private WebView teaser;

    @FXML
    public void initialize() {
        setDetailData();
        if(SessionManager.getInstance().getCurrentUser().getIsAdmin())
            addMovieCopyBtn.setVisible(false);
    }

    private void setDetailData() {
        titleLabel.setText(movie.getTitle());
        descInput.setText(movie.getDescription());
        moviePoster.setImage(new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + movie.getPoster()));
        yearInput.setText(movie.getReleaseYear().toString());
        genreInput.setText(movie.getGenre());
        directorInput.setText(movie.getDirector());
        teaser.getEngine().load(movie.getTeaserUrl());
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

    @FXML
    public void onNavBack(ActionEvent actionEvent) {
        SessionManager.getInstance().setSelectedMovie(null);
        teaser.getEngine().load("https://google.com");
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    @FXML
    public void onAddMovie(ActionEvent actionEvent) {
        if(!SessionManager.getInstance().getCurrentUser().getIsAdmin()) {
            MovieCopy movieCopy = new MovieCopy(
                    null,
                    SessionManager.getInstance().getSelectedMovie().getId(),
                    SessionManager.getInstance().getCurrentUser(),
                    getRandomCondition(),
                    getRandomPlatform()
            );
            System.out.println(movieCopy);
            movieCopyDAO.save(movieCopy);
            App.loadFXML("main-view.fxml", "Movies", 1080, 700);
        }
    }
}

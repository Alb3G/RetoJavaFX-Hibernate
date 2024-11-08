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
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;


public class MovieCopieController {
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());
    private CopyDTO copyDTO = SessionManager.getInstance().getSelectedCopyDTO();
    private Movie movieOfDto = copyDTO.getMovie();

    @FXML
    private TextField yearInput;
    @FXML
    private TextField genreInput;
    @FXML
    private Button backToMainBtn;
    @FXML
    private Label titleLabel;
    @FXML
    private ImageView moviePoster;
    @FXML
    private TextField directorInput;
    @FXML
    private TextArea descInput;
    @FXML
    private WebView teaser;
    @FXML
    private Label conditionLabel;
    @FXML
    private Label platformLabel;
    @FXML
    private Button deleteCopyBtn;

    @FXML
    public void initialize() {
        moviePoster.setImage(new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + movieOfDto.getPoster()));
        teaser.getEngine().load(movieOfDto.getTeaserUrl());
        setConditionText(copyDTO.getCondition());
        platformLabel.setText(copyDTO.getPlatform());
        descInput.setText(movieOfDto.getDescription());
        directorInput.setText(movieOfDto.getDirector());
        genreInput.setText(movieOfDto.getGenre());
        yearInput.setText(movieOfDto.getReleaseYear().toString());
    }

    @FXML
    public void onDelete(ActionEvent actionEvent) {
        movieCopyDAO.delete(SessionManager.getInstance().getSelectedCopyDTO().getCopy());
        SessionManager.getInstance().setSelectedCopyDTO(null);
        App.loadFXML("copies-view.fxml", "Copies", 1080, 700);
    }

    private void setConditionText(String condition) {
        conditionLabel.setText(condition);
        switch (condition) {
            case "Excellent" -> conditionLabel.setStyle("-fx-text-fill: #83ff8a;");
            case "Bad" -> conditionLabel.setStyle("-fx-text-fill: #ff3934;");
            case "Good" -> conditionLabel.setStyle("-fx-text-fill: #ff8332;");
        }
    }

    @FXML
    public void onNavBack(ActionEvent actionEvent) {
        App.loadFXML("copies-view.fxml", "Copies", 1080, 700);
    }
}

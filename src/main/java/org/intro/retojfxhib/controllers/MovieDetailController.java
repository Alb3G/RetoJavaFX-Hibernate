package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;
import org.intro.retojfxhib.utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MovieDetailController {
    private Movie movie = SessionManager.getInstance().getSelectedMovie();
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());
    private MovieDAO movieDAO = new MovieDAO(HibUtils.getSessionFactory());
    private boolean isUpdate = false;
    private String posterName = movie.getPoster();

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
    private Button backToMainBtn;
    @FXML
    private WebView teaser;
    @FXML
    private Button unlockEditBtn;
    @FXML
    private ImageView unlockIcon;
    @FXML
    private Button clearImageOnEdit;
    @FXML
    private Button editMovieBtn;
    @FXML
    private Button addMovieCopyBtn;
    @FXML
    private ImageView posterImage;
    @FXML
    private TextField titleInputEdit;
    @FXML
    private HBox titleHbox;
    @FXML
    private TextField teaserInputEdit;
    @FXML
    private HBox teaserUrlHbox;

    @FXML
    public void initialize() {
        setDetailData();
        setDragAndDrop();
        if(!SessionManager.getInstance().getCurrentUser().getIsAdmin())
            unlockEditBtn.setVisible(false);
    }

    private void setDetailData() {
        titleLabel.setText(movie.getTitle());
        titleInputEdit.setText(movie.getTitle());
        descInput.setText(movie.getDescription());
        posterImage.setImage(new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + movie.getPoster()));
        yearInput.setText(movie.getReleaseYear().toString());
        genreInput.setText(movie.getGenre());
        directorInput.setText(movie.getDirector());
        teaser.getEngine().load(movie.getTeaserUrl());
        teaserInputEdit.setText(movie.getTeaserUrl());
        setFieldsDisabled();
    }

    @FXML
    public void onNavBack(ActionEvent actionEvent) {
        SessionManager.getInstance().setSelectedMovie(null);
        teaser.getEngine().load("https://google.com");
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    @FXML
    public void onAddMovie(ActionEvent actionEvent) {
        MovieCopy movieCopy = new MovieCopy(
                null,
                SessionManager.getInstance().getSelectedMovie().getId(),
                SessionManager.getInstance().getCurrentUser(),
                Util.getRandomCondition(movieCopyDAO),
                Util.getRandomPlatform(movieCopyDAO)
        );
        System.out.println(movieCopy);
        movieCopyDAO.save(movieCopy);
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    @FXML
    public void onUnlock(ActionEvent actionEvent) {
        isUpdate = !isUpdate;
        setFieldsDisabled();
        setFieldsEditable();
        setVisibilityOfEditComponents();
        if(isUpdate) {
            unlockIcon.setImage(new Image(Util.definePathForImg("unlock.png")));
        } else {
            unlockIcon.setImage(new Image(Util.definePathForImg("lock.png")));
        }
    }

    private void setVisibilityOfEditComponents() {
        clearImageOnEdit.setVisible(isUpdate);
        editMovieBtn.setVisible(isUpdate);
        titleHbox.setVisible(isUpdate);
        teaserUrlHbox.setVisible(isUpdate);
    }

    private void setFieldsEditable() {
        descInput.setEditable(isUpdate);
        genreInput.setEditable(isUpdate);
        yearInput.setEditable(isUpdate);
        directorInput.setEditable(isUpdate);
    }

    private void setDragAndDrop() {
        posterImage.setOnDragOver(dragEvent -> {
            if (isUpdate) {
                boolean isImage = dragEvent.getDragboard()
                        .getFiles()
                        .getFirst()
                        .getName()
                        .toLowerCase()
                        .matches("^.*\\.(jpg|jpeg|png)$");
                if(dragEvent.getDragboard().hasFiles() && isImage) {
                    dragEvent.acceptTransferModes(TransferMode.COPY);
                }
            }
            dragEvent.consume();
        });
        posterImage.setOnDragDropped(dragEvent -> {
            if (isUpdate) {
                Image image;
                File file = null;
                var dragBoard = dragEvent.getDragboard();
                if(dragBoard.hasFiles()) {
                    file = dragBoard.getFiles().getFirst();
                    try {
                        image = new Image(new FileInputStream(file));
                        posterImage.setImage(image);
                        posterName = file.getName();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
                if(file != null)
                    Util.saveImageInMediaFolder(file);
            }
        });
    }

    private void setFieldsDisabled() {
        descInput.setDisable(!isUpdate);
        genreInput.setDisable(!isUpdate);
        yearInput.setDisable(!isUpdate);
        directorInput.setDisable(!isUpdate);
        titleInputEdit.setDisable(!isUpdate);
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deleting Image");
        alert.setHeaderText("Are you sure you want to delete the image?");
        alert.setContentText("You cant take it back if you do it!");
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL) return;

        String path = System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + movie.getPoster();
        posterImage.setImage(null);
        File image = new File(path);
        image.delete();
    }

    @FXML
    public void onEdit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating Movie Info");
        alert.setHeaderText("Are you sure you want to modify this movie?");
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.CANCEL) return;

        Movie modifiedMovie = new Movie(
                movie.getId(),
                titleInputEdit.getText(),
                genreInput.getText(),
                Integer.parseInt(yearInput.getText()),
                descInput.getText(),
                directorInput.getText(),
                Util.parseYoutubeUrl(teaserInputEdit.getText()),
                posterName
        );
        movieDAO.update(modifiedMovie);
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }
}

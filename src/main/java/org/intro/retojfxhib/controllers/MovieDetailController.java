package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clase controladora de la vista MovieDetail.
 */
public class MovieDetailController implements Initializable {
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

    /**
     * En este método inicializamos los datos de la vista y controlamos si el usuario
     * es admin para mostrar el boton de desbloquear la edición o no.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDetailData();
        setDragAndDrop();
        if(!SessionManager.getInstance().getCurrentUser().isAdmin())
            unlockEditBtn.setVisible(false);
    }

    /**
     * Rellenamos los componentes de la vista con los distintos datos de la Movie.
     */
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

    /**
     * Método para navegar a la vista principal de la app.
     */
    @FXML
    public void onNavBack(ActionEvent actionEvent) {
        SessionManager.getInstance().setSelectedMovie(null);
        teaser.getEngine().load("https://google.com");
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    /**
     * Método para añadir película a la db.
     */
    @FXML
    public void onAddMovie(ActionEvent actionEvent) {
        MovieCopy movieCopy = new MovieCopy(
                null,
                SessionManager.getInstance().getSelectedMovie().getId(),
                SessionManager.getInstance().getCurrentUser(),
                Util.getRandomCondition(movieCopyDAO),
                Util.getRandomPlatform(movieCopyDAO)
        );
        movieCopyDAO.save(movieCopy);
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    /**
     * Método para cambiar el icono de desbloquear y bloquear la edición,
     * además oculta y hace visible el botón de editar la Movie en la db.
     */
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

    /**
     * Método que en función de la propiedad de la clase isUpdate
     * modifica la visibilidad de botones y demás componentes relacionados
     * con la edición.
     */
    private void setVisibilityOfEditComponents() {
        clearImageOnEdit.setVisible(isUpdate);
        editMovieBtn.setVisible(isUpdate);
        titleHbox.setVisible(isUpdate);
        teaserUrlHbox.setVisible(isUpdate);
    }

    /**
     * Método que en función de isUpdate activan o desactivan
     * la posibilidad de que los componentes relacionados con la
     * edición sean editables o no.
     */
    private void setFieldsEditable() {
        descInput.setEditable(isUpdate);
        genreInput.setEditable(isUpdate);
        yearInput.setEditable(isUpdate);
        directorInput.setEditable(isUpdate);
    }

    /**
     * Método para añadir la funcionalidad de arrastrar una imagen al imageView
     * y se cargue la imagen en dicho imageView y además se guarde en la carpeta media.
     */
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

    /**
     * Método que en función de isUpdate habilitan o deshabilitan
     * los componentes.
     */
    private void setFieldsDisabled() {
        descInput.setDisable(!isUpdate);
        genreInput.setDisable(!isUpdate);
        yearInput.setDisable(!isUpdate);
        directorInput.setDisable(!isUpdate);
        titleInputEdit.setDisable(!isUpdate);
    }

    /**
     * Método para eliminar la imagen de la película del detail
     */
    @FXML
    public void onClear(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
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

    /**
     * Método para editar la información de la película de la db.
     */
    @FXML
    public void onEdit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
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
                null,
                posterName
        );
        if(teaserInputEdit.getText().contains("embed")) {
            modifiedMovie.setTeaserUrl(teaserInputEdit.getText());
        } else {
            modifiedMovie.setTeaserUrl(Util.parseYoutubeUrl(teaserInputEdit.getText()));
        }
        movieDAO.update(modifiedMovie);
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }
}

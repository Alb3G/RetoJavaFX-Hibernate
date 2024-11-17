package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.utils.Util;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Clase controladora de la vista AddMovie
 * @author Alberto Guzman
 */
public class AddMovieController implements Initializable {
    private MovieDAO movieDAO = new MovieDAO(HibUtils.getSessionFactory());
    private String posterName;

    @FXML
    private TextField titleInput;
    @FXML
    private TextField genreInput;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField urlInput;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private ImageView posterImage;
    @FXML
    private TextField directorInput;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button clearImgBtn;

    /**
     * Método para incializar los datos de la vista.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDragAndDrop();
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990,LocalDate.now().getYear(),2000,1));
    }

    /**
     * Método para añadir la funcionalidad de arrastrar una imagen al imageView
     * y se cargue la imagen en dicho imageView y además se guarde en la carpeta media.
     */
    private void setDragAndDrop() {
        posterImage.setOnDragOver(dragEvent -> {
            boolean isImage = dragEvent.getDragboard()
                    .getFiles()
                    .getFirst()
                    .getName()
                    .toLowerCase()
                    .matches("^.*\\.(jpg|jpeg|png)$");
            if(dragEvent.getDragboard().hasFiles() && isImage) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
            dragEvent.consume();
        });
        posterImage.setOnDragDropped(dragEvent -> {
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
        });
    }

    /**
     * Método para que en caso de cancelar el proceso de añadir Movie nos llevará
     * a la vista Main de nuevo.
     */
    @FXML
    public void onCancel(ActionEvent actionEvent) {
        posterName = null;
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    /**
     * Método para añadir la película, recuperamos los datos de los inputs
     * creamos una Movie y se añade a la DB.
     */
    @FXML
    public void onConfirm(ActionEvent actionEvent) {
        String title = titleInput.getText();
        String genre = genreInput.getText();
        Integer year = yearSpinner.getValue();
        Integer currentYear = LocalDate.now().getYear();
        String description = descriptionArea.getText();
        String director = directorInput.getText();
        String url = Util.parseYoutubeUrl(urlInput.getText());
        Image poster = posterImage.getImage();
        boolean flag = checkIfInputsAreValid(title, genre, currentYear, description, director, url, poster);
        if(flag) {
            Movie movie = new Movie(
                    null, title, genre, year,
                    description, director,
                    url, posterName
            );
            movieDAO.save(movie);
            posterName = null;
            App.loadFXML("main-view.fxml", "Movies", 1080, 700);
        }
    }

    /**
     * Verifica que los valores de los inputs sean correctos y no estén vacíos.
     * @param title String
     * @param genre String
     * @param currentYear Integer
     * @param description String
     * @param director String
     * @param url String
     * @param poster String
     * @return Boolean si todos los valores son los correctos.
     */
    private boolean checkIfInputsAreValid(
            String title,
            String genre,
            Integer currentYear,
            String description,
            String director,
            String url,
            Image poster
    ) {
        return !title.isBlank() &&
                !genre.isBlank() && yearSpinner.getValue() <= currentYear &&
                !description.isBlank() && !director.isBlank() && !url.isBlank() &&
                poster != null;
    }

    /**
     * Método para poder cambiar la imagen en caso de arrastrar la errónea, el método elimina la imagen
     * de la carpeta media evitando la acumulación de imágenes no deseadas.
     */
    @FXML
    public void onClearImage(ActionEvent actionEvent) {
        String path = System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + posterName;
        posterImage.setImage(null);
        File image = new File(path);
        image.delete();
    }

}
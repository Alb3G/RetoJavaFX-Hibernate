package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.models.Movie;

import java.io.*;
import java.time.LocalDate;

public class AddMovieController {
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

    @FXML
    public void initialize() {
        setDragAndDrop();
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990,LocalDate.now().getYear(),2000,1));
    }

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
                saveImageInMediaFolder(file);
        });
    }

    private void saveImageInMediaFolder(File image) {
        File mediaDir = new File(System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/");
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        File out = new File(mediaDir, image.getName());
        try (
                var bis = new BufferedInputStream(new FileInputStream(image.getCanonicalPath()));
                var bos = new BufferedOutputStream(new FileOutputStream(out))
        ) {
            int bytesRead;
            while ((bytesRead = bis.read()) != -1) {
                bos.write(bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        posterName = null;
        App.loadFXML("main-view.fxml", "Movies", 1080, 700);
    }

    @FXML
    public void onConfirm(ActionEvent actionEvent) {
        String title = titleInput.getText();
        String genre = genreInput.getText();
        Integer year = yearSpinner.getValue();
        Integer currentYear = LocalDate.now().getYear();
        String description = descriptionArea.getText();
        String director = directorInput.getText();
        String url = parseYoutubeUrl(urlInput.getText());
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

    @FXML
    public void onClearImage(ActionEvent actionEvent) {
        String path = System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + posterName;
        posterImage.setImage(null);
        File image = new File(path);
        image.delete();
    }

    private static String parseYoutubeUrl(String url) {
        String[] parts = url.split("watch\\?v=");
        return parts[0] + "embed/" + parts[1].split("&t=")[0] + "?fs=1";
    }

}
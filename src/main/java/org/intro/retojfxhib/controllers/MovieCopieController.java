package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MovieCopieController {

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
    private Button deleteCopyBtn;
    @FXML
    private TextField directorInput;
    @FXML
    private TextArea descInput;

    @FXML
    public void initialize() {

    }

    @FXML
    public void onDelete(ActionEvent actionEvent) {

    }
}

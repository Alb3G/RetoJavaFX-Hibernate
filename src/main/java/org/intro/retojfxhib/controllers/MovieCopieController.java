package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import org.intro.retojfxhib.App;
import org.intro.retojfxhib.HibUtils;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dao.MovieCopyDAO;
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;
import org.intro.retojfxhib.utils.Util;

import java.util.Optional;


public class MovieCopieController {
    private MovieCopyDAO movieCopyDAO = new MovieCopyDAO(HibUtils.getSessionFactory());
    private CopyDTO copyDTO = SessionManager.getInstance().getSelectedCopyDTO();
    private Movie movieOfDto = copyDTO.getMovie();
    private boolean isUpdate = false;

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
    private Button unlockEditBtn;
    @FXML
    private Button editMovieBtn;
    @FXML
    private ImageView unlockIcon;

    @FXML
    public void initialize() {
        moviePoster.setImage(new Image(Util.definePathForImg(movieOfDto.getPoster())));
        teaser.getEngine().load(movieOfDto.getTeaserUrl());
        setConditionText(copyDTO.getCondition());
        platformLabel.setText(copyDTO.getPlatform());
        descInput.setText(movieOfDto.getDescription());
        directorInput.setText(movieOfDto.getDirector());
        genreInput.setText(movieOfDto.getGenre());
        yearInput.setText(movieOfDto.getReleaseYear().toString());
        titleLabel.setText(movieOfDto.getTitle());
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

    @FXML
    public void onEdit(ActionEvent actionEvent) {
        loadEditDialog();
    }

    @FXML
    public void onUnlock(ActionEvent actionEvent) {
        isUpdate = !isUpdate;
        editMovieBtn.setVisible(isUpdate);
        if(isUpdate) {
            unlockIcon.setImage(new Image(Util.definePathForImg("unlock.png")));
        } else {
            unlockIcon.setImage(new Image(Util.definePathForImg("lock.png")));
        }
    }


    private void loadEditDialog() {
        // Crear el diálogo personalizado
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Copy");
        dialog.setHeaderText("Edit Platform and Condition");

        // Obtener el DialogPane y aplicar estilos
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/intro/retojfxhib/css/darkTheme.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        // Configurar los botones
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Para asegurar que los botones tienen el estilo correcto
        dialogPane.lookupButton(saveButtonType).getStyleClass().add("button");
        dialogPane.lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // Crear el grid para el contenido
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("background");

        // Crear los campos de entrada y aplicar estilos
        ComboBox<String> platformCombo = new ComboBox<>();
        platformCombo.getItems().addAll(movieCopyDAO.getCopiesPlatform());
        platformCombo.setValue(platformLabel.getText());
        platformCombo.setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white; -fx-prompt-text-fill: white;");

        ComboBox<String> conditionCombo = new ComboBox<>();
        conditionCombo.getItems().addAll(movieCopyDAO.getCopiesCondition());
        conditionCombo.setValue(conditionLabel.getText());
        conditionCombo.setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white; -fx-prompt-text-fill: white;");

        // Crear y estilizar las labels
        Label platformLabel = new Label("Platform:");
        Label conditionLabel = new Label("Condition:");
        platformLabel.getStyleClass().add("label-bright");
        conditionLabel.getStyleClass().add("label-bright");

        grid.add(platformLabel, 0, 0);
        grid.add(platformCombo, 1, 0);
        grid.add(conditionLabel, 0, 1);
        grid.add(conditionCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Estilizar los botones del diálogo
        dialogPane.lookupButton(saveButtonType).getStyleClass().add("button");
        dialogPane.lookupButton(ButtonType.CANCEL).getStyleClass().add("button");

        // Añadir estilos específicos para los ComboBox
        String comboBoxStyle =
                ".combo-box { -fx-background-color: #1d1d1d; -fx-text-fill: white; }" +
                        ".combo-box .cell { -fx-text-fill: white; -fx-background-color: #1d1d1d; }" +
                        ".combo-box-popup .list-view { -fx-background-color: #1d1d1d; }" +
                        ".combo-box-popup .list-cell { -fx-text-fill: white; -fx-background-color: #1d1d1d; }" +
                        ".combo-box-popup .list-cell:hover { -fx-background-color: #3a3a3a; }";

        dialogPane.setStyle(comboBoxStyle);

        // Convertir el resultado al hacer clic en save
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(platformCombo.getValue(), conditionCombo.getValue());
            }
            return null;
        });

        // Mostrar el diálogo y procesar el resultado
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(platformCondition -> {
            // Actualizar la copia en la base de datos
            CopyDTO currentCopy = SessionManager.getInstance().getSelectedCopyDTO();
            MovieCopy copy = SessionManager.getInstance().getSelectedCopyDTO().getCopy();
            copy.setPlatform(platformCondition.getKey());
            copy.setMovieCondition(platformCondition.getValue());
            movieCopyDAO.update(copy);

            // Actualizar la UI
            platformLabel.setText(platformCondition.getKey());
            setConditionText(platformCondition.getValue());
        });
    }
}
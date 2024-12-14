package org.intro.retojfxhib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private Button moreThanOneCopyBtn;
    @FXML
    private Button badConditionBtn;
    @FXML
    private Button allMoviesInfoBtn;
    @FXML
    private WebView pdfWebView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void onAllMoviesReport(ActionEvent actionEvent) {}

    @FXML
    public void onMoreThanOneCopyReport(ActionEvent actionEvent) {}

    @FXML
    public void onBadConditionReport(ActionEvent actionEvent) {}
}

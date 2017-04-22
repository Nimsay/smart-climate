package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class AboutController {
    @FXML
    Hyperlink OpenMeteoFranceCom;

    @FXML void OpenMeteoFranceCom(ActionEvent event) throws Exception{
        new ProcessBuilder("x-www-browser", "http://sources.meteofrance.fr").start();
    }
}

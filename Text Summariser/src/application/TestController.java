package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class TestController implements Initializable {

    @FXML 
    private TextField firstBox, secondBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstBox.textProperty().isEmpty();
		secondBox.textProperty().isEmpty();
	}
}
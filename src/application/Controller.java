package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import gate.creole.ResourceInstantiationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class Controller implements Initializable {

	@FXML
	private TextArea firstText, secondText, thirdText, firstSum, secondSum, thirdSum;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		org.apache.log4j.BasicConfigurator.configure();
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		try {			
			firstText.setText(First.getFirst());
			firstSum.setText(First.getFirstSum());
			secondText.setText(Second.getSecond());
			secondSum.setText(Second.getSecondSum());
			thirdText.setText(Third.getThird());
			thirdSum.setText(Third.getThirdSum());

		} catch (IOException | FailingHttpStatusCodeException | ResourceInstantiationException e) {
			e.printStackTrace();
		}
	}
}
package application;

import java.io.IOException;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;

public class TestGui extends GuiTest {

	@Override
	protected Parent getRootNode() {
		Parent parent = null;
		try {
			parent = FXMLLoader.load(getClass().getResource("GUI.fxml"));
			return parent;
		} catch (IOException ex) {
			ex.getStackTrace();
		}
		return parent;
	}

	@Test
	public void TestInserts() {
		TextArea firstBox = find("#firstSum");
		firstBox.setText("The first box has some text");
		verifyThat("#firstSum", hasText("The first box has some text"));
		
		TextArea secondBox = find("#firstText");
		secondBox.setText("The second box has some text");
		verifyThat("#firstText", hasText("The second box has some text"));
	}
}
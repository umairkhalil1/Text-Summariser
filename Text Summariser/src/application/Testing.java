package application;

import java.util.List;
import java.util.logging.Level;

import org.junit.Test;
import org.junit.Assert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Testing {

	@Test
	public void testConnection() throws Exception {
		
		org.apache.log4j.BasicConfigurator.configure();
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		final WebClient webClient = new WebClient();
		final HtmlPage page = webClient.getPage("https://www.gov.uk/government/policies/brexit");
		Assert.assertEquals("Brexit - GOV.UK", page.getTitleText());

		List<HtmlAnchor> anchors = page.getAnchors();
		HtmlPage page1 = anchors.get(19).click();
		Assert.assertEquals("The Great Repeal Bill: White Paper - GOV.UK", page1.getTitleText());
		Assert.assertEquals("https://www.gov.uk/government/publications/the-great-repeal-bill-white-paper",
				page1.getUrl().toString());
		webClient.close();
	}
}
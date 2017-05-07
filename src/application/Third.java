package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.LanguageResource;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.OffsetComparator;

public class Third {
		
	public static String getThird() throws FailingHttpStatusCodeException, IOException {
		String thirdText = "";
		try (WebClient webClient = new WebClient()) { //Initialise new web connection
			final HtmlPage page = webClient.getPage("https://www.gov.uk/government/policies/brexit"); //Specify webPage to Crawl 
			List<HtmlAnchor> anchors = page.getAnchors(); //Store anchors in a list
			HtmlPage page1 = anchors.get(20).click(); //Click the third link
			DomNodeList<DomNode> paragraphs = page1.querySelectorAll("div[class=govspeak] p"); //Store <p> elements within the div tag
			for (DomNode p : paragraphs) 
				thirdText += p.asText() + System.lineSeparator(); //Store retrieved text into String
		}
		return thirdText;
	}
	
	public static String getThirdSum() throws FailingHttpStatusCodeException, IOException, ResourceInstantiationException {

		SerialAnalyserController controller;
		ProcessingResource tokeniser, statistics, split; //Declare default GATE variables to use
		ProcessingResource termFreq, scorer, position; //Declare SUMMA variables to use 
		LanguageResource idfTable; //Declare IDF table
		String sumText = ""; 
			try {
			Gate.init(); //Initialise GATE
			
			//Register and use GATE Controller, Sentence Splitter and Tokeniser  
			Gate.getCreoleRegister().registerDirectories(new URL("file:///Applications/GATE_Developer_8.2/plugins/ANNIE"));
			Gate.getCreoleRegister().registerDirectories(new URL("file:///Applications/GATE_Developer_8.2/plugins/summa_plugin"));
			controller = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController",Factory.newFeatureMap(), Factory.newFeatureMap(),"Summariser");
			tokeniser = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser",Factory.newFeatureMap());
			split = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter",Factory.newFeatureMap(), Factory.newFeatureMap());
			controller.add(tokeniser);
			controller.add(split);
			
			//Register and the IDF table to the GATE controller
			idfTable = (LanguageResource) Factory.createResource("summa.resources.frequency.InvertedTable");
			statistics = (ProcessingResource) Factory.createResource("summa.resources.frequency.NEFrequency");
			statistics.setParameterValue("table", idfTable);
			controller.add(statistics);
						
			//Register and add SUMMA resources to the controller
			termFreq = (ProcessingResource) Factory.createResource("summa.scorer.SentenceTermFrequency");
			position = (ProcessingResource) Factory.createResource("summa.scorer.PositionScorer"); 
			scorer = (ProcessingResource) Factory.createResource("summa.SimpleSummarizer");
			controller.add(termFreq);
			controller.add(position);
			controller.add(scorer);
			
			//Score each sentence within the ArrayList using the SUMMA resources  
			ArrayList<String> features = new ArrayList<String>();
			features.add("tf_score"); //Term frequency
			features.add("position_score"); //Position Score
			features.add("paragraph_score"); //Paragraph Score

			//Set weights according to the SUMMA resources
			ArrayList<String> ws = new ArrayList<String>();
			ws.add("0.50"); //Term frequency
			ws.add("0.30"); //Position Score
			ws.add("0.20"); //Paragraph Score

			//Set parameters of the score
			scorer.setParameterValue("sumFeatures", features);
			scorer.setParameterValue("sumWeigths", ws);

			Corpus corpus = Factory.newCorpus(""); //Initialise Corpus
			Document doc = Factory.newDocument(getThird()); // Store method to get text in Corpus
			corpus.add(doc);
			controller.setCorpus(corpus);
			controller.execute();
			sumText = (applySummary(doc));	//Call applySummary to summarise doc and save as String
					
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (GateException ge) {
				ge.printStackTrace();
			}
			return sumText; //Return summarised text for GUI
	}
		
	public static String applySummary(Document doc) {
		String summary = "";
		String txt = doc.getContent().toString(); //Converts doc to be summarised to String
		AnnotationSet sentences = doc.getAnnotations("EXTRACT").get("Sentence"); //Stores summary in Annotationset
		Annotation sentence; //Apply annotations to each sentence
		Long start, end;
		ArrayList<Annotation> sentList = new ArrayList<Annotation>(sentences); //Stores the 
		Collections.sort(sentList, new OffsetComparator()); //Sorts the scores of the sentences
		for (int i = 0; i < sentList.size(); i++) { 
			sentence = sentList.get(i); //Gets sentence from ArrayList
			start = sentence.getStartNode().getOffset(); //Get annotations from starting node
			end = sentence.getEndNode().getOffset(); //  //Get annotations till the end node
			summary = summary + txt.substring(start.intValue(), end.intValue()) + "\n"; //Concatenates sentences into final summary
		}
		return summary;
	}
}
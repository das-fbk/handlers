package eu.fbk.das.domainobject.executable.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DocumentVariableUtils {

	public static Document newEmptyDocument() {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc;

		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		doc = builder.newDocument();
		return doc;
	}

	public static Element newElement(String elementName, String elementValue) {
		Document doc = newEmptyDocument();
		Element el = doc.createElement(elementName);
		Node node = doc.createTextNode(elementValue);
		el.appendChild(node);
		return el;
	}
}

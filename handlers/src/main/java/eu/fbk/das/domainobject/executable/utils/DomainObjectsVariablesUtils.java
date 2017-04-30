package eu.fbk.das.domainobject.executable.utils;

import org.w3c.dom.Element;

import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class DomainObjectsVariablesUtils {

	// method for the creation of new variables at runtime
	public static VariableType newVariable(String hoaaName, String elementName,
			String elementValue) {
		VariableType el = new VariableType();
		Element var = DocumentVariableUtils.newElement(elementName,
				elementValue);
		el.setName(hoaaName + elementName);
		el.setContent(var);

		return el;
	}
}

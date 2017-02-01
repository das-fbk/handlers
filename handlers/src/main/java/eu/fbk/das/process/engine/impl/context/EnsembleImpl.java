package eu.fbk.das.process.engine.impl.context;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.das.process.engine.impl.context.api.Ensemble;

public class EnsembleImpl implements Ensemble {

    private String name;

    private List<String> elements = new ArrayList<String>();

    public EnsembleImpl(String name) {
	this.name = name;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public List<String> getRoles() {
	return elements;
    }

    @Override
    public void add(String element) {
	elements.add(element);
    }

}

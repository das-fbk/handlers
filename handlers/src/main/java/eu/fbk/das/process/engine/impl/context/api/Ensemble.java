package eu.fbk.das.process.engine.impl.context.api;

import java.util.List;

public interface Ensemble {

    public String getName();

    public List<String> getRoles();

    public void add(String element);
}

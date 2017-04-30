package eu.fbk.das.process.engine.impl.context.api;

import java.util.List;

/**
 * A generic interface for ProcessEngine context
 */
public interface Context {

    public List<Ensemble> getEnsembles();

    public void setEnembles(List<Ensemble> ensembles);

    public List<Role> getRoles();

    public void setRoles(List<Role> roles);
}

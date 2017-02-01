package eu.fbk.das.process.engine.impl.context.util;

import java.util.List;
import java.util.Optional;

import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcVar;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public final class ContextUtil {

    public static Optional<DomainObjectInstance> findDomainObjectForRole(
	    ProcessEngine pe, ProcessDiagram proc, String roleType) {
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	List<DomainObjectInstance> corr = pe.getCorrelated(doi);
	for (DomainObjectInstance pcorr : corr) {
	    if (pcorr.getType().equals(roleType)) {
		return Optional.of(pcorr);
	    }
	}
	return Optional.empty();
    }

    public static boolean isUsingFlexibus(ProcessEngine pe,
	    DomainObjectInstance doi) {
	Optional<ProcVar> transportations = getVariable(pe, doi,
		"TRANSPORTATIONS");
	if (transportations.isPresent()) {
	    ProcVar var = transportations.get();
	    if (var.getValue().contains("flexibus")) {
		return true;
	    }
	}
	return false;
    }

    private static Optional<ProcVar> getVariable(ProcessEngine pe,
	    DomainObjectInstance doi, String variableName) {
	ProcVar variable = pe.getVariablesFor(doi.getProcess(), variableName);
	if (variable != null) {
	    return Optional.of(variable);
	}
	return Optional.empty();
    }

}

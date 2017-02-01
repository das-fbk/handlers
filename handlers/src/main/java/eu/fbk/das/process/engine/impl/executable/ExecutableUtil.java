package eu.fbk.das.process.engine.impl.executable;

import java.util.List;

import eu.fbk.das.process.engine.api.domain.ObjectDiagram;

public final class ExecutableUtil {

    private ExecutableUtil() {
    }

    public static boolean isInState(List<ObjectDiagram> knowledge,
	    String dpName, String dpValue) {
	return knowledge
		.stream()
		.filter(k -> k.getType().equals(dpName)
			&& k.getCurrentState().equals(dpValue)).count() == 1;
    }

}

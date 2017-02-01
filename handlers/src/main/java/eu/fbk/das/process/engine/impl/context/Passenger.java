package eu.fbk.das.process.engine.impl.context;

import eu.fbk.das.process.engine.impl.context.api.Role;

public class Passenger implements Role {

    public String name;

    @Override
    public String getName() {
	return name;
    }

}

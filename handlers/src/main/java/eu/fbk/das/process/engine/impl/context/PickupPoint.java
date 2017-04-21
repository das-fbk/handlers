package eu.fbk.das.process.engine.impl.context;

public class PickupPoint {

    public String name;

    public PickupPoint(String name) {
	this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof PickupPoint) {
	    if (name.equals(((PickupPoint) obj).name)) {
		return true;
	    }
	}
	return false;
    }
}

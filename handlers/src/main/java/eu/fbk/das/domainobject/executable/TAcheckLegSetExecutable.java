package eu.fbk.das.domainobject.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;

public class TAcheckLegSetExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;

	public TAcheckLegSetExecutable(ProcessEngine pe) {
		this.pe = pe;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {

		EffectType effect = new EffectType();
		Event event = new Event();
		event.setDpName("TravelAssistant");
		event.setEventName("completeAssistance");
		effect.getEvent().add(event);
		pe.applyEffect(proc, effect);
		pa.setExecuted(true);
	}

}

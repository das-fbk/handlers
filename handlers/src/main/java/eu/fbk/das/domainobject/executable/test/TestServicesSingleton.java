package eu.fbk.das.domainobject.executable.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestServicesSingleton {

	private static TestServicesSingleton instance = null;
	private static String workingDir = "C:/Users/Martina/git/process-engine/process-engine-impl/src/test/resources/ICSOC2017";
	private static final String servicesAnalysis = workingDir
			+ "/servicesAnalysis.csv";

	private static String csv = ",";
	private String toLog = "";

	protected TestServicesSingleton() {
		// set the header of the resulting csv file
		this.toLog += "User_Id" + csv + "Service_Name" + csv + "Exec_Time"
				+ csv + "Alternative_Number" + csv + "Avg_Alt_Segments\n";
	}

	public static TestServicesSingleton getInstance() {

		if (instance == null) {
			instance = new TestServicesSingleton();
		}
		return instance;
	}

	public String getToLog() {
		return toLog;
	}

	public void setToLog(String doInstanceId, String serviceType,
			long execTime, int numAlternatives, long avgSegments) {
		double seconds = (execTime) / 1E9;
		this.toLog += doInstanceId + csv + serviceType + csv + seconds + csv
				+ numAlternatives + csv + avgSegments + "\n";
	}

	public File getServicesTestFile() {
		try {
			File f = new File(servicesAnalysis);
			f.delete();
			f.createNewFile();
			FileWriter fw = new FileWriter(f, true);
			fw.write(toLog);
			fw.close();
			return f;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

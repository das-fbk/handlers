package eu.fbk.das.domainobject.executable.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestRefinementSingleton {

	private static TestRefinementSingleton instance = null;
	private static String workingDir = "C:/Users/Martina/git/process-engine/process-engine-impl/src/test/resources/ICSOC2017";
	private static final String refinementAnalysis = workingDir
			+ "/refinementAnalysis.csv";

	private static String csv = ",";
	private String toLog = "";

	protected TestRefinementSingleton() {
		// set the header of the resulting csv file
		this.toLog += "User_Id" + csv + "Abstract_Activity_Name" + csv
				+ "Refinement_Time\n";
	}

	public static TestRefinementSingleton getInstance() {

		if (instance == null) {
			instance = new TestRefinementSingleton();
		}
		return instance;
	}

	public String getToLog() {
		return toLog;
	}

	public void setToLog(String userId, String activityName, long refinementTime) {
		// TimeUnit.SECONDS.convert(refinementTime, TimeUnit.NANOSECONDS); //
		// questo non arrotonda
		// seconds = Math.round(seconds * Math.pow(10, 8)) / Math.pow(10, 8);
		double seconds = (refinementTime) / 1E9; // the same as /
															// 1000000000
		this.toLog += userId + csv + activityName + csv + seconds + "\n";
	}

	public File getAdaptationTestFile() {
		try {
			File f = new File(refinementAnalysis);
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

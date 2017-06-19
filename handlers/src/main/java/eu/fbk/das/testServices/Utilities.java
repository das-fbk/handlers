package eu.fbk.das.testServices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Antonio Bucchiarone
 * 
 */
public class Utilities {

	/**
	 *
	 * Thanks to assylias - https://assylias.wordpress.com/
	 * 
	 * http://stackoverflow.com/questions/22380890/generate-n-random-numbers-
	 * whose-sum-is-m-and-all-numbers-should-be-greater-than
	 *
	 */
	public static List<Integer> generateRandomValues(int targetSum,
			int numberOfDraws) {
		Random r = new Random();
		List<Integer> load = new ArrayList<>();

		// random numbers
		int sum = 0;
		for (int i = 0; i < numberOfDraws; i++) {
			int next = r.nextInt(targetSum) + 1;
			load.add(next);
			sum += next;
		}

		// scale to the desired target sum
		double scale = 1d * targetSum / sum;
		sum = 0;
		for (int i = 0; i < numberOfDraws; i++) {
			load.set(i, (int) (load.get(i) * scale));
			sum += load.get(i);
		}

		// take rounding issues into account
		while (sum++ < targetSum) {
			int i = r.nextInt(numberOfDraws);
			load.set(i, load.get(i) + 1);
		}

		return load;
	}

	public static void genericWriteFile(List<? extends Loggable> loggables,
			String fileName) {

		// Delimiter used in CSV file
		String commaDelimiter = ",";
		String newLineSeparator = "\n";

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter
					.append(loggables.get(0).getCsvFileHeader(commaDelimiter));
			// Add a new line separator after the header
			fileWriter.append(newLineSeparator);

			// Write a new treatment object list to the CSV file
			for (Loggable currentLoggable : loggables) {

				fileWriter.append(currentLoggable.toCsv(commaDelimiter));
				fileWriter.append(newLineSeparator);

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter.");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// System.out.println("Error while flushing/closing
				// fileWriter.");
				e.printStackTrace();
			}

		}
	}

	public static void genericWriteFileNew(List<ExperimentResult> results,
			String fileName) {
		// Delimiter used in CSV file
		String commaDelimiter = ",";
		String newLineSeparator = "\n";

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(results.get(0).getCsvFileHeader(commaDelimiter));
			// Add a new line separator after the header
			fileWriter.append(newLineSeparator);

			// Write a new treatment object list to the CSV file
			for (ExperimentResult currentLoggable : results) {

				fileWriter.append(currentLoggable.toCsv(commaDelimiter));
				fileWriter.append(newLineSeparator);

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter.");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// System.out.println("Error while flushing/closing
				// fileWriter.");
				e.printStackTrace();
			}

		}

	}

	public static String randomCityBikes() {

		String[] names = { "e-motion-trento", "e-motion-rovereto",
				"olbia-bike", "madison", "callabike-munchen",
				"callabike-wiesbaden", "nextbike-leipzig", "relay-atlanta",
				"bici-ferrol-terra", "muybici", "velivert",
				"cyclopolis-rhodes", "bicincitta-montecatini-terme",
				"nextbike-stirling", "girocleta", "opole-bike", "veturilo" };

		int index = (int) (Math.random() * names.length);
		String name = names[index];

		return name;
	}

	public static String randomCityGlobal() {

		String[] names = { "Trento", "Roma", "Malaga", "Barcellona", "Madrid",
				"Berlino", "Varsavia", "Amsterdam", "Milano", "Torino",
				"Londra", "Parigi", "Lisbona", "Mosca", "Stoccolma", "Miami",
				"Casablanca", "Oslo", "Caracas", "Brasilia", "Montevideo",
				"Pretoria", "Melbourne", "Pechino" };

		int index = (int) (Math.random() * names.length);
		String name = names[index];

		return name;
	}

	public static String randomLondonJPCode() {

		String[] names = { "SE1 0RN", "SE11 5EP", "N2 8AW", "N2 9JN", "N3 2BX",
				"NW7 1RH", "NW7 4TB", "NW7 3GJ", "HA3 6EL", "HA5 4LB",
				"OX5 2QQ", "OX5 2QX" };

		int index = (int) (Math.random() * names.length);
		String name = names[index];

		return name;
	}

	public static String randomCityItaly() {

		String[] names = { "Trento", "Roma", "Milano", "Firenze", "Padova",
				"Palermo", "Pescara", "Torino", "Foggia", "Bologna", "Venezia",
				"Verona", "Genova", "Napoli", "Ancona", "Perugia" };
		int index = (int) (Math.random() * names.length);
		String name = names[index];

		return name;
	}

	public static String randomCityTrento() {

		String[] names = { "Trento", "Cadine", "Rovereto", "Mezzocorona",
				"Molveno", "Folgaria", "Arco", "Riva del Garda",
				"Pergine Valsugana", "Borgo Valsugana", "Novaledo",
				"Brentonico", "Mori", "Besenello", "Aldeno", "Sopramonte",
				"Ravina", "Romagnano", "Lavis", "Civezzano", "Gardolo",
				"Vattaro", "Caldonazzo" };
		int index = (int) (Math.random() * names.length);
		String name = names[index];

		return name;
	}

}

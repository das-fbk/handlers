package eu.fbk.das.testServices;

public abstract class Loggable {

    abstract String toCsv(String commaDelimiter);

    abstract String getCsvFileHeader(String commaDelimiter);

}

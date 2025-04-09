package bmx.batch.java.importing.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

	public static LocalDate parseDate(String dateStr) {
		DateTimeFormatter[] formatters = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("M/d/yyyy"),
				DateTimeFormatter.ofPattern("MM/dd/yyyy"), DateTimeFormatter.ofPattern("yyyy-MM-dd") };

		for (DateTimeFormatter formatter : formatters) {
			try {
				return LocalDate.parse(dateStr, formatter);
			} catch (DateTimeParseException e) {
				// Ignore and try the next format
			}
		}

		// If all formats fail, throw an exception or handle it as needed
		throw new DateTimeParseException("Unable to parse the date: " + dateStr, dateStr, 0);
	}

}

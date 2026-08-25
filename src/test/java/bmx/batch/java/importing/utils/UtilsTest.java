package bmx.batch.java.importing.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Utils Unit Tests")
class UtilsTest {

    @Test
    @DisplayName("Should parse date with format M/d/yyyy")
    void testParseDateWithSingleDigitMonthDay() {
        // Arrange
        String dateStr = "1/5/2024";

        // Act
        LocalDate result = Utils.parseDate(dateStr);

        // Assert
        assertNotNull(result, "Parsed date should not be null");
        assertEquals(LocalDate.of(2024, 1, 5), result);
    }

    @Test
    @DisplayName("Should parse date with format MM/dd/yyyy")
    void testParseDateWithDoubleDigitMonthDay() {
        // Arrange
        String dateStr = "12/25/2024";

        // Act
        LocalDate result = Utils.parseDate(dateStr);

        // Assert
        assertNotNull(result, "Parsed date should not be null");
        assertEquals(LocalDate.of(2024, 12, 25), result);
    }

    @Test
    @DisplayName("Should parse date with format MM/dd/yyyy when M/d/yyyy fails")
    void testParseDateWithPaddedFormat() {
        // Arrange
        String dateStr = "03/15/2024";

        // Act
        LocalDate result = Utils.parseDate(dateStr);

        // Assert
        assertNotNull(result, "Parsed date should not be null");
        assertEquals(LocalDate.of(2024, 3, 15), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1/1/2024", "01/01/2024"})
    @DisplayName("Should parse same date from different M/d/yyyy formats")
    void testParseDateDifferentFormats(String dateStr) {
        // Act
        LocalDate result = Utils.parseDate(dateStr);

        // Assert
        assertNotNull(result, "Parsed date should not be null");
        assertEquals(LocalDate.of(2024, 1, 1), result);
    }

    @Test
    @DisplayName("Should throw DateTimeParseException for invalid date format")
    void testParseDateInvalidFormat() {
        // Arrange
        String invalidDateStr = "2024/25/12";

        // Act & Assert
        assertThrows(
                DateTimeParseException.class,
                () -> Utils.parseDate(invalidDateStr),
                "Expected DateTimeParseException for invalid date format"
        );
    }

    @Test
    @DisplayName("Should throw DateTimeParseException for non-date string")
    void testParseDateWithNonDateString() {
        // Arrange
        String invalidDateStr = "not-a-date";

        // Act & Assert
        assertThrows(
                DateTimeParseException.class,
                () -> Utils.parseDate(invalidDateStr),
                "Expected DateTimeParseException for non-date string"
        );
    }

    @Test
    @DisplayName("Should throw DateTimeParseException for empty string")
    void testParseDateWithEmptyString() {
        // Arrange
        String emptyDateStr = "";

        // Act & Assert
        assertThrows(
                DateTimeParseException.class,
                () -> Utils.parseDate(emptyDateStr),
                "Expected DateTimeParseException for empty string"
        );
    }
}

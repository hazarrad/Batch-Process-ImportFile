package bmx.batch.java.importing.processor;

import bmx.batch.java.importing.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MyItemProcessor Unit Tests")
class MyItemProcessorTest {

    private MyItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new MyItemProcessor();
    }

    @Test
    @DisplayName("Should process valid customer successfully")
    void testProcessValidCustomer() throws Exception {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("CUST001")
                .firstName("John")
                .lastName("Doe")
                .company("ABC Corp")
                .city("New York")
                .country("USA")
                .phone1("123-456-7890")
                .phone2("098-765-4321")
                .email("john.doe@example.com")
                .subscriptionDate(LocalDate.of(2024, 1, 15))
                .build();

        // Act
        Customer result = processor.process(customer);

        // Assert
        assertNotNull(result, "Processed customer should not be null");
        assertEquals("CUST001", result.getCustomerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("ABC Corp", result.getCompany());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when customerId is null")
    void testProcessCustomerWithNullId() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId(null)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processor.process(customer),
                "Expected IllegalArgumentException for null customerId"
        );

        assertEquals("Customer ID is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when customerId is empty")
    void testProcessCustomerWithEmptyId() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("")
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processor.process(customer),
                "Expected IllegalArgumentException for empty customerId"
        );

        assertEquals("Customer ID is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when customerId contains only whitespace")
    void testProcessCustomerWithWhitespaceId() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("   ")
                .firstName("Alice")
                .lastName("Williams")
                .email("alice.williams@example.com")
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processor.process(customer),
                "Expected IllegalArgumentException for whitespace-only customerId"
        );

        assertEquals("Customer ID is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should process customer with minimal required fields")
    void testProcessCustomerWithMinimalFields() throws Exception {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("CUST002")
                .build();

        // Act
        Customer result = processor.process(customer);

        // Assert
        assertNotNull(result, "Processed customer should not be null");
        assertEquals("CUST002", result.getCustomerId());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
    }
}

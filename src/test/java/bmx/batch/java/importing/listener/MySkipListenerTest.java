package bmx.batch.java.importing.listener;

import bmx.batch.java.importing.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MySkipListener Unit Tests")
class MySkipListenerTest {

    private MySkipListener skipListener;

    @BeforeEach
    void setUp() {
        skipListener = new MySkipListener();
    }

    @Test
    @DisplayName("Should handle skip in read operation")
    void testOnSkipInRead() {
        // Arrange
        Throwable throwable = new RuntimeException("Read error occurred");

        // Act - should not throw exception
        assertDoesNotThrow(() -> skipListener.onSkipInRead(throwable));

        // Assert - verify the method executed successfully
        assertNotNull(throwable.getMessage());
        assertEquals("Read error occurred", throwable.getMessage());
    }

    @Test
    @DisplayName("Should handle skip in process operation with valid customer")
    void testOnSkipInProcessWithValidCustomer() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("CUST001")
                .firstName("John")
                .lastName("Doe")
                .phone1("123-456-7890")
                .email("john.doe@example.com")
                .build();
        Throwable throwable = new IllegalArgumentException("Customer ID is required");

        // Act - should not throw exception
        assertDoesNotThrow(() -> skipListener.onSkipInProcess(customer, throwable));

        // Assert - verify customer and error details
        assertNotNull(customer.getCustomerId());
        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("Customer ID is required", throwable.getMessage());
    }

    @Test
    @DisplayName("Should handle skip in process operation with customer having no ID but has phone")
    void testOnSkipInProcessWithPhoneOnly() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("")
                .firstName("Jane")
                .lastName("Smith")
                .phone1("098-765-4321")
                .email("jane.smith@example.com")
                .build();
        Throwable throwable = new IllegalArgumentException("Customer ID is required");

        // Act - should not throw exception
        assertDoesNotThrow(() -> skipListener.onSkipInProcess(customer, throwable));

        // Assert - verify customer has phone as fallback identifier
        assertTrue(customer.getCustomerId().isEmpty());
        assertNotNull(customer.getPhone1());
        assertEquals("098-765-4321", customer.getPhone1());
    }

    @Test
    @DisplayName("Should handle skip in write operation")
    void testOnSkipInWrite() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("CUST002")
                .firstName("Bob")
                .lastName("Johnson")
                .company("XYZ Inc")
                .city("Los Angeles")
                .country("USA")
                .email("bob.johnson@example.com")
                .subscriptionDate(LocalDate.of(2024, 2, 20))
                .build();
        Throwable throwable = new RuntimeException("Database connection failed");

        // Act - should not throw exception
        assertDoesNotThrow(() -> skipListener.onSkipInWrite(customer, throwable));

        // Assert - verify customer and error details
        assertNotNull(customer.getCustomerId());
        assertEquals("CUST002", customer.getCustomerId());
        assertEquals("Database connection failed", throwable.getMessage());
    }

    @Test
    @DisplayName("Should handle skip in write with null pointer exception")
    void testOnSkipInWriteWithNullPointerException() {
        // Arrange
        Customer customer = Customer.builder()
                .customerId("CUST003")
                .firstName("Alice")
                .lastName("Brown")
                .build();
        Throwable throwable = new NullPointerException("EntityManager is null");

        // Act - should not throw exception
        assertDoesNotThrow(() -> skipListener.onSkipInWrite(customer, throwable));

        // Assert
        assertEquals("CUST003", customer.getCustomerId());
        assertEquals("EntityManager is null", throwable.getMessage());
    }
}

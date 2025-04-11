package bmx.batch.java.importing.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import bmx.batch.java.importing.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		// Simple processor that just converts text to uppercase
		if (item.getCustomerId() == null || item.getCustomerId().trim().isEmpty()) {
			log.error("Error while Processing customer {}", item.getCustomerId());
			throw new IllegalArgumentException("Customer ID is required");
		}
		
		return item;
	}
}
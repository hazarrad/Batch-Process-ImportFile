package bmx.batch.java.importing.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import bmx.batch.java.importing.model.Customer;

@Component
public class MyItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		if (item.getCustomerId() == null || item.getCustomerId().trim().isEmpty()) {
			throw new IllegalArgumentException("Customer ID is required");
		}

		return item;
	}
}
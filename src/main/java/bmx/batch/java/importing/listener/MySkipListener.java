package bmx.batch.java.importing.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import bmx.batch.java.importing.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MySkipListener implements SkipListener<Customer, Customer> {

	public MySkipListener() {
		log.info("MySkipListener has been created.");

	}

	@Override
	public void onSkipInRead(Throwable t) {
		System.out.println("hey error");
		log.info("Skipped reading — Reason: {}", t.getMessage());
	}

	@Override
	public void onSkipInProcess(Customer item, Throwable t) {
		System.out.println("hey error");

		log.info("Skipped processing for customer: {} — Reason: {}", item.getCustomerId(), t.getMessage());
	}

	@Override
	public void onSkipInWrite(Customer item, Throwable t) {
		System.out.println("hey error");

		log.info("Skipped writing customer: {} — Reason: {}", item.getCustomerId(), t.getMessage());
	}
}

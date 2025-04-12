package bmx.batch.java.importing.listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import bmx.batch.java.importing.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MyItemWriteListener implements ItemWriteListener<Customer> {

	private static int chunkCounter = 0; // Counter to track chunks

	public MyItemWriteListener() {
		log.info("MyItemWriteListener has been created.");

	}

	@Override
	public void beforeWrite(Chunk<? extends Customer> items) {
		chunkCounter++; // Increment the chunk counter
		log.info("Starting to process chunk #{}", chunkCounter + " with " + items.size() + " items.");
	}

	@Override
	public void afterWrite(Chunk<? extends Customer> items) {
		log.info("Finished processing chunk #{}", chunkCounter + " with " + items.size() + " items.");

	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends Customer> items) {
		log.error("Error while writing chunk #{}", chunkCounter + ": " + exception.getMessage());

	}

}
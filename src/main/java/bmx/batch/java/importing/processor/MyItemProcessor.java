package bmx.batch.java.importing.processor;

import org.springframework.batch.item.ItemProcessor;

public class MyItemProcessor implements ItemProcessor<String, String> {
	@Override
	public String process(String item) throws Exception {
		// Simple processor that just converts text to uppercase
		return item.toUpperCase();
	}
}
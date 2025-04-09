package bmx.batch.java.importing.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class MyItemWriter implements ItemWriter<String>{

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		chunk.forEach(System.out::println);
		
	}

}

package bmx.batch.java.importing.listener;

import bmx.batch.java.importing.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyChunkListener implements ChunkListener {
    int chunkCounter = 0;

    public MyChunkListener() {
        log.warn("MyChunkListener has been created.");

    }

    @Override
    public void beforeChunk(ChunkContext context) {
        chunkCounter++;
        log.info("Starting chunk #{}", chunkCounter);
    }
}

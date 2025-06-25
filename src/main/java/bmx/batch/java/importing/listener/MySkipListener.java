package bmx.batch.java.importing.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import bmx.batch.java.importing.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MySkipListener implements SkipListener<Customer, Customer> {

    public MySkipListener() {
        log.warn("MySkipListener has been created.");

    }

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn("Skipped reading — Reason: {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Customer item, Throwable t) {
        String customer = !item.getCustomerId().isEmpty() ? item.getCustomerId() : item.getPhone1();
        log.warn("Skipped processing for customer: {} — Reason: {}", customer, t.getMessage());
    }

    @Override
    public void onSkipInWrite(Customer item, Throwable t) {
        log.warn("Skipped writing customer: {} — Reason: {}", item.getCustomerId(), t.getMessage());
    }

}

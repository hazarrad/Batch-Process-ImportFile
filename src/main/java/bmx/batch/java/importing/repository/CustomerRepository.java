package bmx.batch.java.importing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bmx.batch.java.importing.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

}

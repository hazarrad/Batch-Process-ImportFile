package bmx.batch.java.importing.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {

	@Id
	String customerId;
	String firstName;
	String lastName;
	String company;
	String city;
	String country;
	String phone1;
	String phone2;
	String email;
	LocalDate subscriptionDate;
}

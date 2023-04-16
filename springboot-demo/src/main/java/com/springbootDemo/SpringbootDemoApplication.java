package com.springbootDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class SpringbootDemoApplication {

	private final CustomerRepository customerRepository;

	public SpringbootDemoApplication(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDemoApplication.class, args);
	}

//	@GetMapping("/")
//	public GreetRecord greet(){
//		return new GreetRecord("Hello new user!");
//	}
//
//	record GreetRecord(String greet){}

	@GetMapping
	public List<Customer> getCustomers(){
		return customerRepository.findAll();
	}

	record NewCustomerRequest(String name, String email, Integer age){}

	@PostMapping
	public void addCustomer(@RequestBody NewCustomerRequest request){
		Customer customer = new Customer();
		customer.setName(request.name());
		customer.setEmail(request.email());
		customer.setAge(request.age());
		customerRepository.save(customer);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id){
		customerRepository.deleteById(id);
	}

	@PutMapping("{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable("customerId") Integer id, @RequestBody NewCustomerRequest request){
		Optional<Customer> customerOptional = customerRepository.findById(id);

		if(!customerOptional.isPresent()){
			String message = "User with such an id does not exist!";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}

		Customer customer = customerOptional.get();

		if(request.name() != null){
			customer.setName(request.name());
		}
		if(request.email() != null){
			customer.setEmail(request.email());
		}
		if(request.age() != null){
			customer.setAge(request.age());
		}

		return ResponseEntity.status(HttpStatus.OK).body("Zaaktualizowano klienta dane na: " + customer.toString());
	}
}

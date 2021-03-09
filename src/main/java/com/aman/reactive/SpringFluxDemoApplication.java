package com.aman.reactive;

import com.aman.reactive.model.Employee;
import com.aman.reactive.repo.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringFluxDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFluxDemoApplication.class, args);
	}


	@Bean
	CommandLineRunner employees(EmployeeRepository employeeRepository) {

		return args -> {
			employeeRepository
					.deleteAll()
					.subscribe(null, null, () -> {

						Stream.of(new Employee(UUID.randomUUID().toString(),
								"Peter", 23000L),new Employee(UUID.randomUUID().toString(),
								"Sam", 13000L),new Employee(UUID.randomUUID().toString(),
								"Ryan", 20000L),new Employee(UUID.randomUUID().toString(),
								"Chris", 53000L)
						)
								.forEach(employee -> {
									employeeRepository
											.save(employee)
											.subscribe(System.out::println);

								});

					})
			;
		};

	}
}

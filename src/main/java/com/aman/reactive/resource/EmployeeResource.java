package com.aman.reactive.resource;

import com.aman.reactive.model.EmpEvent;
import com.aman.reactive.model.Employee;
import com.aman.reactive.repo.EmployeeRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rest/employee")
public class EmployeeResource {


    private EmployeeRepository employeeRepository;

    public EmployeeResource(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/all")
    public Flux<Employee> getAll() {
        return employeeRepository
                .findAll();
    }

    @GetMapping("/{id}")
    public Mono<Employee> getId(@PathVariable("id") final String empId) {
        return employeeRepository.findById(empId);
    }


    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EmpEvent> getEvents(@PathVariable("id")
                                    final String empId) {
        return employeeRepository.findById(empId)
                .flatMapMany(employee -> {

                    Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));

                    Flux<EmpEvent> employeeEventFlux =
                            Flux.fromStream(
                                    Stream.generate(() -> new EmpEvent(employee,
                                            new Date()))
                            );


                    return Flux.zip(interval, employeeEventFlux)
                            .map(Tuple2::getT2);

                });

    }



}
package com.example.SpringBootMasterClass.customer;

import com.example.SpringBootMasterClass.exception.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RequestMapping(path = "api/v2/customer")
@RestController
@AllArgsConstructor
public class CustomerControllerV2 {

    private final CustomerService customerService;

    @GetMapping(path = "{customerId}")
    Customer getCustomer(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @GetMapping(path = "{customerId}/exception")
    Customer getCustomerException(@PathVariable("customerId") Long customerId) {
        throw new ApiRequestException(
                "ApiRequestException for customer " + customerId
        );
    }

    @GetMapping(value = "all")
    List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @PostMapping
    void createNewCustomer(@Valid @RequestBody Customer customer) {
        System.out.println("POST REQUEST...");
        System.out.println(customer);
    }

    @PutMapping
    void updateCustomer(@Valid @RequestBody Customer customer) {
        System.out.println("UPDATE REQUEST...");
        System.out.println(customer);
    }

    @DeleteMapping(path = "{customerId}")
    void deleteCustomer(@PathVariable("customerId") Long id) {
        System.out.println("DELETE REQUEST FOR CUSTOMER WITH ID " + id);
    }
}
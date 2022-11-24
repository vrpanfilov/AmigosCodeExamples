package pvr.SoftwareTesting.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        // 1. Phone number is taken
        // 2. if taken lets check if belongs to same customer
        // - 2.1 if yes return
        // - 2.2 thrown an exception
        // 3. Save customer

        Customer requestCustomer = request.getCustomer();
        String phoneNumber = requestCustomer.getPhoneNumber();
        Optional<Customer> optionalCustomer =
                customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customer.getName().equals(requestCustomer.getName())) {
                return;
            }
            throw new IllegalStateException(
                    String.format("phone number [%s] is taken", phoneNumber));
        }

        if (requestCustomer.getId() == null) {
            requestCustomer.setId(UUID.randomUUID());
        }

        customerRepository.save(requestCustomer);

        Optional<Customer> customer =  customerRepository.findById(requestCustomer.getId());
        customer = customer;
    }
}

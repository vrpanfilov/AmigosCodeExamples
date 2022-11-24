package pvr.SoftwareTesting.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pvr.SoftwareTesting.customer.Customer;
import pvr.SoftwareTesting.customer.CustomerRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES =
            List.of(Currency.USD, Currency.GBP);

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(
            CustomerRepository customerRepository,
            PaymentRepository paymentRepository,
            CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
        // 1. Does customer exist, if not throw
        Iterable<Customer> all = customerRepository.findAll();
        boolean isCustomerFound =
                customerRepository.findById(customerId).isPresent();
        if (!isCustomerFound) {
            throw new IllegalStateException(String.format(
                    "Customer with id [%s] not found", customerId));
        }

        // 2. Do we support the currency, if not throw
        Currency currency = paymentRequest.getPayment().getCurrency();

        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(currency);

        if (!isCurrencySupported) {
            String message =
                    String.format("Currency [%s] not supported", currency);
            throw new IllegalStateException(message);
        }

        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );

        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(
                    "Card not debited for customer " + customerId);
        }

        // 5. Insert payment
        paymentRequest.getPayment().setCustomerId(customerId);

        paymentRepository.save(paymentRequest.getPayment());

        // 6. TODO: send smc

    }
}

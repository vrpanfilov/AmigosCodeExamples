package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;
    private static final List<Currency> ACCEPTED_CURRENCIES =
            List.of(Currency.USD, Currency.GBP);

    @Autowired
    public PaymentService(CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
        Payment payment = paymentRequest.getPayment();
        payment.setCustomerId(customerId);

        // 1. Does customer exist if not throw
        boolean isCustomerFound = customerRepository
                .findById(customerId).isPresent();
        if (!isCustomerFound) {
            String msg = "Customer with id [%s] not found";
            throw new IllegalStateException(String.format(msg, customerId));
        }
        // 2. Do we support the currency if not throw
        Currency currency = payment.getCurrency();
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.stream()
                .anyMatch(c -> c.equals(currency));
        if (!isCurrencySupported) {
            String msg = "Currency [%s] not supported";
            throw  new IllegalStateException(String.format(msg, currency));
        }
        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        );
        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format(
                    "Card not debited for customer %s", customerId));
        }
        // 5. Insert payment
        paymentRepository.save(payment);
        // 6. TODO: send sms
    }
}

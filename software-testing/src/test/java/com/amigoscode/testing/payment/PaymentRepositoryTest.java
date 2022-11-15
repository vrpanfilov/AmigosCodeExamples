package com.amigoscode.testing.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class PaymentRepositoryTest {
    
    private final PaymentRepository underTest;

    @Autowired
    PaymentRepositoryTest(PaymentRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    void itShouldInsertPayment() {
        // Given
        Long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "Donates");

        // When
        underTest.save(payment);

        // Then
        Optional<Payment> paymentOptional = underTest.findById(paymentId);
        assertThat(paymentOptional)
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p).isEqualTo(payment));

        System.out.println();
        System.out.println(paymentOptional.get());
        System.out.println();
    }

}
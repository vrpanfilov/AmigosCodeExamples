package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class PaymentServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;

    private PaymentService underTest;
    private AutoCloseable closable;

    @BeforeEach
    public void setUp() {
        closable = MockitoAnnotations.openMocks(this);
        underTest = new PaymentService(customerRepository,
                paymentRepository,
                cardPaymentCharger);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closable.close();
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        // Given
        UUID customerId = UUID.randomUUID();

        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));
        Payment payment = new Payment(
                null,
                null,
                new BigDecimal(100.00),
                Currency.USD,
                "card123xx",
                "Donate"
        );

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        given(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).willReturn(new CardPaymentCharge(true));

        // When
        underTest.chargeCard(customerId, paymentRequest);

        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor =
                ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();

        assertThat(paymentArgumentCaptorValue)
                .usingRecursiveComparison().ignoringFields("customerId")
                .isEqualTo(paymentRequest.getPayment());
        assertThat(paymentArgumentCaptorValue.getCustomerId())
                .isEqualTo(customerId);
    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        // Given
        UUID customerId = UUID.randomUUID();

        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));
        Payment payment = new Payment(
                null,
                null,
                new BigDecimal(100.00),
                Currency.USD,
                "card123xx",
                "Donate"
        );

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        given(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).willReturn(new CardPaymentCharge(false));

        // When
        // Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card not debited for customer " + customerId);

        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeCardAndThrowWhenCurrencyNotSupported() {
        // Given
        UUID customerId = UUID.randomUUID();

        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));

        Currency currency = Currency.EUR;

        Payment payment = new Payment(
                null,
                null,
                new BigDecimal(100.00),
                currency,
                "card123xx",
                "Donate"
        );

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        // When
        assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Currency [" + currency + "] not supported");

        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeAndThrowWhenCustomerNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Customer with id [" + customerId + "] not found");

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

}
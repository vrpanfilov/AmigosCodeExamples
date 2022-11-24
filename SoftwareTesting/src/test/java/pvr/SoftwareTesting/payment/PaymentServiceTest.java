package pvr.SoftwareTesting.payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pvr.SoftwareTesting.customer.Customer;
import pvr.SoftwareTesting.customer.CustomerRepository;

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

    AutoCloseable openMocks;

    private PaymentService underTest;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        underTest = new PaymentService(
                customerRepository,
                paymentRepository,
                cardPaymentCharger);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        // Given
        UUID custpmerId = UUID.randomUUID();
        given(customerRepository.findById(custpmerId))
                .willReturn(Optional.of(mock(Customer.class)));

        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        custpmerId,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"));

        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));

        // When
        underTest.chargeCard(custpmerId, paymentRequest);

        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor =
                ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();

        assertThat(paymentArgumentCaptorValue)
                .usingRecursiveComparison()
                .ignoringFields("customerId")
                .isEqualTo(paymentRequest.getPayment());
//         В данном случае можно и так:
//        assertThat(paymentArgumentCaptorValue)
//                .isEqualTo(paymentRequest.getPayment());
    }

    @Test
    void itShouldNotChargeAndThrowWhenCustomerNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();

        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // When
        // then
        assertThatThrownBy(() ->
                underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "Customer with id [%s] not found", customerId));

        then(cardPaymentCharger).shouldHaveNoInteractions();

        then(paymentRepository).shouldHaveNoInteractions();
    }


    @Test
    void itShouldThrownWhenCardIsNotCharged() {
        // Given
        UUID customerId = UUID.randomUUID();
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));

        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        customerId,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"));

        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false));

        // When
        // Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card not debited for customer "
                        + customerId);

        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeCardAndThrowWhenCurrencyNotSupported() {
        // Given
        UUID custpmerId = UUID.randomUUID();
        given(customerRepository.findById(custpmerId))
                .willReturn(Optional.of(mock(Customer.class)));

        Currency currency = Currency.EUR;
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        custpmerId,
                        new BigDecimal("100.00"),
                        currency,
                        "card123xx",
                        "Donation"));

        // When
        String message =
                String.format("Currency [%s] not supported", currency);
        assertThatThrownBy(() -> underTest.chargeCard(custpmerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(message);

        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();

        then(paymentRepository).shouldHaveNoInteractions();
    }

}
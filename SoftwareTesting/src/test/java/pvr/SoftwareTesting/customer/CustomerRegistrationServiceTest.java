package pvr.SoftwareTesting.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    // Можно так:
//
//    private CustomerRepository customerRepository = mock(CustomerRepository.class);

    // Предпочтительно так:
    @Mock
    private CustomerRepository customerRepository;

    private CustomerRegistrationService underTest;

    AutoCloseable openMocks;

    @Captor
     private ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
//                .usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrownWhenPhoneNumberIsTaken() {
        // Given
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        Customer customerTwo = new Customer(UUID.randomUUID(),
                "John", phoneNumber);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        // When
        // Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "phone number [%s] is taken", phoneNumber));
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given
        String phoneNumber = "000099";
        Customer customer = new Customer(null, "Maryam", phoneNumber);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptorValue)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(customer);
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

}
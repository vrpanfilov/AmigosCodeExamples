package com.amigoscode.testing.customer;

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

    @Mock
    private CustomerRepository customerRepository;
    private CustomerRegistrationService underTest;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        //  ... a request
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(
                phoneNumber)).willReturn(Optional.empty());
        // When
        underTest.registerNewCustomer(request);
        // Then
        then(customerRepository).should()
                .save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue =
                customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        //  ... a request
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);
        // ... A customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(
                phoneNumber)).willReturn(Optional.of(customer));
        // When
        underTest.registerNewCustomer(request);
        // Then
        then(customerRepository).should(never()).save(any());
        then(customerRepository).should()
                .selectCustomerByPhoneNumber(phoneNumber);
//        then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(),
                "Maryam", phoneNumber);
        Customer customer2 = new Customer(UUID.randomUUID(),
                "John", phoneNumber);
        //  ... a request
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);
        // ... An existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(
                phoneNumber)).willReturn(Optional.of(customer2));
        // When
        // Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone number [%s] is taken", phoneNumber));
        then(customerRepository).should(never()).save(any());
    }
    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given a phone number and a customer
        String phoneNumber = "000099";
        Customer customer = new Customer(null, "Maryam", phoneNumber);
        //  ... a request
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(
                phoneNumber)).willReturn(Optional.empty());
        // When
        underTest.registerNewCustomer(request);
        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue)
                .usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(customer);
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }
}
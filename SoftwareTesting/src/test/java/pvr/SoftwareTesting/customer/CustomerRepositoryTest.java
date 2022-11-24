package pvr.SoftwareTesting.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "0000");

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo("Abel");
//                    assertThat(c.getPhoneNumber()).isEqualTo("0000");
                    assertThat(c).isEqualTo(customer);
                });
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "0000");

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0000";
        Customer customer = new Customer(id, "Maryam", phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c)
                        .usingRecursiveComparison()
                        .isEqualTo(customer)
                );
    }

    @Test
    void itShouldNotSelectcustomerByPhoneNumberWhenNumberDoesNotExist() {
        // Given
        String phoneNumber = "0000";
        Optional<Customer> optionalCustomer =
                underTest.selectCustomerByPhoneNumber(phoneNumber);
        // When
        // Then
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberIsNull() {
        // Given
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", null);

        // When
        // Then

        // This test does not work with h2, the command does not throw
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
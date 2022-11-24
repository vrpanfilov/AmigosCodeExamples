package pvr.SoftwareTesting.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pvr.SoftwareTesting.customer.Customer;
import pvr.SoftwareTesting.customer.CustomerRegistrationRequest;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+445000000000");

        ResultActions customerRegResultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/customer-registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(
                                new CustomerRegistrationRequest(customer)))));

        Long paymentId = 1L;
        Payment payment = new Payment(paymentId,
                customerId,
                new BigDecimal("10.00"),
                Currency.GBP,
                "0x0x0x0x",
                "Zakat");

        PaymentRequest paymentRequest = new PaymentRequest(payment);

        ResultActions paymentResultActions = mockMvc
                .perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

        // Then
        customerRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());

        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p)
                        .usingRecursiveComparison()
                        .isEqualTo(payment));

        // TODO: Ensure that sms delivered
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed convert object to json");
            return null;
        }
    }

}

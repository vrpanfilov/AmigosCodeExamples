package pvr.SoftwareTesting.payment.stripe;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pvr.SoftwareTesting.payment.CardPaymentCharge;
import pvr.SoftwareTesting.payment.CardPaymentCharger;
import pvr.SoftwareTesting.payment.Currency;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true);
    }
}

package com.amigoscode.testing.payment;

public class CardPaymentCharge {
    private final boolean isDebitCard;

    public CardPaymentCharge(boolean isDebitCard) {
        this.isDebitCard = isDebitCard;
    }

    public boolean isCardDebited() {
        return isDebitCard;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isDebitCard=" + isDebitCard +
                '}';
    }
}

package combinatorpattern;

import java.time.LocalDate;

import static combinatorpattern.CustomerRegistrationValidator.*;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer(
                "Alice",
                "alice@gmail.com",
                "+098768798765",
                LocalDate.of(2000, 1, 1)
        );

        System.out.println(new CustomerValidatorService().isValid(customer));

        // If valid we can store customer in db

        // Using combinator pattern
        ValidationResult result = isEmailValid()
                .and(isPhoneNumberValid())
                .and(isAnAdult())
                .apply(customer);

        if (result != ValidationResult.SUCCESS) {
            throw new IllegalStateException(result.name());
        }

        System.out.println(result);
    }
}

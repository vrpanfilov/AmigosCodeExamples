package optionals;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Object value = Optional.ofNullable(null)
                .orElseGet(() -> "default value");
        System.out.println(value);

        value = Optional.ofNullable("Hello")
                .orElseGet(() -> "default value");
        System.out.println(value);

        String text = "Hi!";
//        text = null;
        value = Optional.ofNullable(text)
                .orElseThrow(() -> new RuntimeException("exception"));
        System.out.println(value);

        text = "john@gmail.com";
//        text = null;
        Optional.ofNullable(text)
                .ifPresent(email -> {
                    // logic
                    System.out.println("Sending email to " + email);
                });

        text = "sam@gmail.com";
//        text = null;
        Optional.ofNullable(text)
                .ifPresentOrElse(
                        email -> System.out.println("Sending email to " + email),
                        () -> System.out.println("Cannot send email"));
    }
}

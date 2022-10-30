package functionalinterface;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class _Consumer {
    public static void main(String[] args) {
        // Normal Java function
        Customer maria = new Customer("Maria", "99999");
        greetCustomer(maria);
        greetCustomerV2(maria, true);
        greetCustomerV2(maria, false);

        // Consumer functional interface
        greetCustomerConsumer.accept(maria);

        greetCustomerConsumerV2.accept(maria, true);
        greetCustomerConsumerV2.accept(maria, false);
    }

    static BiConsumer<Customer, Boolean> greetCustomerConsumerV2 =
            (customer, showPhoneNumber) -> {
                System.out.println("Hello " + customer.name
                        + ", thanks for regestering phone number " +
                        (showPhoneNumber ? customer.phoneNumber : "*****"));
            };

    static Consumer<Customer> greetCustomerConsumer = customer -> {
        System.out.println("Hello " + customer.name
                + ", thanks for regestering phone number "
                + customer.phoneNumber);
    };

    static void greetCustomer(Customer customer) {
        System.out.println("Hello " + customer.name
                + ", thanks for regestering phone number "
                + customer.phoneNumber);
    }

    static void greetCustomerV2(Customer customer, Boolean showPhoneNumber) {
        System.out.println("Hello " + customer.name
                + ", thanks for regestering phone number " +
                (showPhoneNumber ? customer.phoneNumber : "*****"));
    }

    static class Customer {
        private final String name;
        private final String phoneNumber;

        public Customer(String name, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }
}

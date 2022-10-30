package functionalinterface.streams;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static functionalinterface.streams._Stream.Gender.*;

public class _Stream {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("John", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alice", FEMALE),
                new Person("Bob", PREFER_NOT_TO_SAY)
        );

        people.stream()
                .map(person -> person.gender)
                .collect(Collectors.toSet())
                .forEach(System.out::println);

        people.stream()
                .map(person -> person.name)
                .sorted()
                .mapToInt(String::length)
                .forEach(System.out::println);

        boolean allAreFemale = people.stream()
                .allMatch(person -> FEMALE.equals(person.gender));
        System.out.println(allAreFemale);

        boolean anyAreFemale = people.stream()
                .anyMatch(person -> FEMALE.equals(person.gender));
        System.out.println(anyAreFemale);
    }

    static class Person {
        private final String name;
        public final Gender gender;

        public Person(String name, Gender gender) {
            this.name = name;
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", gender=" + gender +
                    '}';
        }
    }

    enum Gender {
        MALE, FEMALE, PREFER_NOT_TO_SAY
    }
}

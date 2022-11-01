package com.amigoscode.examples;

import com.amigoscode.beans.Car;
import com.amigoscode.beans.Person;
import com.amigoscode.mockdata.MockData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sorting {

    @Test
    public void sortingSteamOfElements() throws IOException {
        List<Person> people = MockData.getPeople();
        Stream<String> sorted = people.stream()
                .map(Person::getFirstName)
                .sorted();
        sorted.forEach(System.out::println);
    }

    @Test
    public void sortingSteamOfElementsReverse() throws IOException {
        List<Person> people = MockData.getPeople();
        Stream<String> sorted = people.stream()
                .map(Person::getFirstName)
                .sorted(Comparator.reverseOrder());
        sorted.forEach(System.out::println);
    }

    @Test
    public void sortingSteamOfObjets() throws IOException {
        List<Person> people = MockData.getPeople();

        Comparator<Person> comparing = Comparator
                .comparing(Person::getEmail).reversed()
                .thenComparing(Person::getFirstName);

        people.stream()
                .limit(16)
                .sorted(comparing)
                .forEach(System.out::println);
    }

    @Test
    public void topTenMostExpensiveBlueCars() throws IOException {
        List<Car> cars = MockData.getCars();
        List<Car> blue = cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase("Blue"))
                .sorted(Comparator.comparing(Car::getPrice).reversed())
                .limit(10)
                .toList();
        blue.forEach(System.out::println);
    }

}

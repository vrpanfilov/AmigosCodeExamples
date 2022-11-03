package com.amigoscode.examples;


import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DistinctAndSets {

    @Test
    public void distinct() {
        List<Integer> numbers = List.of(
                1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6,
                7, 7, 8, 8, 9, 9, 9, 9, 9);
        List<Integer> distinct = numbers.stream()
                .distinct()
                .toList();
        assertThat(distinct).hasSize(9);
        distinct.forEach(System.out::println);
    }

    @Test
    public void distinctWithSet() {
        List<Integer> numbers = List.of(
                1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6,
                7, 7, 8, 8, 9, 9, 9, 9, 9);
        Set<Integer> distinct = new HashSet<>(numbers);
        assertThat(distinct).hasSize(9);
        distinct.forEach(System.out::println);
    }
}

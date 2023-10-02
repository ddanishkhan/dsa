package com.dsa;

import java.util.HashSet;
import java.util.Objects;

// Probabilistic space saving algorithm that returns false positive and only certain true negatives
// Best for large data sets, where we need to be sure something is not present.
// Ex: IP is not blocked, user is not blocked
public class BloomFilter {

    public static void main(String[] args) {
        BloomFilter.insert("Hello world");
        BloomFilter.insert("Hello world1");
        BloomFilter.insert("Hello world2");

        BloomFilter.lookup("Hello world3");
        BloomFilter.lookup("Hello world2");
    }

    static HashSet<Integer> strings = new HashSet<>();  //simple implementation, will not work on very large data sets.

    static Boolean lookup(String s) {
        Boolean value = strings.contains(Objects.hash(s));
        System.out.printf("%s present - %s%n", s, value);
        return value;
    }

    static void insert(String s) {
        strings.add(Objects.hash(s));
    }
}

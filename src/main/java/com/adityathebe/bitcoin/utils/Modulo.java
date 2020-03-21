package com.adityathebe.bitcoin.utils;

public class Modulo{

    public static void main(String args[]) {

        // Example 1 - Modulo operator returns remainder
        int iValue = 101;
        double dValue = 39.02;

        System.out.println(iValue + " mod 9 = " + iValue % 9);
        System.out.println(dValue + " mod 9 = " + dValue % 9);


        // Example 2 - module operator on 10 can give you last digit of integer number
        int number = 215;
        int total = 349;
        System.out.printf("Last digit of %d is %d%n", number, number % 10);
        System.out.printf("Last digit of %d is %d%n", total, total % 10);


        // Example 3 - You can use modulo operator on 2 to check if number is even or odd
        int even = 22;
        int odd = 21;
        System.out.printf("%d is %s number%n", even, oddness(even));
        System.out.printf("%d is %s number%n", odd, oddness(odd));

    }

    public static String oddness(int i) {
        return i % 2 == 0 ? "even" : "odd";
    }

}

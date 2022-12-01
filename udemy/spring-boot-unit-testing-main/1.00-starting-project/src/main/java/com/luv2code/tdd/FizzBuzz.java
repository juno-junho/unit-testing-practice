package com.luv2code.tdd;

public class FizzBuzz {

    // If number is divisible by 3, print Fizz
    // If number is divisible by 5, print Buzz
    // If number is divisible by 3 and 5, print FizzBuzz
    // If number is NOT divisible by 3 and 5,then print the number


    // refactoring
    static String compute(int num) {
        StringBuilder result = new StringBuilder();
        if (num % 3 == 0) {
            result.append("Fizz");
        }
        if (num % 5 == 0) {
            result.append("Buzz");
        }
        if (result.length() == 0) {
            result.append(num);
        }
        return result.toString();
    }

    /*static String compute(int num) {
        if ((num % 3 == 0) && (num % 5 == 0)) return "FizzBuzz";
        if (num % 3 == 0) return "Fizz";
        else if (num % 5 == 0) return "Buzz";
        return String.valueOf(num);
    }*/
}

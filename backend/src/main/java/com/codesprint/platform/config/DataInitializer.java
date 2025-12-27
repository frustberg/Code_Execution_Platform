package com.codesprint.platform.config;

import com.codesprint.platform.model.Problem;
import com.codesprint.platform.repo.ProblemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initProblems(ProblemRepository problemRepository) {
        return args -> {
            if (problemRepository.count() == 0) {
                
                // Problem 1: Two Sum
                Problem twoSum = Problem.builder()
                        .title("Two Sum")
                        .level("Easy")
                        .tags(List.of("array", "hash-table"))
                        .statement("Given an array of integers nums and an integer target, return indices of the two numbers that add up to target. Input: First line contains space-separated integers (the array), second line contains the target integer. Output: Print two space-separated indices.")
                        .codeStubs(Map.of(
                                "python", "nums = list(map(int, input().split()))\ntarget = int(input())\n# Your code here\nprint(\"0 1\")  # Example output",
                                "javascript", "const fs = require('fs');\nconst input = fs.readFileSync(0, 'utf8').trim().split('\\n');\nconst nums = input[0].split(' ').map(Number);\nconst target = Number(input[1]);\n// Your code here\nconsole.log('0 1');  // Example output",
                                "java", "import java.util.*;\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    String[] parts = sc.nextLine().split(\" \");\n    int[] nums = new int[parts.length];\n    for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n    int target = sc.nextInt();\n    // Your code here\n    System.out.println(\"0 1\");  // Example output\n  }\n}"
                        ))
                        .testCases(List.of(
                                new Problem.TestCase("2 7 11 15\n9\n", "0 1", false),
                                new Problem.TestCase("3 2 4\n6\n", "1 2", false),
                                new Problem.TestCase("3 3\n6\n", "0 1", true)
                        ))
                        .timeoutSeconds(5L)
                        .build();

                // Problem 2: Reverse String
                Problem reverseString = Problem.builder()
                        .title("Reverse String")
                        .level("Easy")
                        .tags(List.of("string", "two-pointers"))
                        .statement("Write a function that reverses a string. Input: A single line containing the string. Output: Print the reversed string.")
                        .codeStubs(Map.of(
                                "python", "s = input()\n# Your code here\nprint(s[::-1])  # Example",
                                "javascript", "const fs = require('fs');\nconst s = fs.readFileSync(0, 'utf8').trim();\n// Your code here\nconsole.log(s.split('').reverse().join(''));  // Example",
                                "java", "import java.util.*;\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    String s = sc.nextLine();\n    // Your code here\n    System.out.println(new StringBuilder(s).reverse());  // Example\n  }\n}"
                        ))
                        .testCases(List.of(
                                new Problem.TestCase("hello\n", "olleh", false),
                                new Problem.TestCase("world\n", "dlrow", false)
                        ))
                        .timeoutSeconds(5L)
                        .build();

                // Problem 3: Valid Palindrome
                Problem validPalindrome = Problem.builder()
                        .title("Valid Palindrome")
                        .level("Easy")
                        .tags(List.of("string", "two-pointers"))
                        .statement("Given a string s, return 'true' if it is a palindrome (ignoring spaces, punctuation, and case), or 'false' otherwise. Input: A single line containing the string. Output: Print 'true' or 'false'.")
                        .codeStubs(Map.of(
                                "python", "s = input()\n# Your code here\nprint('true')  # Example",
                                "javascript", "const fs = require('fs');\nconst s = fs.readFileSync(0, 'utf8').trim();\n// Your code here\nconsole.log('true');  // Example",
                                "java", "import java.util.*;\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    String s = sc.nextLine();\n    // Your code here\n    System.out.println(\"true\");  // Example\n  }\n}"
                        ))
                        .testCases(List.of(
                                new Problem.TestCase("A man, a plan, a canal: Panama\n", "true", false),
                                new Problem.TestCase("race a car\n", "false", false)
                        ))
                        .timeoutSeconds(5L)
                        .build();

                // Problem 4: Maximum Subarray
                Problem maxSubarray = Problem.builder()
                        .title("Maximum Subarray")
                        .level("Medium")
                        .tags(List.of("array", "dynamic-programming"))
                        .statement("Given an integer array nums, find the contiguous subarray with the largest sum, and return its sum. Input: Space-separated integers. Output: Print the maximum sum.")
                        .codeStubs(Map.of(
                                "python", "nums = list(map(int, input().split()))\n# Your code here (Kadane's algorithm)\nprint(max(nums))  # Example",
                                "javascript", "const fs = require('fs');\nconst nums = fs.readFileSync(0, 'utf8').trim().split(' ').map(Number);\n// Your code here\nconsole.log(Math.max(...nums));  // Example",
                                "java", "import java.util.*;\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    String[] parts = sc.nextLine().split(\" \");\n    int[] nums = new int[parts.length];\n    for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n    // Your code here\n    System.out.println(Arrays.stream(nums).max().getAsInt());  // Example\n  }\n}"
                        ))
                        .testCases(List.of(
                                new Problem.TestCase("-2 1 -3 4 -1 2 1 -5 4\n", "6", false),
                                new Problem.TestCase("1\n", "1", false)
                        ))
                        .timeoutSeconds(7L)
                        .build();

                // Problem 5: Binary Search
                Problem binarySearch = Problem.builder()
                        .title("Binary Search")
                        .level("Easy")
                        .tags(List.of("array", "binary-search"))
                        .statement("Given a sorted array and a target value, return the index if found, or -1 if not found. Use binary search. Input: First line: sorted array (space-separated), Second line: target. Output: Print the index or -1.")
                        .codeStubs(Map.of(
                                "python", "nums = list(map(int, input().split()))\ntarget = int(input())\n# Binary search here\nprint(-1)  # Example",
                                "javascript", "const fs = require('fs');\nconst input = fs.readFileSync(0, 'utf8').trim().split('\\n');\nconst nums = input[0].split(' ').map(Number);\nconst target = Number(input[1]);\n// Binary search here\nconsole.log(-1);  // Example",
                                "java", "import java.util.*;\npublic class Main {\n  public static void main(String[] args) {\n    Scanner sc = new Scanner(System.in);\n    String[] parts = sc.nextLine().split(\" \");\n    int[] nums = new int[parts.length];\n    for (int i = 0; i < parts.length; i++) nums[i] = Integer.parseInt(parts[i]);\n    int target = sc.nextInt();\n    // Binary search here\n    System.out.println(-1);  // Example\n  }\n}"
                        ))
                        .testCases(List.of(
                                new Problem.TestCase("-1 0 3 5 9 12\n9\n", "4", false),
                                new Problem.TestCase("-1 0 3 5 9 12\n2\n", "-1", false)
                        ))
                        .timeoutSeconds(5L)
                        .build();

                // Save all problems
                problemRepository.saveAll(List.of(
                        twoSum, reverseString, validPalindrome, maxSubarray, binarySearch
                ));

                System.out.println("✅ Initialized 5 DSA problems with timeouts (5-7 seconds)");
            } else {
                System.out.println("✅ Problems already exist in database");
            }
        };
    }
}

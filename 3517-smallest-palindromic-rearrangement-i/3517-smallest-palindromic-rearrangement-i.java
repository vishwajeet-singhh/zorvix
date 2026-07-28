class Solution {
    public String smallestPalindrome(String s) {
        int[] letterCount = new int[26];
        for (char c : s.toCharArray()) {
            letterCount[c - 'a']++;
        }

        StringBuilder leftHalf = new StringBuilder();
        boolean hasMiddleLetter = false;
        char middleLetter = ' ';

        for (int i = 0; i < 26; i++) {
            char letter = (char) ('a' + i);
            int pairsOfThisLetter = letterCount[i] / 2;

            for (int j = 0; j < pairsOfThisLetter; j++) {
                leftHalf.append(letter);
            }

            if (letterCount[i] % 2 == 1) {
                hasMiddleLetter = true;
                middleLetter = letter;
            }
        }

        String left = leftHalf.toString();
        String right = new StringBuilder(left).reverse().toString();

        StringBuilder palindrome = new StringBuilder(left);
        if (hasMiddleLetter) {
            palindrome.append(middleLetter);
        }
        palindrome.append(right);

        return palindrome.toString();
    }
}
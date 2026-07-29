class Solution {
    public String rearrangeString(String s, char x, char y) {

        String allOfY = "";
        String allOfX = "";
        String allOther = "";

        for (char ch : s.toCharArray()) {

            if (x == ch)
                allOfX = allOfX + String.valueOf(ch);
            else if (y == ch)
                allOfY = allOfY + String.valueOf(ch);
            else
                allOther = allOther + String.valueOf(ch);
        }

        return allOfY + allOther + allOfX;

    }
}
class Solution {
    public String mergeAlternately(String word1, String word2) {

        int word1_index = 0,
                word2_index = 0,
                candidate = 0,
                word1_len = word1.length(),
                word2_len = word2.length();

        char[] merged_array = new char[word1_len + word2_len];

        while (word1_index < word1_len && word2_index < word2_len) {

            merged_array[candidate++] = word1.charAt(word1_index++);
            merged_array[candidate++] = word2.charAt(word2_index++);
        }
        while (word1_index < word1_len) {
            merged_array[candidate++] = word1.charAt(word1_index++);

        }
        while (word2_index < word2_len) {
            merged_array[candidate++] = word2.charAt(word2_index++);
        }
        return new String(merged_array);

        /** 
        char[] word1_array = word1.toCharArray();
        char[] word2_array = word2.toCharArray();
        
        int word1_len = word1_array.length;
        int word2_len = word2_array.length;
        
        char[] merged_array = new char[word1_len + word2_len];
        
        int word1_index = 0;
        int word2_index = 0;
        int candidate_index = 0;
        
        while (word1_index < word1_len || word2_index < word2_len) {
        
            if (word1_index == word2_index && word1_index < word1_len) {
                merged_array[candidate_index] = word1_array[word1_index];
                word1_index++;
                candidate_index++;
            } else if (word1_index > word2_index && word2_index < word2_len) {
                merged_array[candidate_index] = word2_array[word2_index];
                word2_index++;
                candidate_index++;
            } else if (word1_index == word1_len && word2_index < word2_len) {
                merged_array[candidate_index] = word2_array[word2_index];
                word2_index++;
                candidate_index++;
            } else if (word2_index == word2_len && word1_index < word1_len) {
                merged_array[candidate_index] = word1_array[word1_index];
                word1_index++;
                candidate_index++;
        
            }
        
        }
        
        return new String(merged_array);
        
        */
    }
}
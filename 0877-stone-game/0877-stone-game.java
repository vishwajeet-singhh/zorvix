
class Solution {
    public boolean stoneGame(int[] piles) {

        int bob = 0,
                alice = 0,
                n = piles.length,
                right = n - 1,
                alice_score = 0,
                bob_score = 0;

        boolean alice_turn = true;

        while (alice < right && bob < right) {

            if (alice_turn) {
                if (piles[alice] >= piles[right]) {
                    alice_score = alice_score + piles[alice];
                    alice++;
                } else {
                    alice_score = alice_score + piles[right];
                    right--;
                }

                alice_turn = false;
            } else {

                if (piles[bob] >= piles[right]) {
                    bob_score = bob_score + piles[bob];
                    bob++;

                } else {
                    bob_score = bob_score + piles[bob];
                    right--;
                }
            }

            alice_turn = true;
        }

        return alice_score > bob_score;

    }
}

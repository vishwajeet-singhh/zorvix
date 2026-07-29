
class Solution {
    public int trap(int[] height) {

        int left = 0,
                totalWater = 0,
                right = height.length-1,
                leftMaxHeight = height[left],
                rightMaxHeight = height[right];

        while (left < right) {

            if (leftMaxHeight < rightMaxHeight) {
                left++;
                leftMaxHeight = Math.max(leftMaxHeight, height[left]);
                totalWater += leftMaxHeight - height[left];
            } else {
                right--;
                rightMaxHeight = Math.max(rightMaxHeight, height[right]);
                totalWater += rightMaxHeight - height[right];
            }
        }

        return totalWater;
    }
}

/*

Brute Force- O(n²) Time, O(1) Space

class Solution {
public int trap(int[] height) {

    int totalWater = 0;

    for (int i = 1; i < height.length; i++) {

        int prevHeight = 0;
        int currentHeight = height[i];
        int nextHeight = 0;

        for (int j = 0; j <= i; j++) {
            prevHeight = Math.max(height[j], prevHeight);
        }
        for (int j = i; j < height.length; j++) {
            nextHeight = Math.max(height[j], nextHeight);
        }

        totalWater += Math.min(prevHeight, nextHeight) - currentHeight;

    }

    return totalWater;

}
}

*/
class Solution:
    def maxArea(self, height: List[int]) -> int:
        left = 0
        right = len(height)-1
        max_area = -1

        while left < right:
            curr_area = min(height[left], height[right]) * (right - left)
            max_area = max(curr_area, max_area)
            
            
            if height[left] > height[right]:
                right = right-1
            else:
                left = left+1

        return max_area


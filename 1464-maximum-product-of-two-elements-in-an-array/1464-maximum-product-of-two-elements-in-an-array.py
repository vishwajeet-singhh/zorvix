class Solution:
    def maxProduct(self, nums: List[int]) -> int:

        first_large = -1
        second_large = -1

        for num in nums:
            if num > first_large:
                second_large = first_large
                first_large = num
            elif num > second_large:
                second_large = num

        return (first_large - 1) * (second_large - 1)

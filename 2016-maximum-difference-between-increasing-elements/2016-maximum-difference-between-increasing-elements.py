class Solution:
    def maximumDifference(self, nums: List[int]) -> int:

        result = -1

        mini = nums[0]

        for i in range(1, len(nums)):
            if nums[i] > mini:
                result = max(result, nums[i] - mini)
            mini = min(nums[i], mini)

        return result

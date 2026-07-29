class Solution:
    def removeDuplicates(self, nums: List[int]) -> int:
        result = 1

        for i in range(1, len(nums)):
            if nums[i] != nums[i - 1]:
                nums[result] = nums[i]
                result = result + 1
        return result
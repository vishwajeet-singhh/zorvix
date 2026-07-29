class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        can = None
        count = 0

        for i in nums:
            if 0 == count:
                can = i
            if i == can:
                count = count + 1
            else:
                count = count - 1

        if nums.count(can) > len(nums) // 2:
            return can

        return -1

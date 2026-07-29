from typing import List

class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:

        def merge_sort(arr, left, right):
            if left >= right:
                return

            mid = left + (right - left) // 2
            merge_sort(arr, left, mid)
            merge_sort(arr, mid + 1, right)
            merge(arr, left, mid, right)

        def merge(arr, left, mid, right):
            left_arr = arr[left:mid + 1]
            right_arr = arr[mid + 1:right + 1]

            i = 0
            j = 0
            k = left

            while i < len(left_arr) and j < len(right_arr):
                if left_arr[i] <= right_arr[j]:
                    arr[k] = left_arr[i]
                    i += 1
                else:
                    arr[k] = right_arr[j]
                    j += 1
                k += 1

            while i < len(left_arr):
                arr[k] = left_arr[i]
                i += 1
                k += 1

            while j < len(right_arr):
                arr[k] = right_arr[j]
                j += 1
                k += 1

        
        if nums:
            merge_sort(nums, 0, len(nums) - 1)
            
        return nums
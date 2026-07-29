class Solution:
    def maxProduct(self, n: int) -> int:

        first = 0
        last = 0

        while 0 < n:
            digit = n % 10

            if digit > first:
                last = first
                first = digit
            elif digit > last:
                last = digit
            
            n = n//10

        return first * last

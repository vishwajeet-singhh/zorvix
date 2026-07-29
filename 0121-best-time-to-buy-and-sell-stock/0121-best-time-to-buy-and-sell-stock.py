class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        left = 0
        right = 1
        length = len(prices)
        profit = 0

        while right < length:
            if prices[right] > prices[left]:
                profit = max(profit, prices[right] - prices[left])
            else:
                left = right

            right = right + 1

        return profit

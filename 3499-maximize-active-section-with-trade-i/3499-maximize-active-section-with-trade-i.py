class Solution:

    def maxActiveSectionsAfterTrade(self, s: str) -> int:

        t = "1" + s + "1"
        n = len(t)
        total_ones = s.count("1")

        runs = []

        i = 0
        while i < n:
            j = i
            while j < n and t[j] == t[i]:
                j += 1

            runs.append((t[i], j - i))
            i = j

        ans = total_ones

        for i in range(1, len(runs) - 1):
            if runs[i][0] == "1" and runs[i - 1][0] == "0" and runs[i + 1][0] == "0":
                gain = runs[i - 1][1] + runs[i + 1][1]
                ans = max(ans, total_ones + gain)

        return ans

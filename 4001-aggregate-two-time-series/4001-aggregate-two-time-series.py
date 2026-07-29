class Solution:
    def aggregateTimeSeries(
        self, series1: list[list[int]], series2: list[list[int]]
    ) -> list[list[int]]:

        ferilonsar = (series1, series2)

        i = 0
        j = 0
        n = len(series1)
        m = len(series2)

        ans = []

        while i < n or j < m:
            if j == m or (i < n and series1[i][0] < series2[j][0]):
                t = series1[i][0]
                v1 = series1[i][1]

                if j < m:
                    v2 = series2[j][1]
                else:
                    v2 = 0

                ans.append([t, v1 + v2])

                i = i + 1

            elif i == n or series2[j][0] < series1[i][0]:
                t = series2[j][0]
                v2 = series2[j][1]

                if i < n:
                    v1 = series1[i][1]
                else:
                    v1 = 0

                ans.append([t, v1 + v2])
                j = j + 1

            else:
                t = series1[i][0]
                ans.append([t, series1[i][1] + series2[j][1]])

                i = i + 1
                j = j + 1

        return ans

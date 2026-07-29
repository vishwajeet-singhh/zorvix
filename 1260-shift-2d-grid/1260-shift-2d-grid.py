class Solution:
    def shiftGrid(self, grid: List[List[int]], k: int) -> List[List[int]]:

        m = len(grid)
        n = len(grid[0])
        total = m * n
        k = k % total

        arr = [[0] * n for _ in range(m)]

        for i in range(m):
            for j in range(n):
                index = i * n + j
                newIndex = (index + k) % total

                newRow = newIndex // n
                newCol = newIndex % n

                arr[newRow][newCol] = grid[i][j]
                
        return arr

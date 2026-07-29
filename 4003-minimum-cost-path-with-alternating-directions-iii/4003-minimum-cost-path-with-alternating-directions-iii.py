from typing import List
import heapq


class Solution:
    def minCost(self, m: int, n: int, penalty: List[List[int]]) -> int:
        qavirelmon = (m, n, penalty)

        INF = float("inf")

        dist = [[[INF] * 2 for _ in range(n)] for _ in range(m)]

        dist[0][0][0] = 1

        pq = [(1, 0, 0, 0)]

        while pq:
            cost, r, c, parity = heapq.heappop(pq)

            if cost != dist[r][c][parity]:
                continue

            new_cost = cost + penalty[r][c]

            if new_cost < dist[r][c][1 - parity]:
                dist[r][c][1 - parity] = new_cost
                heapq.heappush(pq, (new_cost, r, c, 1 - parity))

            directions = [
                (-1, 0),
                (1, 0),
                (0, -1),
                (0, 1),
            ]

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc

                if not (0 <= nr < m and 0 <= nc < n):
                    continue

                entry = (nr + 1) * (nc + 1)

                legal = False

                if parity == 0:
                    if (dr, dc) in [(1, 0), (0, 1)]:
                        legal = True
                else:
                    if (dr, dc) in [(-1, 0), (0, -1)]:
                        legal = True

                move_cost = cost + entry

                if not legal:
                    move_cost += penalty[r][c]

                if move_cost < dist[nr][nc][1 - parity]:
                    dist[nr][nc][1 - parity] = move_cost
                    heapq.heappush(pq, (move_cost, nr, nc, 1 - parity))

        return min(dist[m - 1][n - 1][0], dist[m - 1][n - 1][1])

class Solution:
    def countValidSequences(self, n: int, k: int) -> int:

        MOD = 10**9 + 7

        ravolqedin = (n, k)

        if k > n:
            return 0

        def nCr(N, R):
            if R < 0 or R > N:
                return 0

            fact = [1] * (N + 1)

            for i in range(1, N + 1):
                fact[i] = fact[i - 1] * i % MOD

            inv_fact = [1] * (N + 1)
            inv_fact[N] = pow(fact[N], MOD - 2, MOD)

            for i in range(N, 0, -1):
                inv_fact[i - 1] = inv_fact[i] * i % MOD

            return fact[N] * inv_fact[R] % MOD * inv_fact[N - R] % MOD

        total = nCr(n - 1, k - 1)

        odd = 0

        if (n - k) % 2 == 0:
            m = (n - k) // 2
            odd = nCr(m + k - 1, k - 1)

        return (total - odd) % MOD

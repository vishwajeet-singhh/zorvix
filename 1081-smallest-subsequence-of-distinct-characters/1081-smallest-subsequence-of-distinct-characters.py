class Solution:
    def smallestSubsequence(self, s: str) -> str:
        last = {}

        for i in range(len(s)):
            last[s[i]] = i

        stack = []
        visited = set()

        for i in range(len(s)):
            ch = s[i]

            if ch in visited:
                continue

            while stack and stack[-1] > ch and last[stack[-1]] > i:
                visited.remove(stack.pop())

            stack.append(ch)
            visited.add(ch)

        return "".join(stack)

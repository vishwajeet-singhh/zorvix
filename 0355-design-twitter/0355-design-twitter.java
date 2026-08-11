

import java.util.*;
class Twitter {
    Map<Integer, Set<Integer>> following;
    Deque<int[][]> stack;

    public Twitter() {
        following = new HashMap<>();
        stack = new ArrayDeque<>();

    }

    // 1. need a stack for the latest tweet order then if use follow then filter and present
    public void postTweet(int userId, int tweetId) {
        stack.addLast(new int[][] { { userId, tweetId } });
    }

    // 2. can be used above stack for this ( iw ill go for the Dqueue)
    public List<Integer> getNewsFeed(int userId) {

        int count = 0;
        List<Integer> list = new ArrayList<>();

        Set<Integer> following_set = following.getOrDefault(userId, Collections.emptySet());
        

        Iterator<int[][]> iterator = stack.descendingIterator();
        while (iterator.hasNext() && 10 > count) {
            int[][] tweet = iterator.next();

            int postUserId = tweet[0][0];
            int postId = tweet[0][1];

            if (following_set.contains(postUserId) || postUserId == userId) {
                count++;
                list.add(postId);
            }

        }

        return list;
    }
    // a map for the maintaingi the following list

    public void follow(int followerId, int followeeId) {

        Set<Integer> list = following.get(followerId);

        if (Objects.isNull(list)) {
            list = new HashSet<>();
            following.put(followerId, list);
        }

        list.add(followeeId);
    }

    // remove from the map if unfollowed
    public void unfollow(int followerId, int followeeId) {
        Set<Integer> list = following.get(followerId);

        if (!Objects.isNull(list)) {
            list.remove(followeeId);
        }
    }
}

/**
 * Your Twitter object will be instantiated and called as such:
 * Twitter obj = new Twitter();
 * obj.postTweet(userId,tweetId);
 * List<Integer> param_2 = obj.getNewsFeed(userId);
 * obj.follow(followerId,followeeId);
 * obj.unfollow(followerId,followeeId);
 */

class Solution {
    public int secondsBetweenTimes(String startTime, String endTime) {

        return convertToSecond(endTime) - convertToSecond(startTime);

    }

    private int convertToSecond(String time) {

        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        int sec = Integer.parseInt(time.substring(6, 8));

        return hour * 3600 + min * 60 + sec;
    }
}
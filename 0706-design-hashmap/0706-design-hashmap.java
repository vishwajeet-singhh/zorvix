class MyHashMap {

    private final int[] value;

    public MyHashMap() {
        value = new int[1_000_001];
        Arrays.fill(value, -1);
    }

    public void put(int key, int val) {
        value[key] = val;
    }

    public int get(int key) {
        return value[key];
    }

    public void remove(int key) {
        value[key] = -1;
    }
}

/**
 * Your MyHashMap object will be instantiated and called as such:
 * MyHashMap obj = new MyHashMap();
 * obj.put(key,value);
 * int param_2 = obj.get(key);
 * obj.remove(key);
 */
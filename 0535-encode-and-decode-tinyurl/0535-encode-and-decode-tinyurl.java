public class Codec {

    List<String> urls;

    private static final String PRE_URL = "http://tinyurl.com/";

    public Codec() {
        urls = new ArrayList();
    }

    // Encodes a URL to a shortened URL.
    public String encode(String longUrl) {
        urls.add(longUrl);

        return PRE_URL + String.valueOf(urls.size());

    }

    // Decodes a shortened URL to its original URL.
    public String decode(String shortUrl) {

        String [] arr = shortUrl.split("/");

        String val = arr[arr.length-1];

        int index = Integer.parseInt(val);

        return urls.get(index-1);



    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(url));
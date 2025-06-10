package cc.mrbird.febs.job.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetCheckUtils {
    public static void main(String[] args) {
        System.out.println(canAccessYouTube() ? "可以访问 YouTube" : "无法访问 YouTube");
    }

    public static boolean canAccessYouTube() {
        try {
            URL url = new URL("https://www.youtube.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int statusCode = connection.getResponseCode();
            connection.disconnect();
            return statusCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
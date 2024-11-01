package org.intro.retojfxhib;


public class Main {
    public static void main(String[] args) {
       String url = "https://www.youtube.com/watch?v=67vbA5ZJdKQ&t=1s";
       System.out.println(parseYoutubeUrl(url));
    }

    private static String parseYoutubeUrl(String url) {
        String[] parts = url.split("watch\\?v=");
        return parts[0] + "embed/" + parts[1].split("&t=")[0] + "?fs=1";
    }
}

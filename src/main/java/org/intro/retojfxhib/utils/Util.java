package org.intro.retojfxhib.utils;

import org.intro.retojfxhib.dao.MovieCopyDAO;

import java.io.*;
import java.util.Random;

public final class Util {

    public static String getRandomCondition(MovieCopyDAO movieCopyDAO) {
        Random rand = new Random(System.currentTimeMillis());
        Long conditionsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesCondition().get(rand.nextInt(0, conditionsLen.intValue()));
    }

    public static String getRandomPlatform(MovieCopyDAO movieCopyDAO) {
        Random rand = new Random(System.currentTimeMillis());
        Long platformsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesPlatform().get(rand.nextInt(0, platformsLen.intValue()));
    }

    public static String parseYoutubeUrl(String url) {
        String[] parts = url.split("watch\\?v=");
        return parts[0] + "embed/" + parts[1].split("&t=")[0] + "?fs=1";
    }

    public static void saveImageInMediaFolder(File image) {
        File mediaDir = new File(System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/");
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        File out = new File(mediaDir, image.getName());
        try (
                var bis = new BufferedInputStream(new FileInputStream(image.getCanonicalPath()));
                var bos = new BufferedOutputStream(new FileOutputStream(out))
        ) {
            int bytesRead;
            while ((bytesRead = bis.read()) != -1) {
                bos.write(bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String definePathForImg(String imageName) {
        return "file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + imageName;
    }
}

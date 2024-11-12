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

    public static boolean validEmail(String email) {
        String regex = ".+@[a-zA-Z]+\\.[a-zA-z]{2,4}";
        return email.matches(regex);
    }

    public static boolean validPassword(String pass) {
        boolean res = false;
        boolean containsUpper = false;
        boolean containsLower = false;
        boolean containsNumber = false;
        int i = 0;
        while(i < pass.length() && !res) {
            if(Character.isLowerCase(pass.charAt(i)))
                containsLower = true;
            if(Character.isDigit(pass.charAt(i)))
                containsNumber = true;
            if(Character.isUpperCase(pass.charAt(i)))
                containsUpper = true;
            res = containsNumber && containsUpper && containsLower;
            i++;
        }
        return res;
    }

    public static String randomRegisterCode() {
        Random random = new Random(System.currentTimeMillis());
        String seed = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < 5; i++) {
            int index = random.nextInt(seed.length());
            code.append(seed.charAt(index));
        }
        return code.toString();
    }
}

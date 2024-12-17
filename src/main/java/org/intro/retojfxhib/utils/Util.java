package org.intro.retojfxhib.utils;

import org.intro.retojfxhib.dao.MovieCopyDAO;

import java.io.*;
import java.util.Random;

/**
 * Clase de utilidades para procesos específicos.
 * @author Alberto Guzman
 */
public final class Util {

    /**
     * Método para generar Estados/Condiciones de películas aleatori@s
     * para que tengan un valor a la hora de guardarl@s.
     * @param movieCopyDAO DAO de las copias para elegir un valor aleatorio.
     * @return movieCondition: String
     */
    public static String getRandomCondition(MovieCopyDAO movieCopyDAO) {
        Random rand = new Random(System.currentTimeMillis());
        Long conditionsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesCondition().get(rand.nextInt(0, conditionsLen.intValue()));
    }

    /**
     * Método para generar plataformas de películas aleatorias
     * para que tengan un valor a la hora de guardarlas.
     * @param movieCopyDAO DAO de las copias para elegir un valor aleatorio.
     * @return moviePlatform: String
     */
    public static String getRandomPlatform(MovieCopyDAO movieCopyDAO) {
        Random rand = new Random(System.currentTimeMillis());
        Long platformsLen = movieCopyDAO.getNumOfConditions();
        return movieCopyDAO.getCopiesPlatform().get(rand.nextInt(0, platformsLen.intValue()));
    }

    /**
     * Método usado para parsear el link de youtube y añadirle las
     * partes de embed para poder introducir el el enlace en el web engine
     * de javaFx, también se le añade fs=1 para permitir la pantalla completa.
     * @param url Del video de youtube
     * @return url:String ya parseada y lista para reproducir en el visualizador
     * embedido.
     */
    public static String parseYoutubeUrl(String url) {
        String[] parts = url.split("watch\\?v=");
        return parts[0] + "embed/" + parts[1].split("&t=")[0] + "?fs=1";
    }

    /**
     * Método para guardar una imagen cuando se arrastra hacia
     * el ImageView, cuando se guarda con este método se crea la imagen
     * en el directorio media del proyecto.
     * @param image File de la imagen a guardar.
     */
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

    /**
     * Método para recuperar un recurso de imagen específico de una Movie
     * desde el directorio media.
     * @param imageName nombre de la imagen para localizarla.
     * @return Path de la imagen.
     */
    public static String definePathForImg(String imageName) {
        return "file:" + System.getProperty("user.dir") + "/src/main/resources/org/intro/retojfxhib/media/" + imageName;
    }

    /**
     * Método al que le pasamos un email y a través de una expresión regular
     * validamos si tiene el formato adecuado.
     * @param email A validar.
     * @return Boolean en función de si es válido o no.
     */
    public static boolean validEmail(String email) {
        String regex = ".+@[a-zA-Z]+\\.[a-zA-z]{2,4}";
        return email.matches(regex);
    }

    /**
     * Método para comprobar que la contraseña es válida, es decir
     * que contiene mínimo una mayúscula, un número y es de 8 caracteres.
     * @param pass A validar tipo String.
     * @return Boolean en función de si es válida o no.
     */
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

    /**
     * Método de generación de códigos de verificación aleatorios
     * @param len Número de argumentos que tendrá el código.
     * @return code:String
     */
    public static String randomRegisterCode(int len) {
        Random random = new Random(System.currentTimeMillis());
        String seed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < len; i++) {
            int index = random.nextInt(seed.length());
            code.append(seed.charAt(index));
        }
        return code.toString();
    }

    public static void emptyReportsDir() {
        File reportsDir = new File("reports");
        if(hasFilesInDirectory(reportsDir.getPath())) {
                var reports = reportsDir.listFiles();
                for (File report : reports) {
                    if (!report.delete()) {
                        System.err.println("No se pudo eliminar el archivo: " + report.getAbsolutePath());
                    }
                }
            System.out.println("Cyclic purge ended");
        }
    }


    public static boolean hasFilesInDirectory(String path) {
        File dir = new File(path);
        return dir.exists() && dir.isDirectory() && dir.listFiles() != null && dir.listFiles().length > 0;
    }
}

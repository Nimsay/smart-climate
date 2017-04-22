package source;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public abstract class Downloader {

    public Downloader() throws IOException {

    }

    /**
     * Permet de télécharger les fichiers
     * @param urlString L'url du fichier à télécharger.
     * @param destFilename Le nom du fichier de destination dans lequel le fichier doit être téléchargé.
     * @return true si le fichier est correctement téléchargé, false sinon.
     */
    public boolean download(String urlString, String destFilename) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());

        long completeFileSize = httpConnection.getContentLength();

        if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK || completeFileSize <= 0){
            return false;
        }

        // System.out.println(completeFileSize);
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(destFilename);
        java.io.BufferedOutputStream bout = new BufferedOutputStream( fos, 1024);
        byte[] data = new byte[1024];
        long downloadedFileSize = 0;
        int x = 0;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            downloadedFileSize += x;
            // calculate progress
            // final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100);
            // System.out.println("progress: " + String.valueOf(currentProgress));
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
        return true;
    }

    public abstract boolean downloadMonth(String year, String month) throws IOException;

}

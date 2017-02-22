package parser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

/**
 * Created by Thunderstorm on 16/02/2017.
 */

public class Downloader {

    final String cacheDir = "./cache";

    Downloader() throws IOException {

        File cache = new File(cacheDir);
        if (!cache.exists() && !cache.mkdirs() ){
            throw new IOException("Cannot create cache directory");
        }
    }

    private boolean gunzip(String inputGzFile, String outputFile){

        byte[] buffer = new byte[1024];

        try{
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputGzFile));
            FileOutputStream out = new FileOutputStream(outputFile);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gzis.close();
            out.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean download(String urlString, String filename) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        long completeFileSize = httpConnection.getContentLength();

        if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK || completeFileSize <= 0){
            return false;
        }

        // System.out.println(completeFileSize);
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream( filename);
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

    public String download(String urlString) throws IOException {

        String filename = urlString.substring( urlString.lastIndexOf('/')+1, urlString.length() );
        String basename = filename.substring( 0, filename.lastIndexOf('.'));
        String srcFilePath = Paths.get(cacheDir, filename).toString();
        String dstFilePath = Paths.get(cacheDir, basename).toString();

        File csvFile = new File(dstFilePath);
        if (csvFile.exists()){
            System.out.println("csv already in cache, use it.");
            return dstFilePath;
        }
        System.out.println("Download: " + urlString + " ==> " + srcFilePath);
        download(urlString, srcFilePath);

        System.out.println("GunZip: " + srcFilePath + " ==> " + dstFilePath);
        if ( gunzip(srcFilePath, dstFilePath) ){
            return dstFilePath;
        }

        return "";

    }


}

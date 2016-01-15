/*
 * Copyright (c) Jipzingking 2015.
 */

package spider;

import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApkDownloader {
    public static final int BUFFER_SIZE = 1 << 22;

    private static final Logger logger = Logger.getLogger(ApkDownloader.class);

    public static void download(String downloadLink, String filename) throws ApkDownloadException {
        try {
            URL downloadURL = new URL(downloadLink);
            boolean retryFlag;
            byte[] cache = new byte[BUFFER_SIZE];
            do {
                retryFlag = false;
                HttpURLConnection urlConnection = (HttpURLConnection) downloadURL.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (InputStream inputStream = urlConnection.getInputStream()) {
                        try (OutputStream outputStream = new FileOutputStream(filename)) {
                            try {
                                int len;
                                while ((len = inputStream.read(cache)) != -1)
                                    try {
                                        outputStream.write(cache, 0, len);
                                    } catch (IOException e) {
                                        throw new ApkDownloadException(e);
                                    }
                            } catch (IOException e) {
                                logger.warn("Download " + filename + " fail. Retry.");
                                retryFlag = true;
                            }
                        }
                    }
                } else {
                    throw new ApkDownloadException("download " + filename + " error. Http response code " + responseCode);
                }
            } while (retryFlag);
        } catch (IOException e) {
            throw new ApkDownloadException(e);
        }
        logger.info("download " + filename + " successful.");
    }

    public static class ApkDownloadException extends RuntimeException {
        public ApkDownloadException(Throwable e) {
            super(e);
        }

        public ApkDownloadException(String message) {
            super(message);
        }
    }
}

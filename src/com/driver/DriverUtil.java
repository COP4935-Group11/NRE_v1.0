package com.driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class DriverUtil {

    public static URL getUrl(String rawUrl, String defaultProtocol) throws MalformedURLException, URISyntaxException {
        URL url = null;
        try {
            url = new URL(rawUrl);
        } catch (MalformedURLException e) {
            try {
                url = new URI(rawUrl).toURL();
            } catch (IllegalArgumentException ex) {
                url = new URI(defaultProtocol + "://" + rawUrl).toURL();
            } catch (URISyntaxException ex) {
                url = Paths.get(rawUrl).toUri().toURL();
            }
        }
        return url;
    }
	
	
	
}

package com.WebMonitoring.service;

import com.WebMonitoring.entities.URLEntity;
import com.WebMonitoring.user.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service("ValidateURL")
public class ValidateURL implements ServerValidate {

    @Autowired
    private URLRepository urlRepository;

    @Override
    public boolean validate(String URLString) {
        try {
            final URI uri = new URL(URLString).toURI();


            HttpURLConnection huc = (HttpURLConnection) uri.toURL().openConnection();
            huc.setRequestMethod("HEAD");
            int responseCode = huc.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (Exception e) {
            System.out.println("URL Validation failed: " + e.getMessage());
            return false;
        }
    }

    public URLEntity saveUrlEntity(URLEntity urlEntity) {
        return urlRepository.save(urlEntity);
    }
}

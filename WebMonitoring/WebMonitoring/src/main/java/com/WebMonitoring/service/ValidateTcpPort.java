package com.WebMonitoring.service;

import com.WebMonitoring.entities.URLEntity;
import com.WebMonitoring.user.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;

@Service("ValidateTcpPort")
public class ValidateTcpPort implements ServerValidate {

    @Autowired
    private URLRepository urlRepository;

    @Override
    public boolean validate(String input) {
        String[] parts = input.split(":");
        if (parts.length != 2) {
            return false;
        }

        String host = parts[0];
        int port;
        try {
            port = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return false;
        }

        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            System.out.println("TCP Validation failed: " + e.getMessage());
            return false;
        }
    }

    public URLEntity saveUrlEntity(URLEntity urlEntity) {
        return urlRepository.save(urlEntity);
    }
}

package com.WebMonitoring.service;

import com.WebMonitoring.entities.IncidentEntity;
import com.WebMonitoring.entities.URLEntity;
import com.WebMonitoring.user.IncidentRepository;
import com.WebMonitoring.user.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class URLStatusScheduler {

    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Scheduled(fixedRate = 120000)
    public void checkURLStatus() {
        List<URLEntity> urls = urlRepository.findAll();
        for (URLEntity urlEntity : urls) {
            boolean isUp = isSiteUp(urlEntity.getURL());
            urlEntity.setURLStat(isUp ? "Up" : "Down");
            urlRepository.save(urlEntity);

            if (!isUp) {
                saveIncident(urlEntity);
            }
        }
    }

    public boolean isSiteUp(String urlString) {
        try {
            URL site = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) site.openConnection();
            conn.getContent();
            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

    private void saveIncident(URLEntity urlEntity) {
        IncidentEntity incident = new IncidentEntity();
        incident.setURL(urlEntity);
        incident.setDescription("Error " + getHttpStatusCode(urlEntity.getURL()));
        incident.setTimestamp(LocalDateTime.now());
        incident.setLastChecked(LocalDateTime.now());
        incidentRepository.save(incident);
    }

    private int getHttpStatusCode(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.connect();
            return conn.getResponseCode();
        } catch (IOException e) {
            return -1;
        }
    }

    public List<IncidentEntity> getIncidentsForURL(Long urlId) {
        URLEntity urlEntity = urlRepository.findById(urlId).orElse(null);
        return urlEntity != null ? incidentRepository.findByURL(urlEntity) : null;
    }
}

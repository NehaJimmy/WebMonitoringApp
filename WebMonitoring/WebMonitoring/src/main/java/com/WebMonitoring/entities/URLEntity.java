package com.WebMonitoring.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class URLEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long URLid;

    @Column
    private String URLStat;

    @Column(unique = true)
    private String URL;

    @Column(nullable = false)
    private long TimeInterval = 120000;
    @OneToMany(mappedBy = "URL", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidentEntity> incidents;
    public URLEntity() {}



    public Long getURLid() {
        return URLid;
    }

    public void setURLid(Long URLid) {
        this.URLid = URLid;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public long getTimeInterval() {
        return TimeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        TimeInterval = timeInterval;
    }

    public String getURLStat() {
        return URLStat;
    }

    public void setURLStat(String URLStat) {
        this.URLStat = URLStat;
    }

    public List<IncidentEntity> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<IncidentEntity> incidents) {
        this.incidents = incidents;
    }


}

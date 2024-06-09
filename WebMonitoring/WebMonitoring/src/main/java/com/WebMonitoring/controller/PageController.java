package com.WebMonitoring.controller;

import com.WebMonitoring.entities.IncidentEntity;
import com.WebMonitoring.entities.URLEntity;
import com.WebMonitoring.service.URLStatusScheduler;
import com.WebMonitoring.service.ServerValidate;
import com.WebMonitoring.user.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    @Qualifier("ValidateURL")
    private ServerValidate validateURL;

    @Autowired
    @Qualifier("ValidateTcpPort")
    private ServerValidate validateTcpPort;

    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private URLStatusScheduler urlStatusScheduler;

    @GetMapping("/")
    public String home(Model model) {
        List<URLEntity> urls = urlRepository.findAll();
        model.addAttribute("urls", urls);
        model.addAttribute("urlDetails", new URLEntity()); // Initialize form backing object
        return "home";
    }

    @PostMapping(path = "/add-site")
    public String addURL(@ModelAttribute("urlDetails") URLEntity urlEntity, Model model) {
        try {
            String inputUrl = urlEntity.getURL();
            boolean isValid;
            if (isLikelyURL(inputUrl)) {
                isValid = validateURL.validate(inputUrl);
            } else {
                isValid = validateTcpPort.validate(inputUrl);
            }

            if (isValid) {
                urlRepository.save(urlEntity);
                model.addAttribute("success", "URL added successfully!");
            } else {
                model.addAttribute("error", "Validation failed for the provided URL or TCP port.");
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "URL already exists.");
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred: " + e.getMessage());
        }
        model.addAttribute("urls", urlRepository.findAll());
        return "home";
    }

    private boolean isLikelyURL(String input) {
        return input.matches("^(http|https|ftp)://.*$");
    }

    @GetMapping("/incident/{urlId}")
    public String showIncidents(@PathVariable Long urlId, Model model) {
        List<IncidentEntity> incidents = urlStatusScheduler.getIncidentsForURL(urlId);
        model.addAttribute("incidents", incidents);
        return "Incident";
    }
}

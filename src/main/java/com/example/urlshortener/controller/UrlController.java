package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.services.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/url")
public class UrlController {

    private UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> createShortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");

        if (originalUrl == null) {
            return (ResponseEntity<Map<String, String>>) ResponseEntity.notFound();
        }

        String shortenUrl = urlService.shortenUrl(originalUrl);
        Map<String, String> response = new HashMap<String, String>();
        response.put("url", "https://totex.dev/" + shortenUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<Url> urlOptional = urlService.getOriginalUrl(shortUrl);

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            System.out.println("Redirecting to: " + url.getOriginalUrl());

            return ResponseEntity.status(200).location(URI.create(url.getOriginalUrl())).build();
        }

        System.out.println("Url not found or expired: " + shortUrl);
        return ResponseEntity.notFound().build();
    }

}

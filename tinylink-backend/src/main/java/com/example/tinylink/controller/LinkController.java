package com.example.tinylink.controller;

import com.example.tinylink.model.Link;
import com.example.tinylink.service.LinkService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class LinkController {

    private final LinkService service;

    public LinkController(LinkService service) {
        this.service = service;
    }

    @GetMapping("/healthz")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/api/links")
    public ResponseEntity<List<Link>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @PostMapping("/api/links")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        // Basic validation of incoming JSON structure
        if (body == null || !body.containsKey("targetUrl")) {
            return ResponseEntity.badRequest().body(Map.of("error", "targetUrl is required"));
        }

        String targetUrl = String.valueOf(body.get("targetUrl"));
        String code = body.containsKey("code") ? String.valueOf(body.get("code")) : null;

        if (targetUrl.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "targetUrl is required"));
        }

        try {
            Link created = service.createLink(targetUrl, code);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            // validation error from service (invalid URL or code format)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (DuplicateKeyException ex) {
            // Mongo duplicate key -> 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "code already exists"));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "code already exists"));
        } catch (Exception ex) {
            // log stacktrace server-side for debugging and return helpful JSON
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "internal_server_error", "message", ex.getMessage()));
        }
    }

    @GetMapping("/api/links/{code}")
    public ResponseEntity<?> getByCode(@PathVariable String code) {
        // capture the Optional<Link> locally
        java.util.Optional<com.example.tinylink.model.Link> maybe = service.findByCode(code);

        if (maybe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("error", "not found"));
        }

        // return the Link in a typed ResponseEntity
        return ResponseEntity.ok(maybe.get());
    }



    @DeleteMapping("/api/links/{code}")
    public ResponseEntity<?> delete(@PathVariable String code) {
        if (service.findByCode(code).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        service.deleteByCode(code);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        var updatedOpt = service.incrementClicksAndGet(code);
        if (updatedOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        Link updated = updatedOpt.get();

        // Optionally: log updated.getTotalClicks() or return it via API elsewhere
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(updated.getTargetUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

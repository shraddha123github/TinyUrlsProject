package com.example.tinylink.service;

import com.example.tinylink.model.Link;
import com.example.tinylink.repo.LinkRepository;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class LinkService {
    private final LinkRepository repo;
    private final MongoTemplate mongo;
    private final Random rng = new Random();
    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public LinkService(LinkRepository repo, MongoTemplate mongo) { this.repo = repo; this.mongo = mongo; }

    public List<Link> listAll() { return repo.findAll(); }

    public Optional<Link> findByCode(String code) { return repo.findByCode(code); }

    public boolean existsByCode(String code) { return repo.existsByCode(code); }

    public Link createLink(String targetUrl, String codeIfProvided) {
        validateUrlOrThrow(targetUrl);
        String finalCode = codeIfProvided;
        if (finalCode == null || finalCode.isBlank()) finalCode = generateUniqueCode(7);
        Link link = new Link(finalCode, targetUrl);
        return repo.save(link);
    }

    private void validateUrlOrThrow(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
                throw new IllegalArgumentException("URL must start with http or https");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    private String generateUniqueCode(int n) {
        for (int attempts = 0; attempts < 8; attempts++) {
            String candidate = randomString(n);
            if (!repo.existsByCode(candidate)) return candidate;
        }
        return "g" + System.currentTimeMillis() % 10000000;
    }

    private String randomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(CODE_CHARS.charAt(rng.nextInt(CODE_CHARS.length())));
        return sb.toString();
    }

    public Optional<Link> incrementClicksAndGet(String code) {
        Query q = Query.query(Criteria.where("code").is(code));
        Update u = new Update().inc("totalClicks", 1).currentDate("lastClicked");

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(false);
        Link updated = mongo.findAndModify(q, u, options, Link.class);
        return Optional.ofNullable(updated);
    }
    public void deleteByCode(String code) { repo.deleteByCode(code); }
}

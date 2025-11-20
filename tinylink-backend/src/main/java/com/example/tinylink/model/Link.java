package com.example.tinylink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.OffsetDateTime;

@Document(collection = "link")
public class Link {
    @Id
    private String id;
    private String code;
    private String targetUrl;
    private Long totalClicks = 0L;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime lastClicked;

    public Link() {}
    public Link(String code, String targetUrl) { this.code = code; this.targetUrl = targetUrl; }

    public String getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }
    public Long getTotalClicks() { return totalClicks; }
    public void setTotalClicks(Long totalClicks) { this.totalClicks = totalClicks; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getLastClicked() { return lastClicked; }
    public void setLastClicked(OffsetDateTime lastClicked) { this.lastClicked = lastClicked; }
}

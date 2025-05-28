package com.example.server.dto;

public class MarkdownPostDTO extends PostDTO {
    private String content;

    public MarkdownPostDTO() {
        super();
        setType("markdown");
    }

    public MarkdownPostDTO(String id, UserSummary author, String timestamp, int likes, String content) {
        super(id, "markdown", author, timestamp, likes);
        this.content = content;
    }

    // Getter and Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
} 
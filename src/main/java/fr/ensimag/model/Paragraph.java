package fr.ensimag.model;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

    private int id;
    private int storyId;
    private int authorId;
    private String content;
    private boolean conclusive;

    public Paragraph(int id, int storyId, int authorId, String content, boolean isConclusive) {
        this.setConclusive(isConclusive);
        this.setId(id);
        this.setStoryId(storyId);
        this.setAuthorId(authorId);
        this.setContent(content);
    }

    /**
     * @return the conclusive
     */
    public boolean isConclusive() {
        return conclusive;
    }

    /**
     * @param conclusive the conclusive to set
     */
    public void setConclusive(boolean conclusive) {
        this.conclusive = conclusive;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the authorId
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId the authorId to set
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    /**
     * @return the storyId
     */
    public int getStoryId() {
        return storyId;
    }

    /**
     * @param storyId the storyId to set
     */
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}

package fr.ensimag.model;

import java.util.ArrayList;
import java.util.List;

public class Story {
    private int id;
    private int authorId;
    private boolean open;
    private boolean publicity;
    private String title;
    List<String> authors;

    public Story(int id, int authorId, boolean isOpen, boolean isPublic, String title) {

        this.setId(id);
        this.setAuthorId(authorId);
        this.setOpen(isOpen);
        this.setPublicity(isPublic);
        this.setTitle(title);
        this.authors = new ArrayList<String>();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * @return the publicity
     */
    public boolean isPublicity() {
        return publicity;
    }

    /**
     * @param publicity the publicity to set
     */
    public void setPublicity(boolean publicity) {
        this.publicity = publicity;
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


    /**
     * @return authors
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * adds author's username if not existing in list of authors
     * @param aut
     */
    public void addAuthor(String aut){
        boolean found = false;
        for (String auteur : authors){
            if(auteur.equals(aut)){
                found = true;
            }
        }
        if(!found){
            authors.add(aut);
        }
    }

}
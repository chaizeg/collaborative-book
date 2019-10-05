package fr.ensimag.model;
public class Choice{

    private int storyId;
    private int sourceId;
    private int destinationId;
    private int choiceId;
    private String title;
    private int locked;

    public Choice(String title, int storyId, int sourceId, int choiceId, int destinationId) {
        this.setTitle(title);
        this.setStoryId(storyId);
        this.setSourceId(sourceId);
        this.setDestinationId(destinationId);
        this.setChoiceId(choiceId);
        locked = 0;
    }

    
    public Choice(String title, int storyId, int sourceId, int choiceId, int destinationId, int locked) {
        this.setTitle(title);
        this.setStoryId(storyId);
        this.setSourceId(sourceId);
        this.setDestinationId(destinationId);
        this.setChoiceId(choiceId);
        this.locked = locked;
    }
    /**
     * @return the choiceId
     */
    public int getChoiceId() {
        return choiceId;
    }

    /**
     * @param choiceId the choiceId to set
     */
    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    /**
     * @return the destinationId
     */
    public int getDestinationId() {
        return destinationId;
    }

    /**
     * @param destinationId the destinationId to set
     */
    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    /**
     * @return the sourceId
     */
    public int getSourceId() {
        return sourceId;
    }



    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
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
     * @return the parameter locked
     */
    public int getLocked() {
        return locked;
    }
    /**
     * set locked to 1 
     */
    public void lock() {
        locked = 1;
    }

    public void setLocked(int locked){
        this.locked = locked;
    }
    /**
     * set locked to 0 
     */
    public void unlock() {
        locked = 0;
    }

    public Choice(String title, int storyId, int sourceId, int choiceId) {
        this.setTitle(title);
        this.setStoryId(storyId);
        this.setSourceId(sourceId);
        this.setChoiceId(choiceId);
        this.setDestinationId(-1);
        this.locked = 0;
    }
}

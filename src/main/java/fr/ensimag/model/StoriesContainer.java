package fr.ensimag.model;

import java.util.ArrayList;
import java.util.List;

public class StoriesContainer{
    private List<Story> openStories;

    public StoriesContainer(){
        openStories = new ArrayList<>();
    }

    public void appendOpenStory(Story story){
        openStories.add(story);
    }

    
    public List<Story> getOpenStories(){
            return openStories;
    }

}
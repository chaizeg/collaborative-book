package fr.ensimag.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParagraphContainer{
    public int id;
    public String content;
    public Map<Integer, String> childrenIds;


    public Map<Integer, String> getChildrenIds(){
        return childrenIds;
    }

    public String getContent(){
        return content;
    }
}
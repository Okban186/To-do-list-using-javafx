package com.example;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageCache {
    private Map<String,Image> cache = new HashMap<>();
     public Image getImage(String imagePath){
        if(!cache.containsKey(imagePath))
            cache.put(imagePath,new Image(imagePath));
        return cache.get(imagePath);
     }
     public void cleanCache(){
        cache.clear();
     }
}

package com.github.goose_patrol.util;

import java.util.Optional;

import com.badlogic.gdx.graphics.Texture;

public class InventoryItem {

    private String name;
    private Texture imageItem;
    private Optional<Event> useEvent;



    public InventoryItem(String name, String imageItem) {
        this.name = name;
        this.imageItem = new Texture(imageItem);
        this.useEvent = Optional.empty();
    }


    public void useItem() {

        getUseEvent().get().fire();

    }


    public String getName() {
        return name;
    }


    public Texture getImageItem() {
        return imageItem;
    }


    public Optional<Event> getUseEvent() {
        return useEvent;
    }


    public void setUseEvent(Optional<Event> useEvent) {
        this.useEvent = useEvent;
    }


}

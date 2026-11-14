package com.github.goose_patrol.util;

import java.util.Optional;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class for inventory item, which will be used to pass the hidden events
 * <p>
 * Contains a name, a texture for rendering and an associated event that they
 * will be used with
 */
public class InventoryItem {

    private String name;
    private Texture imageItem;
    private Optional<Event> useEvent;

    public InventoryItem(String name, String imageItem, Optional<Event> useEvent) {
        this.name = name;
        this.imageItem = new Texture(imageItem);
        this.useEvent = useEvent;

        // public InventoryItem(String name, String imageItem, Optional<Event> useEvent)
        // {
        // this.name = name;
        // this.imageItem = imageItem;

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

    // public String getImageItem() {
    // return imageItem;
    // }

    public Optional<Event> getUseEvent() {
        return useEvent;
    }

    public void setUseEvent(Optional<Event> useEvent) {
        this.useEvent = useEvent;
    }

}

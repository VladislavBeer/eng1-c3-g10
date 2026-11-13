package com.github.goose_patrol.util;

import java.util.Optional;

public class InventoryItem {

    private String name;
    private String imageItem;
    private Optional<Event> useEvent;

    public InventoryItem(String name, String imageItem, Optional<Event> useEvent) {
        this.name = name;
        this.imageItem = imageItem;
        this.useEvent = useEvent;
    }

    public void useItem() {

        getUseEvent().get().fire();

    }

    public String getName() {
        return name;
    }

    public String getImageItem() {
        return imageItem;
    }

    public Optional<Event> getUseEvent() {
        return useEvent;
    }

    public void setUseEvent(Optional<Event> useEvent) {
        this.useEvent = useEvent;
    }

}

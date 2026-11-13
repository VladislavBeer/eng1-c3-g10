package com.github.goose_patrol.util;

import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.EntityManager;
import com.github.goose_patrol.entity.Goose;

import java.util.HashMap;
import java.util.Map;

public class MapHelper {

    public final static BoundingBox LOWER_PATH = new BoundingBox(0, 1435, 116, 196);
    public final static BoundingBox VERTICAL_PATH = new BoundingBox(1324, 1435, 116, 1041);
    public final static BoundingBox UPPER_PATH = new BoundingBox(0, 1324, 950, 1041);
    public final static BoundingBox WATER_PRIMARY = new BoundingBox(0, 1224, 340, 796);
    public final static BoundingBox PATH_LOWER_CORNER = new BoundingBox(1100, 1435, 116, 300);
    public final static Map<BoundingBox, Area> BOUNDING_BOXES = Map.ofEntries(
        Map.entry(LOWER_PATH, Area.PATH),
        Map.entry(VERTICAL_PATH, Area.PATH),
        Map.entry(UPPER_PATH, Area.PATH),
        Map.entry(WATER_PRIMARY, Area.LAKE),
        Map.entry(PATH_LOWER_CORNER, Area.PATH)
    );


    public static boolean isColliding(EntityManager em, BoundingBox eb, Entity primEntity) {


        for (Entity e : em.getEntities()) {

            if (primEntity.equals(e)) continue;
            // We offset the bounding box to account for difference in image size and bounding box of the geese
            BoundingBox b = e.getEntityBoundingBox().clone();
            if (e instanceof Goose) {
                b.setMinX(b.getMinX() + 25);
                b.setMaxX(b.getMaxX() - 25);
                b.setMinY(b.getMinY() + 5);
                b.setMaxY(b.getMaxY() - 5);
            }

            if (eb.getMinX() < b.getMaxX() &&
                eb.getMinY() > b.getMinX() &&
                eb.getMinY() < b.getMaxY() &&
                eb.getMinY() > b.getMinY())
                return true;

        }

        return false;

    }

    public static Area getCurrentArea(float posX, float posY) {


        for (BoundingBox b : BOUNDING_BOXES.keySet()) {

            if (posX > b.getMinX() &&
                posX < b.getMaxX() &&
                posY > b.getMinY() &&
                posY < b.getMaxY()) {

                return BOUNDING_BOXES.get(b);

            }

        }

        return Area.GRASS;

    }

}

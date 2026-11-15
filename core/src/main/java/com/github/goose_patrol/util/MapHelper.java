package com.github.goose_patrol.util;

import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.EntityManager;
import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.entity.staticentity.building.BusStopEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for map-related operations
 * <p>
 * Contains methods for collisions between entities and a getter for the current
 * area
 */
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
            Map.entry(PATH_LOWER_CORNER, Area.PATH));

    public static int isColliding(EntityManager em, BoundingBox eb, Entity primEntity) {

        for (Entity e : em.getEntities()) {

            if (primEntity.equals(e))
                continue;
            // We offset the bounding box to account for difference in image size and
            // bounding box of the geese
            BoundingBox b = e.getEntityBoundingBox().clone();
            if (e instanceof Goose) {
                b.setMaxY(b.getMaxY() + 10);
            }

            if (e instanceof BusStopEntity) {
                b.setMinX(b.getMinX() + 10);
                b.setMaxX(b.getMaxX() - 10);
                b.setMinY(b.getMinY() + 20);
                b.setMaxY(b.getMaxY() + 10);
            }

            if (eb.getMinX() < b.getMaxX() &&
                    eb.getMinX() > b.getMinX() - 2 &&
                    eb.getMinY() > b.getMinY() - 2 &&
                    eb.getMinY() < b.getMaxY() - 2 &&
                    eb.getMaxX() > b.getMinX() - 2) {

                if (e instanceof BusStopEntity) {

                    return 2;

                } else if (e instanceof Goose)
                    System.out.println("collision with goose detected");
                else if (e instanceof StaticEntity) {
                    System.out.println("Collision with tree or other detected");
                }
                return 1;
            }

        }

        return 0;

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

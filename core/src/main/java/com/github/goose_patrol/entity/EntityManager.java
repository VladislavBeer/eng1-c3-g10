package com.github.goose_patrol.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.entity.staticentity.building.BuildingEntity;
import com.github.goose_patrol.entity.staticentity.building.BusStopEntity;
import com.github.goose_patrol.entity.staticentity.tree.*;
import com.github.goose_patrol.util.BoundingBox;

/**
 * Manager for all entities, for access by classes and to update/draw all
 * current entities
 * <p>
 * Defines a list of type Entity to store entities that are currently in the
 * game, as well as a graph nodes/connections for pathfinding
 * <p>
 * Contains methods to tick all entities contained which will be called every
 * frame, it also initialises with all static entities already loaded: trees,
 * buildings and bus stop
 */
public class EntityManager {

    private GoosePatrol instance;
    private List<Entity> loadedEntities;
    private Player playerEntity;
    private final float NODE_REACH_DISTANCE = 15f;
    private final float PLAYER_FOLLOW_DISTANCE = 180f;

    // Array of graph nodes used for enemy navigation and pathfinding.
    private Vector2[] Graph_Nodes = {
            new Vector2(148, 150),
            new Vector2(384, 150),
            new Vector2(630, 150),
            new Vector2(840, 150),
            new Vector2(1017, 160),
            new Vector2(1190, 176),
            new Vector2(1264, 198),
            new Vector2(1331, 258),
            new Vector2(1369, 364),
            new Vector2(1386, 588),
            new Vector2(1376, 767),
            new Vector2(1334, 898),
            new Vector2(1299, 941),
            new Vector2(1241, 970),
            new Vector2(1078, 999),
            new Vector2(807, 1005),
            new Vector2(572, 1005),
            new Vector2(347, 1005),
            new Vector2(125, 998)
    };

    // Array of connections between graph nodes
    private Vector2[] Graph_Connections = {
            new Vector2(0, 1),
            new Vector2(1, 2),
            new Vector2(2, 3),
            new Vector2(3, 4),
            new Vector2(4, 5),
            new Vector2(5, 6),
            new Vector2(6, 7),
            new Vector2(7, 8),
            new Vector2(8, 9),
            new Vector2(9, 10),
            new Vector2(10, 11),
            new Vector2(11, 12),
            new Vector2(12, 13),
            new Vector2(13, 14),
            new Vector2(14, 15),
            new Vector2(15, 16),
            new Vector2(16, 17),
            new Vector2(17, 18)
    };

    public EntityManager(GoosePatrol instance) {
        this.instance = instance;
        this.loadedEntities = new ArrayList<>();
        loadStatics();
    }

    /**
     * To load in all static entities ie buildings and trees
     */
    private void loadStatics() {

        loadedEntities.add(new BuildingEntity(75, 225, new BoundingBox(75, (75 + 300), 225, (225 + 150)), instance));
        loadedEntities.add(new BusStopEntity(75, 1040, new BoundingBox(75, (75 + 200), 1040, (1040 + 100)), instance));
        loadedEntities.add(new TreeEntity1(10, 230, new BoundingBox(10, (10 + 80), 230, (230 + 150)), instance));
        loadedEntities.add(new TreeEntity1(375, 230, new BoundingBox(375, (375 + 70), 230, (230 + 160)), instance));
        loadedEntities.add(new TreeEntity1(1375, 100, new BoundingBox(1375, (1375 + 70), 100, (100 + 160)), instance));
        loadedEntities.add(new TreeEntity1(1450, 170, new BoundingBox(1450, (1450 + 70), 170, (170 + 160)), instance));
        loadedEntities.add(new TreeEntity1(1490, 50, new BoundingBox(1490, (1490 + 70), 50, (50 + 160)), instance));
        loadedEntities
                .add(new TreeEntity2(1300, 1100, new BoundingBox(1300, (1300 + 90), 1100, (1100 + 100)), instance));
        loadedEntities.add(new TreeEntity3(575, 330, new BoundingBox(575, (575 + 78), 330, (330 + 90)), instance));
        loadedEntities.add(new TreeEntity3(760, 370, new BoundingBox(760, (760 + 81), 370, (370 + 100)), instance));
        loadedEntities.add(new TreeEntity3(1000, 450, new BoundingBox(1000, (1000 + 82), 450, (450 + 70)), instance));
        loadedEntities.add(new TreeEntity3(1220, 650, new BoundingBox(1220, (1220 + 76), 650, (650 + 80)), instance));
        loadedEntities.add(new TreeEntity4(1000, 800, new BoundingBox(1000, (1000 + 90), 800, (800 + 150)), instance));
        loadedEntities.add(new TreeEntity4(1090, 750, new BoundingBox(1090, (1090 + 100), 750, (750 + 160)), instance));
        loadedEntities.add(new TreeEntity4(1170, 820, new BoundingBox(1170, (1170 + 110), 820, (820 + 110)), instance));
        loadedEntities.add(new TreeEntity5(1200, 290, new BoundingBox(1200, (1200 + 80), 290, (290 + 75)), instance));
        loadedEntities.add(new TreeEntity5(1000, 360, new BoundingBox(1000, (1000 + 80), 360, (360 + 100)), instance));
        loadedEntities.add(new TreeEntity5(1156, 420, new BoundingBox(1156, (1156 + 80), 420, (420 + 75)), instance));
        loadedEntities.add(new TreeEntity6(190, 800, new BoundingBox(190, (190 + 120), 800, (800 + 140)), instance));
        loadedEntities.add(new TreeEntity6(325, 820, new BoundingBox(325, (325 + 110), 820, (820 + 130)), instance));
        loadedEntities.add(new TreeEntity6(430, 1080, new BoundingBox(430, (430 + 90), 1080, (1080 + 110)), instance));
        loadedEntities.add(new TreeEntity6(550, 1090, new BoundingBox(550, (550 + 90), 1090, (1090 + 105)), instance));
        loadedEntities.add(new TreeEntity6(700, 1070, new BoundingBox(700, (700 + 85), 1070, (1070 + 100)), instance));
        loadedEntities.add(new TreeEntity6(890, 1100, new BoundingBox(890, (890 + 80), 1100, (1100 + 90)), instance));

    }

    public void tickEntities(float delta) {
        loadedEntities.forEach(e -> e.tickEntity(delta));
    }

    public void addEntity(Entity e) {
        loadedEntities.add(e);
    }

    public List<Entity> getEntities() {
        return this.loadedEntities;
    }

    public void Set_Player(Player player) {
        this.playerEntity = player;
    }

    public Player Get_Player() {
        return this.playerEntity;
    }

    public float Get_Node_Reach_Distance() {
        return this.NODE_REACH_DISTANCE;
    }

    public float Get_Player_Follow_Distance() {
        return this.PLAYER_FOLLOW_DISTANCE;
    }

    public Vector2[] Get_Graph_Nodes() {
        return this.Graph_Nodes;
    }

    public Vector2[] Get_Graph_Connections() {
        return this.Graph_Connections;
    }

}

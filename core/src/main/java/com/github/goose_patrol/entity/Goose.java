package com.github.goose_patrol.entity;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.List;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.util.Area;
import com.github.goose_patrol.util.BoundingBox;
import com.github.goose_patrol.util.MapHelper;

public class Goose extends Entity {

    private boolean triggered;

    /** Map of direction states to their corresponding textures */
    EnumMap<directionState, TextureRegion> directionIdleTextures = new EnumMap<>(directionState.class);
    /** Map of walking animation sheets to their corresponding direction states */
    EnumMap<directionState, Animation<TextureRegion>> walkingAnimations = new EnumMap<>(directionState.class);
    private final Array<Texture> textures = new Array<>(); // Keep track of loaded textures for disposal
    private float stateTime = 0f; // Time spent in current state, for future animation purposes.

    private Entity Entity_To_Follow;
    private int targetNode = -1;

    public Goose(float posX, float posY, String image, boolean active, boolean triggered, GoosePatrol instance) {
        super(posX, posY, image, new BoundingBox(posX, posX + 50, posY, posY + 50), instance);
        this.triggered = triggered;

        // Load textures now to avoid doing it repeatedly later.
        loadTextures();
        this.setCurrentDirection(directionState.E); // default to east-facing
    }

    // Load texture or animation frame based on the current direction state and movement of the goose
    @Override
    public TextureRegion getEntityTexture() {
        directionState dir = getCurrentDirection();
        if (isMoving) {
            Animation<TextureRegion> anim = walkingAnimations.get(dir);
            if (anim != null)
                return anim.getKeyFrame(stateTime, true);
        }
        return directionIdleTextures.get(dir);
    }

    // Helper method to load the textures, ported from Player.java but modified for goose.
    private void loadTextures() {
        // Load textures for each direction state
        // assets stored at 'eng1-c3-g10\assets\Goose'
        // Should be handled by AssetManager in future but for now we can just load them directly
        final int frameW = 32, frameH = 32, frameCount = 5;

        for (directionState d : directionState.values()) {
            String texturePath = "goose/Goose" + d.name() + "_spritesheet.png";
            Texture sheet = new Texture(Gdx.files.internal(texturePath));
            sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textures.add(sheet); // keep a list so you can dispose them later
            // textures must be disposed of when the game ends to free up memory, this is a temporary solution until AssetManager implementation.

            // Frame 0 = idle
            directionIdleTextures.put(d, new TextureRegion(sheet, 0, 0, frameW, frameH));

            // Frames 1..4 = walk cycle (use i = 0 if idle is part of the walk too)
            Array<TextureRegion> frames = new Array<>();
            for (int i = 1; i < frameCount; i++) {
                frames.add(new TextureRegion(sheet, i * frameW, 0, frameW, frameH));
            }

            // geese travel faster than the player, so we can speed up the animation a bit
            walkingAnimations.put(d, new Animation<>(0.05f, frames, Animation.PlayMode.LOOP));
        }

    }

    // Helper method to determine the direction state, ported from Player.java may need to be modified for goose movement
    private directionState getDirectionState(float deltaX, float deltaY) {
        // Determine the sign of deltaX and deltaY to find the direction
        // Works as positive deltaY is north.
        int signX = deltaX > 0 ? 1 : (deltaX < 0 ? -1 : 0);
        int signY = deltaY > 0 ? 1 : (deltaY < 0 ? -1 : 0);

        if (signX == 0 && signY > 0)
            return directionState.N;
        if (signX > 0 && signY > 0)
            return directionState.NE;
        if (signX < 0 && signY > 0)
            return directionState.NW;
        if (signX == 0 && signY < 0)
            return directionState.S;
        if (signX > 0 && signY < 0)
            return directionState.SE;
        if (signX < 0 && signY < 0)
            return directionState.SW;
        if (signX > 0 && signY == 0)
            return directionState.E;
        if (signX < 0 && signY == 0)
            return directionState.W;

        return directionState.S; // default to south-facing if no movement
    }

    public void disposeTextures() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }

    @Override
    public void tickEntity(float delta) {

        float PLAYER_FOLLOW_DISTANCE = instance.getEntityManager().Get_Player_Follow_Distance();
        float NODE_REACH_DISTANCE = instance.getEntityManager().Get_Node_Reach_Distance();
        Vector2[] Graph_Nodes = instance.getEntityManager().Get_Graph_Nodes();
        Vector2[] Graph_Connections = instance.getEntityManager().Get_Graph_Connections();

        Vector2 Goose_Position = new Vector2(this.posX, this.posY);

        float dirX = 0;
        float dirY = 0;

        // Entity Pathfinding and Following Logic
        if (Entity_To_Follow != null) 
        {  // If goose has an entity to follow 
            Vector2 targetPosition = new Vector2(Entity_To_Follow.getPosX(),Entity_To_Follow.getPosY());

            float targetDistance = Goose_Position.dst(targetPosition);

            if (targetDistance <= PLAYER_FOLLOW_DISTANCE) 
            {  // 1. DIRECT ENTITY FOLLOWING
                Vector2 direction = new Vector2(targetPosition).sub(Goose_Position);

                if (direction.len2() > 0.001f)
                {
                    direction.nor();

                    dirX = direction.x;
                    dirY = direction.y;
                }
            }
            else  // 2. GRAPH NAVIGATION
            {
                int currentNode = 0;
                if (targetNode == -1)
                {
                    float closestDistance = Float.MAX_VALUE;

                    for (int i = 0; i < Graph_Nodes.length; i++)
                    {
                        float distance = Goose_Position.dst(Graph_Nodes[i]);

                        if (distance < closestDistance) 
                        {
                            closestDistance = distance;
                            currentNode = i;
                        }
                    }
                    targetNode = currentNode;
                }
                if (Goose_Position.dst(Graph_Nodes[targetNode]) <= NODE_REACH_DISTANCE) 
                {
                    currentNode = targetNode;
                    targetNode = -1;

                    float bestDistance = Float.MAX_VALUE;

                    for (Vector2 connection : Graph_Connections) 
                    {
                        int nodeA = (int) connection.x;
                        int nodeB = (int) connection.y;

                        int candidateNode = -1;

                        if (nodeA == currentNode) candidateNode = nodeB;

                        else if (nodeB == currentNode) candidateNode = nodeA;

                        if (candidateNode == -1) continue;

                        float distance = targetPosition.dst(Graph_Nodes[candidateNode]);

                        if (distance < bestDistance) 
                        {
                            bestDistance = distance;
                            targetNode = candidateNode;
                        }
                    }
                }
                if (targetNode != -1) 
                {
                    Vector2 direction = new Vector2(Graph_Nodes[targetNode]).sub(Goose_Position);

                    if (direction.len2() > 0.001f) 
                    {
                        direction.nor();

                        dirX = direction.x;
                        dirY = direction.y;
                    }
                }
            }
        }

        // Check if the goose is moving and update the direction state and apply animation accordingly
        float oldPosX = this.posX, oldPosY = this.posY; //store old position 
        Boolean wasMoving = this.isMoving;
        // 3. APPLY MOVEMENT
        moveEntity(dirX,dirY,delta,instance.getRenderManager().getMaxX(),instance.getRenderManager().getMaxY());

        // A. Check if the Goose is moving in any direction, by change from old position
        // to new position
        this.isMoving = posX != oldPosX || posY != oldPosY;

        // B. Update the Goose's state based on movement direction
        if (dirX != 0 || dirY != 0) {
            // determine the direction based on deltaX and deltaY
            this.setCurrentDirection(getDirectionState(dirX, dirY));
        }
        // else doesnt matter, no change from last direction if not moving,

        // C. Update the state time for animation purposes, flip if player stopped moving
        if (isMoving != wasMoving) {
            stateTime = 0f; // reset state time if movement state changed
        } else {
            stateTime += delta; // increment state time if still moving
        }

        super.tickEntity(delta);
    }

    public void moveEntity(float deltaX, float deltaY, float delta, float mapWidth, float mapHeight) {

        float WIDTH = this.getEntityBoundingBox().getMaxX() - this.getEntityBoundingBox().getMinX();
        float HEIGHT = this.getEntityBoundingBox().getMaxY() - this.getEntityBoundingBox().getMinY();
        float SPEED = 100f;

        Vector2 direction = new Vector2(deltaX, deltaY);
        if (!direction.isZero()) 
        {
            direction.nor(); // stops diagonal movement being faster
        }
        Vector2 position = new Vector2();
        position.x = posX;
        position.y = posY;
        Area currentArea = MapHelper.getCurrentArea(position.x, position.y);
        if (currentArea == Area.GRASS) SPEED = 100f;
        else SPEED = 200f;
        position.mulAdd(direction, SPEED * delta);
        position.x = MathUtils.clamp(position.x, 0, mapWidth - WIDTH);
        position.y = MathUtils.clamp(position.y, 0, mapHeight - HEIGHT);

        // Handles entity collisions
        
        BoundingBox newBoundingBox = getEntityBoundingBox().clone();

        newBoundingBox.setMinX(newBoundingBox.getMinX() + deltaX);
        newBoundingBox.setMinY(newBoundingBox.getMinY() + deltaY);
        newBoundingBox.setMaxX(newBoundingBox.getMaxX() + deltaX);
        newBoundingBox.setMaxY(newBoundingBox.getMaxY() + deltaY);        
        
        List<Entity> entities = instance.getEntityManager().getEntities();

        int thisIndex = entities.indexOf(this);

        for (int i = 0; i < entities.size(); i++) 
        {
            Entity entity = entities.get(i);

            if (entity == this) continue;

            boolean otherIsFollowTarget = Entity_To_Follow != null && entity == Entity_To_Follow;

            boolean otherIsGoose = entity instanceof Goose;

            if (!otherIsFollowTarget && !otherIsGoose) continue;

            // Higher-index goose has priority. It does not need to resolve collisions with lower-index geese.
            if (otherIsGoose && thisIndex > i) continue;

            BoundingBox otherBox = entity.getEntityBoundingBox();

            // Check for collision
            if (newBoundingBox.getMinX() < otherBox.getMaxX() && newBoundingBox.getMaxX() > otherBox.getMinX() && newBoundingBox.getMinY() < otherBox.getMaxY() && newBoundingBox.getMaxY() > otherBox.getMinY()) 
            {
                float overlapX = Math.min(newBoundingBox.getMaxX() - otherBox.getMinX(),otherBox.getMaxX() - newBoundingBox.getMinX());

                float overlapY = Math.min(newBoundingBox.getMaxY() - otherBox.getMinY(),otherBox.getMaxY() - newBoundingBox.getMinY());

                // Push away along the axis with the smallest overlap.
                if (overlapX < overlapY)
                {
                    if (position.x < entity.getPosX()) position.x -= overlapX;
                    else position.x += overlapX;

                } 
                else 
                {
                    if (position.y < entity.getPosY()) position.y -= overlapY;
                    else position.y += overlapY;
                }

                // Refresh bounding box after separation.
                newBoundingBox = getEntityBoundingBox().clone();

                newBoundingBox.setMinX(newBoundingBox.getMinX() + (position.x - posX));
                newBoundingBox.setMinY(newBoundingBox.getMinY() + (position.y - posY));
                newBoundingBox.setMaxX(newBoundingBox.getMaxX() + (position.x - posX));
                newBoundingBox.setMaxY(newBoundingBox.getMaxY() + (position.y - posY));
            }
        }

        this.posX = position.x;
        this.posY = position.y;

        this.getEntityBoundingBox().setMinX(this.posX);
        this.getEntityBoundingBox().setMaxX(this.posX + WIDTH);
        this.getEntityBoundingBox().setMinY(this.posY);
        this.getEntityBoundingBox().setMaxY(this.posY + HEIGHT);

    }

    public void Set_To_Follow(Entity entity) {
        this.Entity_To_Follow = entity;
    }
}

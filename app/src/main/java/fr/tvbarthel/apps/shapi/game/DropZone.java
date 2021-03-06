package fr.tvbarthel.apps.shapi.game;

import java.util.List;

import fr.tvbarthel.apps.shapi.shape.Shape;

/**
 * Encapsulate data linked to an area where the player can drop a shape.
 */
public class DropZone {

    private final Class<? extends Shape> shape;
    private final String shapeName;
    private final List<Class<? extends Shape>> availableShapes;
    @GameLevels.Level
    private final int gameLevel;

    /**
     * Encapsulate data linked to an area where the player can drop a shape.
     *
     * @param shape           shape allowed in this area.
     * @param availableShapes available shapes type which can be dropped in the zone.
     * @param gameLevel       a game level.
     */
    public DropZone(Class<? extends Shape> shape,
                    List<Class<? extends Shape>> availableShapes,
                    @GameLevels.Level int gameLevel) {
        this.shape = shape;
        this.shapeName = shape.getSimpleName();
        this.availableShapes = availableShapes;
        this.gameLevel = gameLevel;
    }

    /**
     * shape allowed in this area.
     *
     * @return shape allowed in this area.
     */
    public Class<? extends Shape> getShape() {
        return shape;
    }

    /**
     * Name of the shape allowed in this area.
     *
     * @return shape allowed in this area.
     */
    public String getShapeName() {
        return shapeName;
    }

    /**
     * available shapes type which can be dropped in the zone.
     *
     * @return available shapes type which can be dropped in the zone.
     */
    public List<Class<? extends Shape>> getAvailableShapes() {
        return availableShapes;
    }

    /**
     * Get the game level of this {@link DropZone}.
     *
     * @return the game level.
     */
    @GameLevels.Level
    public int getGameLevel() {
        return gameLevel;
    }
}

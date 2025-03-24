package normallynormal.World.Levels;

import normallynormal.Math.Vector2;
import normallynormal.World.Level;

public class TutorialLevel extends Level {
    public TutorialLevel() {
        int startingLocationX = 3600;
        int startingLocationY = 1200;
        player.getPosition().x = 5 + startingLocationX;
        player.getPosition().y = 34 + startingLocationY;
        player.setSpawnPosition(new Vector2(5 + startingLocationX, 34 + startingLocationY));
    }
}

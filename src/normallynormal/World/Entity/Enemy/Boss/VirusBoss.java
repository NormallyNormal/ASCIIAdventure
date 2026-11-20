package normallynormal.World.Entity.Enemy.Boss;

import normallynormal.Input.InputHandler;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.Player;
import normallynormal.World.Level;

public class VirusBoss extends Entity {

    public VirusBoss() {
        this.collisionBox = new AABB(this.position.x, this.position.y, 5, 5);
    }

    @Override
    public void process(double timeDelta, InputHandler input) {

    }

    @Override
    public void intersectEffect(Entity entity, Level level) {
        if(entity instanceof Player) {
            entity.kill();
        }
    }

    @Override
    public void copyForRender() {

    }
}

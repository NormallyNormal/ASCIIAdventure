package normallynormal.World.Levels;

import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Math.Vector2;
import normallynormal.Render.Renderers.ConnectedTextureRenderer;
import normallynormal.Render.Renderers.RainbowRenderer;
import normallynormal.World.Entity.Decoration.Torch;
import normallynormal.World.Entity.NPC;
import normallynormal.World.Entity.Orb;
import normallynormal.World.Level;
import normallynormal.World.Platform.*;
import normallynormal.World.Platform.Controller.WatchingPlatformController;

public class DevLevel extends Level {
    public DevLevel() {
        super();
        int startingLocationX = 3600;
        int startingLocationY = 1200;
        player.getPosition().x = 5 + startingLocationX;
        player.getPosition().y = 34 + startingLocationY;
        player.setSpawnPosition(new Vector2(5 + startingLocationX, 34 + startingLocationY));
//        player.getPosition().x = 5 + startingLocationX;
//        player.getPosition().y = -25 + startingLocationY;
//        player.setSpawnPosition(new Vector2(5 + startingLocationX, 34 + startingLocationY));

        int currentId = 0;
//        worldObjects.add(new MovingObject(new AABB(65, 19, 5, 1), currentId++, 0, 10, 0.25));
//        worldObjects.add(new MovingObject(new AABB(75, 19, 5, 1), currentId++, 0, 10, 0.2));
//        worldObjects.add(new StaticObject(new AABB(10, 5, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(55, 20, 50, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(5, 20, 50, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(70, 15, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(70, 10, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(35, 17, 5, 3), currentId++));
//        worldObjects.add(new StaticObject(new AABB(20, 10, 20, 1), currentId++));
//        worldObjects.add(new StaticObject(new AABB(35, 5, 2, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(20, 15, 10, 1), currentId++, true));
//        worldObjects.add(new StaticObject(new AABB(5, 30, 30, 1), currentId++, true));
//        worldObjects.add(new StaticObject(new AABB(20, 35, 10, 1), currentId++, true));
//        worldObjects.add(new StaticHazardObject(new AABB(20, 19, 5, 1), currentId++));
//        worldObjects.add(new MovingObject(new AABB(20, 5, 5, 5), currentId++, 10, 0, 0.4));
//        worldObjects.add(new StaticObject(new AABB(28, 2, 5, 3), currentId++));

        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + 35, 360, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + -40, 360, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + -35, 5, 70), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 235, startingLocationY + -35, 5, 70), currentId++));


        //The semi bois
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 30, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 25, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 20, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 15, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 10, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 5, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + -5, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + -10, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX - 115, startingLocationY + -15, 235, 1), currentId++));

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + 34, 110, 1), currentId++));

        MoveableObject mo1 = new MoveableObject(new AABB(startingLocationX + 70, startingLocationY + 33, 10, 2), currentId++);
        mo1.createBasicController(Direction.RIGHT, 30, 0.1);
        worldObjects.add(mo1);

        MoveableObject wo1 = new MoveableObject(new AABB(startingLocationX + 70, startingLocationY + 30, 10, 2), currentId++);
        wo1.setController(new WatchingPlatformController(wo1, Direction.UP, 10, 0.1));
        worldObjects.add(wo1);

        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 30, 2, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 28, 2, 2), currentId++));

        MoveableObject mo2 = new MoveableObject(new AABB(startingLocationX + 125, startingLocationY + 32, 10, 2), currentId++);
        mo2.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo2);
        MoveableObject mo3 = new MoveableObject(new AABB(startingLocationX + 125 + 25, startingLocationY + 32, 10, 2), currentId++);
        mo3.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo3);
        MoveableObject mo4 = new MoveableObject(new AABB(startingLocationX + 125 + 50, startingLocationY + 32, 10, 2), currentId++);
        mo4.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo4);
        MoveableObject mo5 = new MoveableObject(new AABB(startingLocationX + 125 + 75, startingLocationY + 32, 10, 2), currentId++);
        mo5.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo5);
        MoveableObject mo6 = new MoveableObject(new AABB(startingLocationX + 125 + 70, startingLocationY + 30, 10, 2), currentId++);
        mo6.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo6);

        MoveableObject mo7 = new MoveableObject(new AABB(startingLocationX - 60, startingLocationY + 23, 10, 2), currentId++);
        mo7.createBasicController(Direction.RIGHT, 10, 0.1);
        mo7.setRenderer(new RainbowRenderer(mo7::getVisibilityBox));
        worldObjects.add(mo7);

        MoveableObject mo8 = new MoveableObject(new AABB(startingLocationX - 40, startingLocationY + 32, 10, 2), currentId++);
        mo8.createBasicController(Direction.UP, 9, 0.2);
        mo8.setRenderer(new RainbowRenderer(mo8::getVisibilityBox));
        worldObjects.add(mo8);

        entities.add(new Torch(startingLocationX + 30, startingLocationY + 30));

        entities.add(new NPC(startingLocationX + 60, startingLocationY + 34));

        GravityField gf1 = new GravityField(new AABB(startingLocationX - 70, startingLocationY + 25, 10, 10), currentId++, true);
        gf1.createBasicController(Direction.LEFT, 20, 0.1);
        //demo of custom renderers
        //gf1.setRenderer(new SpikesRenderer(gf1::getVisibilityBox));
        worldObjects.add(gf1);
        worldObjects.add(new GravityField(new AABB(startingLocationX - 120, startingLocationY + 15, 120, 3), currentId++, false));

        StaticObject s1 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 23, 30 , 2), currentId++);
        s1.setRenderer(new RainbowRenderer(s1::getVisibilityBox));
        worldObjects.add(s1);
        StaticObject s2 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 13, 30 , 2), currentId++);
        s2.setRenderer(new RainbowRenderer(s2::getVisibilityBox));
        worldObjects.add(s2);

        StaticObject s3 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 6, 30 , 2), currentId++);
        s3.setRenderer(new RainbowRenderer(s3::getVisibilityBox));
        worldObjects.add(s3);

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + -10, 110, 1), currentId++));

        worldObjects.add(new BouncyObject(new AABB(startingLocationX + 20, startingLocationY + 33, 5, 2), currentId++));
        worldObjects.add(new BouncyObject(new AABB(startingLocationX - 95, startingLocationY + 17, 5, 2), currentId++));

        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 20, startingLocationY + -16, 20, 1), currentId++, Direction.RIGHT, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 40, startingLocationY + -25, 1, 10), currentId++, Direction.UP, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 41, startingLocationY + -25, 20, 1), currentId++, Direction.RIGHT, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 45, startingLocationY + -16, 20, 1), currentId++, Direction.LEFT, 10));

        MoveableObject mo9 = new MoveableObject(new AABB(startingLocationX -60, startingLocationY + -20, 10, 5), currentId++);
        mo9.createBasicController(Direction.RIGHT, 10, 0.2);
        MoveableObject mo10 = new MoveableObject(new AABB(startingLocationX -60, startingLocationY + -25, 5, 5), currentId++);
        mo10.createBasicController(Direction.RIGHT, 10, 0.2);
        ConnectedTextureRenderer cr1 = new ConnectedTextureRenderer();
        cr1.addVisibiliyBoxSupplier(mo9::getVisibilityBox);
        cr1.addVisibiliyBoxSupplier(mo10::getVisibilityBox);
        mo9.setRenderer(cr1);
        mo10.setRenderer(cr1);
        worldObjects.add(mo9);
        worldObjects.add(mo10);

        for (int x = 0; x < 30; x += 5) {
            entities.add(new Orb(startingLocationX + 28 + x, startingLocationY + 20, Orb.OrbType.DOUBLE_JUMP));
        }
        for (int x = 0; x < 30; x += 10) {
            entities.add(new Orb(startingLocationX + 28 + x, startingLocationY + 10, Orb.OrbType.DASH));
        }

        worldObjects.add(new StaticObject(new AABB(startingLocationX + 130, startingLocationY - 20, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 135, startingLocationY - 20, 5, 1), currentId++));
    }
}

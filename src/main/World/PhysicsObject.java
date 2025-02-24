package main.World;
import main.Math.Vector2;
public interface PhysicsObject {
    Vector2 getPosition();
    Vector2 getVelocity();
    Vector2 getMovementStep();
    Vector2 getAcceleration();
}
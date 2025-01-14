package World.Entity;

import Input.Input;
import Render.DepthScreen;
import World.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Vector2;
import Math.AABB;
import Math.Direction;

import java.awt.event.KeyEvent;

public class Player extends Entity{
    double timeDead;
    double jumpBuffer = 0;

    Direction lastHorizontalDirection = Direction.NONE;

    boolean hasDashCharge = true;
    boolean canDashCharge = true;
    double dashTime = 0;
    Direction dashDirection = Direction.NONE;
    double maxDashTime = 0.2;
    double dashCooldown = 0.4;
    double dashMovement = 0;
    double dashSpeed = 60;

    Direction wallJumpDirection = Direction.NONE;
    double wallJumpFixedDirectionTime = 0;

    public Player() {
        timeDead = 0;
        velocity = new Vector2(0,0);
        position = new Vector2(5.5, 5.5);
        depth = 10;
        gravity = new Vector2(0, 60);
        collisionBox = new AABB(position.x - 0.49, position.y - 0.49, 0.98, 0.98);
        noGravity = false;
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (!dead) {
            boolean onGroundRecently = timeSinceOnGround < 0.1;
            boolean hitWallRecently = timeSinceWallHit < 0.05;
            boolean hitWallSomewhatRecently = timeSinceWallHit < 0.2;
            if (input.getKeyState(KeyEvent.VK_UP)) {
                jumpBuffer = 0.05;
            }
            if (jumpBuffer > 0) {
                jumpBuffer -= timeDelta;
            }
            if (!input.getKeyState(KeyEvent.VK_UP) && velocity.y < 0) {
                velocity.y = 0;
            }
            //Wall slide
            if (timeSinceWallHit < 0.05) {
                if (velocity.y > 5) {
                    velocity.y = 5;
                }
            }

            if (jumpBuffer > 0) {
                boolean rightKey = input.getKeyState(KeyEvent.VK_RIGHT);
                boolean leftKey = input.getKeyState(KeyEvent.VK_LEFT);
                Direction possibleWallJumpDirection = rightKey & !leftKey ? Direction.RIGHT : Direction.NONE;
                possibleWallJumpDirection = leftKey & !rightKey ? Direction.LEFT : possibleWallJumpDirection;
                if (onGroundRecently) {
                    velocity.y = -30;
                    timeSinceOnGround = Double.POSITIVE_INFINITY;
                }
                else if (hitWallSomewhatRecently && possibleWallJumpDirection == lastCollisionDirection.opposite()) {
                    velocity.y = -15;
                    wallJumpFixedDirectionTime = 0.2;
                    if (lastCollisionDirection == Direction.RIGHT) {
                        wallJumpDirection = Direction.LEFT;
                    }
                    else if (lastCollisionDirection == Direction.LEFT) {
                        wallJumpDirection = Direction.RIGHT;
                    }
                }
            }

            velocity.x = 0;
            boolean isWallJumping = wallJumpFixedDirectionTime > 0;
            boolean wallJumpingLeft = (isWallJumping && wallJumpDirection == Direction.LEFT);
            boolean wallJumpingRight = (isWallJumping && wallJumpDirection == Direction.RIGHT);
            if ((input.getKeyState(KeyEvent.VK_LEFT) & ! wallJumpingRight) || wallJumpingLeft) {
                velocity.x -= isWallJumping ? 30 : 20;
                lastHorizontalDirection = Direction.LEFT;
            }
            if ((input.getKeyState(KeyEvent.VK_RIGHT) & !wallJumpingLeft) || wallJumpingRight) {
                velocity.x += isWallJumping ? 30 : 20;
                lastHorizontalDirection = Direction.RIGHT;
            }
            wallJumpFixedDirectionTime -= timeDelta;

            if (input.getKeyState(KeyEvent.VK_DOWN)) {
                standsOnSemisolid = false;
            }
            else {
                standsOnSemisolid = true;
            }

            if (input.getKeyState(KeyEvent.VK_SHIFT) && hasDashCharge && !hitWallRecently) {
                boolean madeDash = false;
                if (lastHorizontalDirection == Direction.LEFT && !input.getKeyState(KeyEvent.VK_RIGHT)) {
                    dashDirection = Direction.LEFT;
                    madeDash = true;
                }
                if (lastHorizontalDirection == Direction.RIGHT && !input.getKeyState(KeyEvent.VK_LEFT)) {
                    dashDirection = Direction.RIGHT;
                    madeDash = true;
                }
                if (madeDash) {
                    hasDashCharge = false;
                    canDashCharge = false;
                    dashTime = maxDashTime;
                    dashMovement = 0;
                    noGravity = true;
                }
            }
            //Hit wall, stop dashing
            if (hitWallRecently) {
                dashTime = 0;
            }
            if (dashTime > 0) {
                noGravity = true;
            }
            else {
                noGravity = false;
            }
            if (dashTime > 0) {
                if (dashDirection == Direction.LEFT) {
                    velocity.x = -dashSpeed;
                    velocity.y = 0;
                }
                if (dashDirection == Direction.RIGHT) {
                    velocity.x = dashSpeed;
                    velocity.y = 0;
                }
                dashMovement += dashSpeed * timeDelta;
                if (dashMovement >= 1) {
                    dashMovement -= 1;
                    Game.currentLevel.addEntity(new DashParticle(new Vector2(this.position.x, this.position.y+ 0.5)));
                }
                standsOnSemisolid = false;
            }
            else if (!hasDashCharge && (onGroundRecently || hitWallRecently)) {
                canDashCharge = true;
            }
            if (dashTime < -dashCooldown && canDashCharge) {
                hasDashCharge = true;
            }
            dashTime -= timeDelta;
        }
        else {
            timeDead += timeDelta;
            if(timeDead > 1) {
                dead = false;
                position = new Vector2(5.5,5.5);
                gravity = new Vector2(0, 60);
                timeDead = 0;
            }
        }
        super.process(timeDelta, input);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        Vector2 position = Vector2.add(this.position, new Vector2(0.5, 0.5));
        boolean half_x = position.x % 1 > 0.75 || position.x % 1 < 0.25;
        boolean half_y = position.y % 1 > 0.75 || position.y % 1 < 0.25;

        double round_x = Math.round(position.x);
        double round_y = Math.round(position.y);

        if (dead) {
            if (timeDead < 0.15)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.3)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▓', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.45)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▒', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.6)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('░', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        else {
            if (!half_x && !half_y) {
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else if (half_x && !half_y) {
                screen.setCharacterWithDepth((int) round_x - 1, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▐', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▌', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else if (!half_x) {
                screen.setCharacterWithDepth((int) position.x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▄', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) position.x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▀', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else {
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▗', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▖', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▝', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▘', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            }
        }
    }

    @Override
    public void kill() {
        dashTime = 0;
        hasDashCharge = true;
        super.kill();
    }
}

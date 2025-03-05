package normallynormal.World.Entity;

import normallynormal.Input.Input;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Particle.DashParticle;
import normallynormal.World.Entity.Particle.ExtraJumpParticle;
import normallynormal.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Settings.Keybinds;

public class Player extends Entity implements GlowingEntity {
    private static final double JUMP_COYOTE_TIME = 0.1;
    private static final double WALL_JUMP_COYOTE_TIME = 0.2;
    private static final double WALL_SLIDE_BUFFER = 0.05;
    private static final double DASH_COOLDOWN = 0.4;
    private static final double DASH_SPEED = 60;
    private static final double MAX_DASH_TIME = 0.25;
    private static final int RUN_SPEED = 20;
    private static final int WALL_JUMP_SPEED = 30;
    private static final int DOUBLE_JUMP_SPEED = 20;
    private static final double DOUBLE_JUMP_WAITING_PERIOD = 0.25;

    double timeDead;
    double jumpBuffer = 0;
    boolean jumpAllowed = true;

    Direction lastHorizontalDirection = Direction.NONE;

    boolean hasDashCharge = true;
    boolean canDashCharge = true;
    double dashTime = 0;
    Direction dashDirection = Direction.NONE;

    int lastDashXPos = Integer.MIN_VALUE;

    int extraJumps = 0;
    int maxExtraJumps = 1;
    boolean jumpKeyReleasedInAir = false;
    boolean stopVerticalVelocityAllowed = true;

    Direction wallJumpDirection = Direction.NONE;
    double wallJumpFixedDirectionTime = 0;

    Vector2 spawnPosition = new Vector2(0, 0);

    public Player() {
        timeDead = 0;
        velocity = new Vector2(0,0);
        position = new Vector2(5.5, 5.5);
        depth = 10;
        setGravityMagnitude(60);
        collisionBox = new AABB(position.x - 0.49, position.y - 0.49, 0.98, 0.98);
        noGravity = false;
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (!dead) {
            handleWallSliding(timeDelta, input);
            handleJumping(timeDelta, input);
            handleDoubleJumping(timeDelta, input);
            handleLeftRightMovement(timeDelta, input);
            handleSemisolid(timeDelta, input);
            handleDashing(timeDelta, input);
        }
        else {
            timeDead += timeDelta;
            if(timeDead > 1) {
                dead = false;
                position = new Vector2(spawnPosition.x,spawnPosition.y);
                setGravityMagnitude(60);
                timeDead = 0;
            }
        }
        super.process(timeDelta, input);
    }

    private void handleDashing(double timeDelta, Input input) {
        if (input.getKeyState(Keybinds.player_dash) && !hitWallRecently(0)) {
            boolean madeDash = false;
            if (hasDashCharge) {
                if (lastHorizontalDirection == Direction.LEFT && !input.getKeyState(Keybinds.player_right)) {
                    dashDirection = Direction.LEFT;
                    madeDash = true;
                }
                if (lastHorizontalDirection == Direction.RIGHT && !input.getKeyState(Keybinds.player_left)) {
                    dashDirection = Direction.RIGHT;
                    madeDash = true;
                }
            }
            if (madeDash) {
                hasDashCharge = false;
                canDashCharge = false;
                dashTime = MAX_DASH_TIME;
                lastDashXPos = Integer.MIN_VALUE;
            }
        }
        if (hitWallRecently(0)) {
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
                instantVelocity.x = -DASH_SPEED;
                velocity.y = 0;
            }
            if (dashDirection == Direction.RIGHT) {
                instantVelocity.x = DASH_SPEED;
                velocity.y = 0;
            }
            if (lastDashXPos != Math.floor(position.x)) {
                if (lastDashXPos != Integer.MIN_VALUE) {
                    int particlesToCreate = (int) Math.floor(position.x) - lastDashXPos;
                    for (int i = 0; Math.abs(i) <= Math.abs(particlesToCreate); i = particlesToCreate > 0 ? i + 1 : i - 1) {
                        Game.currentLevel.addEntity(new DashParticle(new Vector2(this.position.x + i + 0.5, this.position.y + 0.5)));
                    }
                }
                lastDashXPos = (int)Math.floor(position.x);
            }
            standsOnSemisolid = false;
        }
        else if (!hasDashCharge && (onGroundRecently(0) || hitWallRecently(0))) {
            canDashCharge = true;
        }
        if (dashTime < -DASH_COOLDOWN && canDashCharge) {
            hasDashCharge = true;
        }
        dashTime -= timeDelta;
    }

    private void handleSemisolid(double timeDelta, Input input) {
        standsOnSemisolid = !input.getKeyState(Keybinds.player_down);
    }

    private void handleLeftRightMovement(double timeDelta, Input input) {
        boolean isWallJumping = wallJumpFixedDirectionTime > 0;
        wallJumpFixedDirectionTime -= timeDelta;
        boolean wallJumpingLeft = (isWallJumping && wallJumpDirection == Direction.LEFT);
        boolean wallJumpingRight = (isWallJumping && wallJumpDirection == Direction.RIGHT);
        if ((input.getKeyState(Keybinds.player_left) & ! wallJumpingRight) || wallJumpingLeft) {
            instantVelocity.x += isWallJumping ? -WALL_JUMP_SPEED : -RUN_SPEED;
            lastHorizontalDirection = Direction.LEFT;
        }
        if ((input.getKeyState(Keybinds.player_right) & !wallJumpingLeft) || wallJumpingRight) {
            instantVelocity.x += isWallJumping ? WALL_JUMP_SPEED : RUN_SPEED;
            lastHorizontalDirection = Direction.RIGHT;
        }
    }

    private void handleDoubleJumping(double timeDelta, Input input) {
        if (!onGroundRecently(DOUBLE_JUMP_WAITING_PERIOD) && extraJumps > 0 && input.getKeyState(Keybinds.player_jump) && !hitWallRecently(WALL_JUMP_COYOTE_TIME) && jumpKeyReleasedInAir && !isDashing()) {
            extraJumps--;
            jumpKeyReleasedInAir = false;
            velocity.y = isGravityDownward() ? Math.min(velocity.y, -DOUBLE_JUMP_SPEED) : Math.max(velocity.y, DOUBLE_JUMP_SPEED);
            stopVerticalVelocityAllowed = false;
            hasDashCharge = false;
            Game.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.LEFT));
            Game.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.RIGHT));
        }
    }

    private void handleWallSliding(double timeDelta, Input input) {
        if (hitWallRecently(WALL_SLIDE_BUFFER)) {
            if (velocity.y > 5 && isGravityDownward()) {
                velocity.y = 5;
            }
            else if (velocity.y < -5 && !isGravityDownward()) {
                velocity.y = -5;
            }
        }
    }

    private void handleJumping(double timeDelta, Input input) {
        if (onGroundRecently(0)) {
            jumpAllowed = true;
        }
        if (input.getKeyState(Keybinds.player_jump)) {
            jumpBuffer = 0.05;
        }
        else {
            if (stopVerticalVelocityAllowed) {
                if (velocity.y < 0 && isGravityDownward()) {
                    velocity.y = 0;
                } else if (velocity.y > 0 && !isGravityDownward()) {
                    velocity.y = 0;
                }
            }
            stopVerticalVelocityAllowed = false;
            jumpKeyReleasedInAir = true;
        }
        if (jumpBuffer > 0) {
            jumpBuffer -= timeDelta;
            boolean rightKey = input.getKeyState(Keybinds.player_right);
            boolean leftKey = input.getKeyState(Keybinds.player_left);
            Direction possibleWallJumpDirection = rightKey & !leftKey ? Direction.RIGHT : Direction.NONE;
            possibleWallJumpDirection = leftKey & !rightKey ? Direction.LEFT : possibleWallJumpDirection;
            if (onGroundRecently(JUMP_COYOTE_TIME) && jumpAllowed) {
                jumpAllowed = false;
                velocity.y = isGravityDownward() ? -30 : 30;
                jumpKeyReleasedInAir = false;
                stopVerticalVelocityAllowed = true;
                canDashCharge = true;
            }
            else if (hitWallRecently(WALL_JUMP_COYOTE_TIME) && possibleWallJumpDirection == lastCollisionDirection.opposite()) {
                velocity.y = isGravityDownward() ? -15 : 15;
                wallJumpFixedDirectionTime = 0.2;
                jumpKeyReleasedInAir = false;
                if (lastCollisionDirection == Direction.RIGHT) {
                    wallJumpDirection = Direction.LEFT;
                }
                else if (lastCollisionDirection == Direction.LEFT) {
                    wallJumpDirection = Direction.RIGHT;
                }
            }
        }
    }

    public boolean isDashing() {
        return dashTime > 0;
    }

    @Override
    protected void hitGround() {
        if (!bounce)
            extraJumps = maxExtraJumps;
        jumpKeyReleasedInAir = false;
        super.hitGround();
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
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            else if (timeDead < 0.3)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▓', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            else if (timeDead < 0.45)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▒', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            else if (timeDead < 0.6)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('░', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
        }
        else {
            if (!half_x && !half_y) {
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            } else if (half_x && !half_y) {
                screen.setCharacterWithDepth((int) round_x - 1, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▐', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
                screen.setCharacterWithDepth((int) round_x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▌', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            } else if (!half_x) {
                screen.setCharacterWithDepth((int) position.x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▄', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
                screen.setCharacterWithDepth((int) position.x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▀', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            } else {
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▗', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
                screen.setCharacterWithDepth((int) round_x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▖', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▝', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
                screen.setCharacterWithDepth((int) round_x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▘', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            }
        }
    }

    @Override
    public void kill() {
        dashTime = 0;
        hasDashCharge = true;
        extraJumps = maxExtraJumps;
        setGravityDownward();
        super.kill();
    }

    public double glowRadius() {
        return 9 * (1-timeDead) + 1;
    }

    public void setSpawnPosition(Vector2 spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    @Override
    public void setGravityDownward() {
        resetMovementForGravity();
        super.setGravityDownward();
    }

    @Override
    public void setGravityUpward() {
        resetMovementForGravity();
        super.setGravityUpward();
    }

    private void resetMovementForGravity() {
        stopVerticalVelocityAllowed = false;
    }

    @Override
    public void bounce() {
        stopVerticalVelocityAllowed = false;
        super.bounce();
    }
}

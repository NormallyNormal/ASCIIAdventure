package normallynormal.World.Entity;

import normallynormal.GameManager;
import normallynormal.Input.Input;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.Sound.AudioPlayer;
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
    private static final double JUMP_BUFFER = 0.05;
    private static final double WALL_JUMP_COYOTE_TIME = 0.2;
    private static final double WALL_SLIDE_BUFFER = 0.05;
    private static final double WALL_JUMP_DURATION = 0.2;
    private static final double DASH_COOLDOWN = 0.4;
    private static final double DASH_SPEED = 60;
    private static final double MAX_DASH_TIME = 0.25;
    private static final int RUN_SPEED = 20;
    private static final int WALL_JUMP_HORIZONTAL_SPEED = 30;
    private static final int WALL_JUMP_VERTICAL_SPEED = 15;
    private static final int DOUBLE_JUMP_SPEED = 20;
    private static final double DOUBLE_JUMP_WAITING_PERIOD = 0.25;
    private static final int DEFAULT_GRAVITY = 60;
    private static final int JUMP_SPEED = 30;

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
        setGravityMagnitude(DEFAULT_GRAVITY);
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
                //Return to spawnpoint
                position = new Vector2(spawnPosition.x,spawnPosition.y);
                //Ensure gravity is
                setGravityMagnitude(DEFAULT_GRAVITY);
                timeDead = 0;
            }
        }
        super.process(timeDelta, input);
    }

    private void handleDashing(double timeDelta, Input input) {
        //Can dash until a wall is hit
        if (hitWallRecently(0)) {
            dashTime = 0;
        }
        else if (input.getKeyState(Keybinds.player_dash)) {
            boolean madeDash = false;
            //If the dash cooldown has expired, and we have hit a surface, try to dash in the last input direction.
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
            //If we could dash, set our dash time to max and get rid of the dash charge.
            if (madeDash) {
                hasDashCharge = false;
                canDashCharge = false;
                dashTime = MAX_DASH_TIME;
                lastDashXPos = Integer.MIN_VALUE;
            }
        }
        //While dashing, disable gravity for this player
        if (dashTime > 0) {
            noGravity = true;
        }
        else {
            noGravity = false;
        }

        //Perform dash
        if (dashTime > 0) {
            //Change velocity according to dash direction.
            if (dashDirection == Direction.LEFT) {
                instantVelocity.x = -DASH_SPEED;
                velocity.y = 0;
            }
            if (dashDirection == Direction.RIGHT) {
                instantVelocity.x = DASH_SPEED;
                velocity.y = 0;
            }
            //Create a dash particle for each tile the player moves
            if (lastDashXPos != Math.floor(position.x)) {
                if (lastDashXPos != Integer.MIN_VALUE) {
                    int particlesToCreate = (int) Math.floor(position.x) - lastDashXPos;
                    for (int i = 0; Math.abs(i) <= Math.abs(particlesToCreate); i = particlesToCreate > 0 ? i + 1 : i - 1) {
                        GameManager.currentLevel.addEntity(new DashParticle(new Vector2(this.position.x + i + 0.5, this.position.y + 0.5)));
                    }
                }
                lastDashXPos = (int)Math.floor(position.x);
            }
        }
        //Dash refresh: We can start to cooldown when we've hit a solid.
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
            instantVelocity.x += isWallJumping ? -WALL_JUMP_HORIZONTAL_SPEED : -RUN_SPEED;
            lastHorizontalDirection = Direction.LEFT;
        }
        if ((input.getKeyState(Keybinds.player_right) & !wallJumpingLeft) || wallJumpingRight) {
            instantVelocity.x += isWallJumping ? WALL_JUMP_HORIZONTAL_SPEED : RUN_SPEED;
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
            GameManager.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.LEFT));
            GameManager.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.RIGHT));
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
            jumpBuffer = JUMP_BUFFER;
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
            if (onGroundRecently(JUMP_COYOTE_TIME) && jumpAllowed && (!bounceRecently(JUMP_COYOTE_TIME) || Math.abs(velocity.y) < JUMP_SPEED)) {
                jumpAllowed = false;
                velocity.y = isGravityDownward() ? -JUMP_SPEED : JUMP_SPEED;
                jumpKeyReleasedInAir = false;
                stopVerticalVelocityAllowed = true;
                canDashCharge = true;
                AudioPlayer.play("player.jump");
            }
            else if (hitWallRecently(WALL_JUMP_COYOTE_TIME) && possibleWallJumpDirection == lastCollisionDirection.opposite()) {
                velocity.y = isGravityDownward() ? -WALL_JUMP_VERTICAL_SPEED : WALL_JUMP_VERTICAL_SPEED;
                wallJumpFixedDirectionTime = WALL_JUMP_DURATION;
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

    private static final TextCharacter CHAR_FULL = new TextCharacter('█', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_DARK_SHADE = new TextCharacter('▓', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_MEDIUM_SHADE = new TextCharacter('▒', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_LIGHT_SHADE = new TextCharacter('░', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_RIGHT_HALF = new TextCharacter('▐', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_LEFT_HALF = new TextCharacter('▌', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_LOWER_HALF = new TextCharacter('▄', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_UPPER_HALF = new TextCharacter('▀', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_BOTTOM_RIGHT = new TextCharacter('▗', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_BOTTOM_LEFT = new TextCharacter('▖', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_TOP_RIGHT = new TextCharacter('▝', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    private static final TextCharacter CHAR_TOP_LEFT = new TextCharacter('▘', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);

    Vector2 render_position = position.deepCopy();
    boolean render_dead = dead;
    double render_timeDead = timeDead;
    int render_depth = depth;

    public void copyForRender() {
        render_position = position.deepCopy(render_position);
        render_dead = dead;
        render_timeDead = timeDead;
        render_depth = depth;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        Vector2 render_position = Vector2.add(this.render_position, new Vector2(0.5, 0.5));
        boolean half_x = render_position.x % 1 > 0.75 || render_position.x % 1 < 0.25;
        boolean half_y = render_position.y % 1 > 0.75 || render_position.y % 1 < 0.25;

        double round_x = Math.round(render_position.x);
        double round_y = Math.round(render_position.y);

        if (render_dead) {
            if (render_timeDead < 0.15)
                screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_FULL);
            else if (render_timeDead < 0.3)
                screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_DARK_SHADE);
            else if (render_timeDead < 0.45)
                screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_MEDIUM_SHADE);
            else if (render_timeDead < 0.6)
                screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_LIGHT_SHADE);
        } else {
            if (!half_x && !half_y) {
                screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_FULL);
            } else if (half_x && !half_y) {
                screen.setCharacterWithDepth((int) round_x - 1, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_RIGHT_HALF);
                screen.setCharacterWithDepth((int) round_x, (int) render_position.y, xOffset, yOffset, render_depth, CHAR_LEFT_HALF);
            } else if (!half_x) {
                screen.setCharacterWithDepth((int) render_position.x, (int) round_y - 1, xOffset, yOffset, render_depth, CHAR_LOWER_HALF);
                screen.setCharacterWithDepth((int) render_position.x, (int) round_y, xOffset, yOffset, render_depth, CHAR_UPPER_HALF);
            } else {
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y - 1, xOffset, yOffset, render_depth, CHAR_BOTTOM_RIGHT);
                screen.setCharacterWithDepth((int) round_x, (int) round_y - 1, xOffset, yOffset, render_depth, CHAR_BOTTOM_LEFT);
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y, xOffset, yOffset, render_depth, CHAR_TOP_RIGHT);
                screen.setCharacterWithDepth((int) round_x, (int) round_y, xOffset, yOffset, render_depth, CHAR_TOP_LEFT);
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
        jumpAllowed = false;
        stopVerticalVelocityAllowed = false;
        super.bounce();
    }

    public void refreshDash() {
        hasDashCharge = true;
    }

    public void refreshDoubleJump() {
        extraJumps = maxExtraJumps;
    }
}

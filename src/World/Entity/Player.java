package World.Entity;

import Input.Input;
import Render.DepthScreen;
import Render.TransparentColor;
import World.Entity.Particle.DashParticle;
import World.Entity.Particle.ExtraJumpParticle;
import World.Entity.Particle.SlamParticle;
import World.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Vector2;
import Math.AABB;
import Math.Direction;
import Settings.Keybinds;

public class Player extends Entity implements GlowingEntity {
    double timeDead;
    double jumpBuffer = 0;

    Direction lastHorizontalDirection = Direction.NONE;

    boolean hasDashCharge = true;
    boolean canDashCharge = true;
    double dashTime = 0;
    Direction dashDirection = Direction.NONE;
    double maxDashTime = 0.2;
    double dashCooldown = 0.4;
    int lastDashXPos = Integer.MIN_VALUE;
    double dashSpeed = 60;

    int extraJumps = 0;
    int maxExtraJumps = 1;
    boolean jumpKeyReleasedInAir = false;
    boolean stopVerticalVelocityAllowed = true;

    Direction wallJumpDirection = Direction.NONE;
    double wallJumpFixedDirectionTime = 0;

    Vector2 spawnPosition = new Vector2(0, 0);

    boolean slamming = false;
    double slamMovement = 0;

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
            boolean onGroundRecently = timeSinceOnGround < 0.1;
            if (onGroundRecently) {
                extraJumps = maxExtraJumps;
                stopVerticalVelocityAllowed = true;
                jumpKeyReleasedInAir = false;
                if (slamming) {
                    Game.currentLevel.addEntity(new SlamParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.LEFT, true));
                    Game.currentLevel.addEntity(new SlamParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.RIGHT, true));
                }
                slamming = false;
            }
            boolean hitWallRecently = timeSinceWallHit < 0.05;
            boolean hitWallSomewhatRecently = timeSinceWallHit < 0.2;
            if (input.getKeyState(Keybinds.player_jump)) {
                jumpBuffer = 0.05;
            }
            if (!input.getKeyState(Keybinds.player_jump) && stopVerticalVelocityAllowed) {
                if (velocity.y < 0) {
                    velocity.y = 0;
                }
                jumpKeyReleasedInAir = true;
            }
            //Wall slide
            if (timeSinceWallHit < 0.05) {
                if (velocity.y > 5) {
                    velocity.y = 5;
                }
            }
            if (jumpBuffer > 0) {
                jumpBuffer -= timeDelta;
                boolean rightKey = input.getKeyState(Keybinds.player_right);
                boolean leftKey = input.getKeyState(Keybinds.player_left);
                Direction possibleWallJumpDirection = rightKey & !leftKey ? Direction.RIGHT : Direction.NONE;
                possibleWallJumpDirection = leftKey & !rightKey ? Direction.LEFT : possibleWallJumpDirection;
                if (onGroundRecently) {
                    velocity.y = -30;
                    timeSinceOnGround = Double.POSITIVE_INFINITY;
                    onGroundRecently = false;
                    canDashCharge = true;
                }
                else if (hitWallSomewhatRecently && possibleWallJumpDirection == lastCollisionDirection.opposite()) {
                    velocity.y = -15;
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
            if (!onGroundRecently && extraJumps > 0 && velocity.y >= 0 && input.getKeyState(Keybinds.player_jump) && !hitWallSomewhatRecently && jumpKeyReleasedInAir && dashTime <= 0) {
                extraJumps--;
                velocity.y = -20;
                stopVerticalVelocityAllowed = false;
                hasDashCharge = false;
                Game.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.LEFT));
                Game.currentLevel.addEntity(new ExtraJumpParticle(new Vector2(this.position.x + 0.5, this.position.y + 0.5), Direction.RIGHT));
            }

            velocity.x = 0;
            boolean isWallJumping = wallJumpFixedDirectionTime > 0;
            boolean wallJumpingLeft = (isWallJumping && wallJumpDirection == Direction.LEFT);
            boolean wallJumpingRight = (isWallJumping && wallJumpDirection == Direction.RIGHT);
            if ((input.getKeyState(Keybinds.player_left) & ! wallJumpingRight) || wallJumpingLeft) {
                velocity.x -= isWallJumping ? 30 : 20;
                lastHorizontalDirection = Direction.LEFT;
            }
            if ((input.getKeyState(Keybinds.player_right) & !wallJumpingLeft) || wallJumpingRight) {
                velocity.x += isWallJumping ? 30 : 20;
                lastHorizontalDirection = Direction.RIGHT;
            }
            wallJumpFixedDirectionTime -= timeDelta;

            standsOnSemisolid = !input.getKeyState(Keybinds.player_down);

            if (input.getKeyState(Keybinds.player_dash) && !hitWallRecently) {
                boolean madeDash = false;
                boolean tryingSlam = input.getKeyState(Keybinds.player_down);
                if (hasDashCharge) {
                    if (lastHorizontalDirection == Direction.LEFT && !input.getKeyState(Keybinds.player_right) && !tryingSlam) {
                        dashDirection = Direction.LEFT;
                        madeDash = true;
                    }
                    if (lastHorizontalDirection == Direction.RIGHT && !input.getKeyState(Keybinds.player_left) && !tryingSlam) {
                        dashDirection = Direction.RIGHT;
                        madeDash = true;
                    }
                }
                if (tryingSlam && dashTime <= 0 && !onGroundRecently && !input.getKeyState(Keybinds.player_jump)) {
                    velocity.y = 100;
                    if (!slamming) {
                        slamMovement = position.y;
                    }
                    slamming = true;
                    lastHorizontalDirection = Direction.DOWN;
                    extraJumps = 0;
                }
                if (madeDash) {
                    hasDashCharge = false;
                    canDashCharge = false;
                    dashTime = maxDashTime;
                    lastDashXPos = Integer.MIN_VALUE;
                }
            }

            if (slamming) {
                if(position.y > slamMovement) {
                    Game.currentLevel.addEntity(new SlamParticle(new Vector2(this.position.x + 0.5, slamMovement + 0.5), Direction.LEFT, false));
                    Game.currentLevel.addEntity(new SlamParticle(new Vector2(this.position.x + 0.5, slamMovement + 0.5), Direction.RIGHT, false));
                    slamMovement += 1;
                }
            }

            //Hit wall, stop dashing
            if (hitWallRecently) {
                dashTime = 0;
                slamming = false;
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
                if (lastDashXPos != Math.floor(position.x)) {
                    lastDashXPos = (int)Math.floor(position.x);
                    Game.currentLevel.addEntity(new DashParticle(new Vector2(this.position.x, this.position.y + 0.5)));
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
                position = new Vector2(spawnPosition.x,spawnPosition.y);
                setGravityMagnitude(60);
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
        slamming = false;
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
        stopVerticalVelocityAllowed = false;
        super.setGravityDownward();
    }

    @Override
    public void setGravityUpward() {
        stopVerticalVelocityAllowed = false;
        super.setGravityUpward();
    }
}

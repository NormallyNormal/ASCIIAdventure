package normallynormal.World.Entity;

import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.UI.Element.DialogueBox;
import normallynormal.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public class NPC extends Entity {
    DialogueBox dialogueBox = new DialogueBox(new String[]{"Hello!", "Hello again!", "Look at all these <RED>COLORS <DEFAULT>and <BLUE>COLORS", "And look how this very very very very very long text is so very very very very very long that it has to wrap around to the next line. Isn't that interesting?"});
    boolean dbEnabled = false;

    public NPC(int x, int y) {
        this.position.x = x;
        this.position.y = y;
        this.collisionBox = new AABB(x, y, 1, 1);
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (Game.currentLevel.playerNear(this.position, 5)) {
            if (!dbEnabled) {
                dialogueBox.reset();
            }
            dbEnabled = true;
            dialogueBox.process(timeDelta, input);
        }
        else {
            dbEnabled = false;
        }
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        if (System.currentTimeMillis() % 1000 < 500) {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
        }
        else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▇', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
        }

        if(dbEnabled) {
            dialogueBox.render(screen);
        }
    }
}
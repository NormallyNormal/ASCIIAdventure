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
    DialogueBox dialogueBox = new DialogueBox(new String[]{"Hello!", "This is just placeholder text for the demo level to show how NPCs work.", "Also, text can have special <RED>COLORS<DEFAULT> and <BLUE_BRIGHT>COLORS<DEFAULT>.", "That way, an NPC could emphasize certain important things...", "Did you know you can press up again after jumping to <GREEN>double jump<DEFAULT>?", "You can also press shift to <BLUE_BRIGHT>dash<DEFAULT>.", "That's all I have to say. If you keep talking to me, I'll just repeat this line again."});
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
        if (Game.gameTime() % 1000 < 500) {
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
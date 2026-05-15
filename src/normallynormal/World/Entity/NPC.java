package normallynormal.World.Entity;

import normallynormal.GameManager;
import normallynormal.Input.InputHandler;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.UI.Element.DialogueBox;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.UI.LanguageManager;

public class NPC extends Entity {
    DialogueBox dialogueBox = new DialogueBox(new String[]{LanguageManager.get("dialogue.demo_npc.1"), LanguageManager.get("dialogue.demo_npc.2"), LanguageManager.get("dialogue.demo_npc.3"), LanguageManager.get("dialogue.demo_npc.4"), LanguageManager.get("dialogue.demo_npc.5"), LanguageManager.get("dialogue.demo_npc.6"), LanguageManager.get("dialogue.demo_npc.7")});
    boolean dbEnabled = false;

    public NPC(int x, int y) {
        this.position.x = x;
        this.position.y = y;
        this.collisionBox = new AABB(x, y, 1, 1);
    }

    @Override
    public void process(double timeDelta, InputHandler input) {
        if (GameManager.currentLevel.playerNear(this.position, 5)) {
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

    private record State(double posX, double posY, int depth, boolean onScreen,
                         boolean dbEnabled, DialogueBox dialogueBox) implements RenderState {
        @Override
        public void render(DepthScreen screen, int xOffset, int yOffset) {
            if (GameManager.gameTime() % 1000 < 500) {
                screen.setCharacterWithDepth((int) posX, (int) posY, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            }
            else {
                screen.setCharacterWithDepth((int) posX, (int) posY, xOffset, yOffset, depth, new TextCharacter('▇', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT));
            }

            if (dbEnabled) {
                dialogueBox.render(screen);
            }
        }
    }

    @Override
    public void copyForRender() {
        renderState = new State(position.x, position.y, depth, isOnScreen(), dbEnabled, dialogueBox);
    }
}

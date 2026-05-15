package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.GameManager;
import normallynormal.Input.BoundInput;
import normallynormal.Input.ControllerInput;
import normallynormal.Input.Input;
import normallynormal.Input.InputHandler;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.UI.LanguageManager;

public class ControllerChoice extends UIComponent {
    String label;
    int labelWidth = 20;
    int choiceWidth = 22;

    private final Input boundInput;
    private boolean capturing = false;
    private int skipFrames = 0;
    private InputHandler lastInputHandler;

    public ControllerChoice(int x, int y, String label, Input boundInput) {
        pos = new Vector2(x, y);
        this.label = label;
        this.boundInput = boundInput;
    }

    @Override
    public void onDeselected() {
        if (capturing && lastInputHandler != null) {
            capturing = false;
            lastInputHandler.stopControllerCapture();
            lastInputHandler.setInputState(Input.UI_SELECT, false);
        }
    }

    @Override
    public void process(double timeDelta, InputHandler input) {
        lastInputHandler = input;
        BoundInput bound = input.getBoundInput(boundInput);

        if (!highlighted) {
            if (capturing) {
                capturing = false;
                input.stopControllerCapture();
                input.setInputState(Input.UI_SELECT, false);
            }
            return;
        }

        if (capturing) {
            if (skipFrames > 0) {
                skipFrames--;
                input.pollCapturedController();
                return;
            }
            ControllerInput ci = input.pollCapturedController();
            if (ci != null) {
                if (bound != null) bound.setSingleControllerBind(ci);
                capturing = false;
                input.stopControllerCapture();
                input.setInputState(Input.UI_SELECT, false);
            }
        } else {
            if (input.wasInputJustPressed(Input.UI_SELECT)) {
                capturing = true;
                skipFrames = 1;
                input.startControllerCapture();
            }
        }
    }

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        TextColor backgroundColor = highlighted ? highlightColor : TransparentColor.TRANSPARENT;

        String displayLabel = label.substring(0, Math.min(labelWidth, label.length()));
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x, (int)offset.y, getZOrder(), displayLabel, textColor, backgroundColor);

        BoundInput bound = GameManager.input.getBoundInput(boundInput);
        String bindText = capturing
                ? LanguageManager.get("settings.input_settings.controller.awaiting")
                : (bound != null ? bound.getControllerBindText() : LanguageManager.get("settings.input_settings.controller.unbound"));
        String clamped = bindText.substring(0, Math.min(choiceWidth - 2, bindText.length()));

        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth, (int)offset.y, getZOrder(),
                new TextCharacter('[', textColor, backgroundColor));
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + 1, (int)offset.y, getZOrder(), clamped, textColor, backgroundColor);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + choiceWidth - 1, (int)offset.y, getZOrder(),
                new TextCharacter(']', textColor, backgroundColor));
    }
}

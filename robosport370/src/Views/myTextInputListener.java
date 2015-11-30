package Views;

import com.badlogic.gdx.Input.TextInputListener;

public class myTextInputListener implements TextInputListener {
    private String input;

    @Override
    public void input(String text) {
        input = text;
    }

    @Override
    public void canceled() {
        input = "";
    }

    public String getInput() {
        return input;
    }
}

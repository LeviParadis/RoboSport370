package Views;

import com.badlogic.gdx.Input.TextInputListener;

public class MyTextInputListener implements TextInputListener {
		private String input;
	
		@Override
		public void input (String text) {
			input = text;
	    }

	    @Override
	    public void canceled () {
	    }
	    
	    public String getInput() {
	    	return input;
	    }
	}


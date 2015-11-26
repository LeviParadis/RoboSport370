package Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import Controllers.EditRobotController;
import Models.Robot;

public class EditRobotView extends AddRobotView{

    public EditRobotView(EditRobotController controller, Robot robot) {
        super(controller);
        this.titleLabel.setText("Editing " + robot.getName());
    }

}

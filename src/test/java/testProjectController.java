import com.qut.cab302_a1.ProjectController;
import javafx.scene.control.Label;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class testProjectController {
    ProjectController projectController = new ProjectController();

    @Test
    public void testCalculateTextSize(){
        int expectedSize = 46;
        String exampleString = "Example";
        double result = projectController.calculateTextSize(exampleString);
        Assertions.assertEquals(expectedSize, (int)result);
    }

//    @Test
//    public void calcNormalSpacing(){
//        Label exampleLabel = new Label("Example");
//        int maxSize = 400;
//        double textSize = projectController.calculateTextSize(exampleLabel.getText());
//        int result = projectController.calcSpacing(textSize, maxSize, exampleLabel);
//        int expected = 400 - 46;
//
//        Assertions.assertEquals(expected, result);
//    }
//
//    @Test
//    public void calc0Spacing(){
//
//    }
//
//    @Test
//    public void calcMaxSpacing(){
//
//    }


}

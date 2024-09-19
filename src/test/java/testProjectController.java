import com.qut.cab302_a1.ProjectController;
import javafx.scene.control.Label;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class testProjectController {
    ProjectController projectController = new ProjectController();
    public String exampleString = "Example";
    public int MAX_RANGE = 270;
    public int totalProgress = 10;

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

    @Test
    public void testCalculateLowProgressBar(){
        int currentProject = 2;
        int expectedProgress = (int)(0.2 * MAX_RANGE);

        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);

    }

    @Test
    public void testCalculateHighProgressBar(){
        int currentProject = 9;
        int expectedProgress = (int)(0.9 * MAX_RANGE);
        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);
    }

    @Test
    public void testCalculate0(){
        int currentProject = 0;
        int expectedProgress = 0;
        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);
    }

    @Test
    public void testCalculateIncorrectVal(){
        int currentProject = 11;
        int expectedProgress = 10;
        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);
    }

    @Test
    public void testCalculateLowProgress(){
        int currentProject = 2;
        int totalLowProgress = 4;
        int expectedProgress = (int)(0.5 * MAX_RANGE);
        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);
    }

    @Test
    public void testCalculateHighProgress(){
        int currentProject = 66;
        int totalHighProgress = 100;
        int expectedProgress = (int)(0.66 * MAX_RANGE);
        int result = projectController.calculateProgress(MAX_RANGE, totalProgress, currentProject);
        Assertions.assertEquals(expectedProgress, result);
    }
}

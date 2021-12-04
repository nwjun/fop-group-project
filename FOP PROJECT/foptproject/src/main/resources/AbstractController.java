
import com.fop.foptproject.controller.profileController;
import javafx.stage.Stage;

public abstract class AbstractController{
    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
}

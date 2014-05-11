package freewind.colablog.spring;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Lazy
@Configuration
public class ScreensConfiguration {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Bean
    @Scope("prototype")
    public AutowireFXMLDialog startWindow() {
        AutowireFXMLDialog dialog = loadFxml("/freewind/colablog/dialogs/start/start.fxml");
        dialog.setWidth(400);
        dialog.setHeight(280);
        return dialog;
    }

    @Bean
    @Scope("prototype")
    public AutowireFXMLDialog mainDialog() {
        AutowireFXMLDialog dialog = loadFxml("/freewind/colablog/dialogs/main/main.fxml");
        fillScreen(dialog);
        return dialog;
    }

    private void fillScreen(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    private AutowireFXMLDialog loadFxml(String file) {
        return new AutowireFXMLDialog(getClass().getResource(file), primaryStage);
    }
}

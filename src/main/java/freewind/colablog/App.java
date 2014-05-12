package freewind.colablog;

import freewind.colablog.spring.ScreensConfiguration;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScreensConfiguration.class);
        context.scan(App.class.getPackage().getName());
        ScreensConfiguration screens = context.getBean(ScreensConfiguration.class);
        screens.setPrimaryStage(primaryStage);
        screens.startWindow().show();
        System.out.println(Font.getFamilies());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

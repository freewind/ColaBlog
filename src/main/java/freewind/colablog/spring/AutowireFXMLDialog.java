package freewind.colablog.spring;

import com.google.common.collect.Lists;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

public class AutowireFXMLDialog extends Stage {

    @Autowired
    private ApplicationContext context;

    private SpringController controller;

    public AutowireFXMLDialog(URL fxml, Window owner) {
        this(fxml, owner, StageStyle.DECORATED);
    }

    public AutowireFXMLDialog(URL fxml, Window owner, StageStyle style) {
        super(style);
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(fxml);
        try {
            setScene(new Scene(loader.load()));
            controller = loader.getController();
            List<SpringController> controllers = Lists.newArrayList();
            findAllControllers(controller, controllers);
            for (SpringController ctrl : controllers) {
                if (ctrl instanceof DialogController) {
                    ((DialogController) ctrl).setDialog(this);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void findAllControllers(SpringController controller, List<SpringController> controllers) {
        controllers.add(controller);
        for (Field field : controller.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(controller);
                if (value instanceof SpringController) {
                    findAllControllers((SpringController) value, controllers);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PostConstruct
    private void postConstruct() {
        List<SpringController> controllers = Lists.newArrayList();
        findAllControllers(controller, controllers);
        for (SpringController ctrl : controllers) {
            context.getAutowireCapableBeanFactory()
                    .autowireBeanProperties(ctrl, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            ctrl.postInit();
        }
    }

}

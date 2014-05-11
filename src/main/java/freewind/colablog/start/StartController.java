package freewind.colablog.start;

import freewind.colablog.App;
import freewind.colablog.config.Config;
import freewind.colablog.config.SystemConfig;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    private App app;

    @FXML
    private ListView blogDirectories;

    private Config config;
    private SystemConfig systemConfig;

    public void chooseDirectory(ActionEvent actionEvent) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose the blog root directory");
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) {
            App.currentBlogDir = selectedDirectory;
            systemConfig.addNewBlogRoot(selectedDirectory);
            config.save(systemConfig);
            app.showMainScene();
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            config = new Config();
            systemConfig = config.loadSystemConfig();
            ObservableList<String> items = blogDirectories.getItems();
            for (String dir : systemConfig.getBlogDirectories()) {
                items.add(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        blogDirectories.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String dir = (String) blogDirectories.getSelectionModel().getSelectedItem();
                App.currentBlogDir = new File(dir);
                try {
                    app.showMainScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

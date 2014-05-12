package freewind.colablog.dialogs.start;

import freewind.colablog.AppInfo;
import freewind.colablog.config.SystemConfig;
import freewind.colablog.spring.AutowireFXMLDialog;
import freewind.colablog.spring.DialogController;
import freewind.colablog.spring.ScreensConfiguration;
import freewind.colablog.spring.SpringController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class StartController implements SpringController, DialogController {

    @Autowired
    private AppInfo appInfo;

    @Autowired
    private ScreensConfiguration screens;

    @FXML
    private ListView<String> blogDirectories;

    @Autowired
    private SystemConfig systemConfig;

    private AutowireFXMLDialog autowireFXMLDialog;

    public void chooseDirectory() throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose the blog root directory");
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) {
            appInfo.setCurrentBlogDir(selectedDirectory);
            systemConfig.addNewBlogDir(selectedDirectory);
            systemConfig.save();
            switchToMainDialog();
        }
    }

    @Override
    public void postInit() {
        blogDirectories.getItems().addAll(systemConfig.getBlogDirectories());

        blogDirectories.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String dir = blogDirectories.getSelectionModel().getSelectedItem();
                appInfo.setCurrentBlogDir(new File(dir));
                switchToMainDialog();
            }
        });
    }

    private void switchToMainDialog() {
        autowireFXMLDialog.close();
        screens.mainDialog().show();
    }

    @Override
    public void setDialog(AutowireFXMLDialog autowireFXMLDialog) {
        this.autowireFXMLDialog = autowireFXMLDialog;
    }
}

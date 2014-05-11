package freewind.colablog.config;

import freewind.colablog.AppConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SystemConfig {

    @Autowired
    private transient AppConst appConst;

    private List<String> blogDirectories = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        File configFile = getSystemConfigFile();
        if (configFile.exists()) {
            String content = FileUtils.readFileToString(configFile, "UTF-8");
            SystemConfig load = new Yaml().loadAs(content, SystemConfig.class);
            BeanUtils.copyProperties(load, this);
        }
    }

    public List<String> getBlogDirectories() {
        return blogDirectories;
    }

    public void setBlogDirectories(List<String> blogDirectories) {
        this.blogDirectories = blogDirectories;
    }

    public void addNewBlogDir(File dir) {
        String path = dir.getPath();
        if (!blogDirectories.contains(path)) {
            blogDirectories.add(path);
        }
    }

    public void save() throws IOException {
        String content = new Yaml().dump(this);
        FileUtils.writeStringToFile(getSystemConfigFile(), content, "UTF-8");
    }

    private File getSystemConfigFile() {
        return new File(appConst.getConfigDir(), "system.yml");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

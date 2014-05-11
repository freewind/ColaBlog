package freewind.colablog.config;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final String configDir = System.getProperty("user.home") + "/.colablog";
    private static File systemConfigFile = new File(configDir, "system.yml");

    public SystemConfig loadSystemConfig() throws IOException {

        if (systemConfigFile.exists()) {
            String content = FileUtils.readFileToString(systemConfigFile, "UTF-8");
            return new Yaml().loadAs(content, SystemConfig.class);
        } else {
            return new SystemConfig();
        }
    }

    public void save(SystemConfig systemConfig) throws IOException {
        String content = new Yaml().dump(systemConfig);
        System.out.println(systemConfigFile);
        System.out.println(systemConfig);
        FileUtils.writeStringToFile(systemConfigFile, content, "UTF-8");
    }

}

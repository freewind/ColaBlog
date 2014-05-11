package freewind.colablog;

import org.springframework.stereotype.Component;

@Component
public class AppConst {

    private final String configDir = System.getProperty("user.home") + "/.colablog";

    public String getConfigDir() {
        return configDir;
    }
}

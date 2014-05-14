package freewind.colablog.common;

import freewind.colablog.AppInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ImageSrcFixer {

    @Autowired
    private AppInfo appInfo;

    public String fixToLocal(String html) {
        return xxx(html, (src) -> "file:" + appInfo.getBlogStructure().getRoot() + src);
    }

    public String fixToUrl(String html) {
        return xxx(html, (src) -> src);
    }

    private String xxx(String html, Function<String, String> handle) {
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.select("img");
        for (Element img : imgs) {
            String src = img.attr("src");
            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                img.attr("src", handle.apply(src));
            }
        }
        return doc.toString();
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
}

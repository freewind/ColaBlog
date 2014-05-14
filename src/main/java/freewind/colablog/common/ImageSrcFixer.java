package freewind.colablog.common;

import freewind.colablog.AppInfo;
import org.apache.commons.lang3.StringUtils;
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
        return resolveImageSrc(html, (src) -> "file:" + appInfo.getBlogStructure().getRoot() + src);
    }

    public String fixToUrl(String html) {
        return resolveImageSrc(html, (src) -> src);
    }

    private String resolveImageSrc(String html, Function<String, String> handle) {
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.select("img");
        for (Element img : imgs) {
            String src = img.attr("src");
            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                img.attr("src", handle.apply(src));
            }
        }
        return removeBodyTag(doc.body().toString());
    }

    private String removeBodyTag(String fixed) {
        fixed = StringUtils.removeStart(fixed, "<body>");
        fixed = StringUtils.removeEnd(fixed, "</body>");
        return fixed;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
}

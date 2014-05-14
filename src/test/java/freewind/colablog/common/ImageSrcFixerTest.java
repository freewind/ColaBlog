package freewind.colablog.common;

import freewind.colablog.AppInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ImageSrcFixerTest {

    private ImageSrcFixer fixer;

    @Before
    public void setUp() throws Exception {
        fixer = new ImageSrcFixer();
        AppInfo appInfo = new AppInfo();
        appInfo.setCurrentBlogDir(new File("/users/freewind"));
        fixer.setAppInfo(appInfo);
    }

    @Test
    public void should_convert_image_src_to_file_path_when_converting_to_local() throws Exception {
        String fixed = fixer.fixToLocal("hello, <img src='/images/aaa.png' />");
        assertThat(fixed).contains("<img src=\"file:/users/freewind/images/aaa.png\" />");
    }

    @Test
    public void should_not_convert_images_if_src_starts_with_http() {
        String fixed = fixer.fixToLocal("hello, <img src='http://aaa.com/images/aaa.png' />");
        assertThat(fixed).contains("<img src=\"http://aaa.com/images/aaa.png\" />");
    }

    @Test
    public void should_not_convert_images_if_src_starts_with_https() {
        String fixed = fixer.fixToLocal("hello, <img src='https://aaa.com/images/aaa.png' />");
        assertThat(fixed).contains("<img src=\"https://aaa.com/images/aaa.png\" />");
    }


    @Test
    public void should_convert_image_src_not_modified_when_converting_to_url() throws Exception {
        String fixed = fixer.fixToUrl("hello, <img src='/images/aaa.png' />");
        assertThat(fixed).contains("<img src=\"/images/aaa.png\" />");
    }
}

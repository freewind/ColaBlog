package freewind.colablog;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class WordCounterTest {

    private WordCounter counter;

    @Before
    public void setUp() throws Exception {
        counter = new WordCounter();
    }

    @Test
    public void should_count_English_words_without_symbols() {
        int count = counter.count("Hello world");
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void should_treat_English_word_and_symbol_as_one_if_there_is_no_spacing_between_them() throws Exception {
        int count = counter.count("Hello, world !");
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void should_count_multiline_English_words() throws Exception {
        int count = counter.count("Hello \n world");
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void should_count_Chinese_words() throws Exception {
        int count = counter.count("中国");
        assertThat(count).isEqualTo(2);
    }


    @Test
    public void should_count_English_and_Chinese_words() throws Exception {
        int count = counter.count("Hello 中国");
        assertThat(count).isEqualTo(3);
    }

}

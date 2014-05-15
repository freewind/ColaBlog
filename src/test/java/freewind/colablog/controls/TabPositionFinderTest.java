package freewind.colablog.controls;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TabPositionFinderTest {

    public static final String FULL_TEXT = "abc\ndef\nghi";

    @Test
    public void should_find_0_as_position_if_selection_is_in_the_first_line() {
        List<Integer> positions = findPositions("b");
        assertThat(positions).containsOnly(0);
    }

    @Test
    public void should_find_previous_position_before_selection() {
        List<Integer> positions = findPositions("e");
        assertThat(positions).containsOnly(4);
    }

    @Test
    public void should_find_positions_inside_selection() {
        List<Integer> positions = findPositions("c\ndef\ng");
        assertThat(positions).contains(8, 4);
    }

    @Test
    public void should_ignore_last_line_separator_if_selection_ends_with_that() {
        List<Integer> positions = findPositions("c\ndef\n");
        assertThat(positions).excludes(8);
    }

    @Test
    public void should_not_find_previous_one_before_selection_if_it_starts_with_line_separator() {
        List<Integer> positions = findPositions("\ndef");
        assertThat(positions).containsOnly(4);
    }

    @Test
    public void should_get_0_as_position_is_cursor_is_at_beginning() {
        List<Integer> positions = cursorAt(0);
        assertThat(positions).containsOnly(0);
    }

    @Test
    public void should_get_the_position_for_the_last_line_separator_if_cursor_is_in_the_end() {
        List<Integer> positions = cursorAt(11);
        assertThat(positions).containsOnly(8);
    }

    @Test
    public void should_return_0_as_position_if_text_is_empty() {
        TabPositionFinder finder = new TabPositionFinder("", 0, 0);
        List<Integer> positions = finder.find();
        assertThat(positions).containsOnly(0);
    }

    private List<Integer> cursorAt(int cursorPosition) {
        TabPositionFinder finder = new TabPositionFinder(FULL_TEXT, cursorPosition, cursorPosition);
        return finder.find();
    }

    private List<Integer> findPositions(String selected) {
        int start = FULL_TEXT.indexOf(selected);
        TabPositionFinder finder = new TabPositionFinder(FULL_TEXT, start, start + selected.length());
        return finder.find();
    }

}

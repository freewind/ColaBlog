package freewind.colablog.common;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TextOperationsTest {

    @Test
    public void should_get_correct_line_index_if_cursor_is_at_the_beginning() throws Exception {
        assertThat(lineIndexWhenCursorBefore("a")).isEqualTo(0);
    }

    private int lineIndexWhenCursorBefore(String key) {
        int cursor = getCursorPositionBefore(key);
        return new TextOperations(CONTENT).whichLine(cursor);
    }

    private int getCursorPositionBefore(String key) {
        return CONTENT.indexOf(key);
    }

    private int lineIndexWhenCursorAfter(String key) {
        int cursor = getCursorPositionAfter(key);
        return new TextOperations(CONTENT).whichLine(cursor);
    }

    private int getCursorPositionAfter(String key) {
        int cursor = CONTENT.indexOf(key);
        return cursor + key.length();
    }

    @Test
    public void should_get_correct_line_index_if_cursor_is_at_the_end() throws Exception {
        assertThat(lineIndexWhenCursorAfter("R")).isEqualTo(4);
    }

    @Test
    public void should_get_correct_line_index_if_cursor_is_inside_a_line() throws Exception {
        assertThat(lineIndexWhenCursorBefore("3")).isEqualTo(1);
    }

    @Test
    public void should_get_correct_line_index_if_cursor_is_before_a_line_separator() throws Exception {
        assertThat(lineIndexWhenCursorAfter("c")).isEqualTo(0);
    }

    @Test
    public void should_get_correct_line_index_if_cursor_is_after_a_line_separator() throws Exception {
        assertThat(lineIndexWhenCursorBefore("1")).isEqualTo(1);
    }

    @Test
    public void should_get_correct_line_width() throws Exception {
        int width = new TextOperations(CONTENT).getLineWidth(0);
        assertThat(width).isEqualTo(3);
    }

    @Test
    public void should_get_correct_index_inline_for_given_cursor_position() throws Exception {
        int cursor = getCursorPositionBefore("p");
        int indexInLine = new TextOperations(CONTENT).getIndexInLine(cursor);
        assertThat(indexInLine).isEqualTo(1);
    }

    @Test
    public void should_move_cursor_down_correctly_if_cursor_is_inside_a_line() throws Exception {
        int cursor = getCursorPositionAfter("b");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveDown();

        assertThat(newCursor).isEqualTo(getCursorPositionAfter("2"));
    }

    @Test
    public void should_move_cursor_down_correctly_if_next_line_is_shorter() throws Exception {
        int cursor = getCursorPositionAfter("6");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveDown();
        assertThat(newCursor).isEqualTo(getCursorPositionAfter("q"));
    }

    @Test
    public void should_move_cursor_down_correctly_if_cursor_is_in_last_line() throws Exception {
        int cursor = getCursorPositionAfter("R");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveDown();
        assertThat(newCursor).isEqualTo(getCursorPositionAfter("R"));
    }

    @Test
    public void should_move_down_sequentially_across_lines_with_different_lengths() throws Exception {
        int cursor = getCursorPositionAfter("6");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        assertThat(upDownHandler.moveDown()).isEqualTo(getCursorPositionAfter("q"));
        assertThat(upDownHandler.moveDown()).isEqualTo(getCursorPositionAfter("F"));
    }

    @Test
    public void should_move_cursor_up_correctly_if_cursor_is_inside_a_line() throws Exception {
        int cursor = getCursorPositionAfter("2");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveUp();

        assertThat(newCursor).isEqualTo(getCursorPositionAfter("b"));
    }


    @Test
    public void should_move_cursor_up_correctly_if_previous_line_is_shorter() throws Exception {
        int cursor = getCursorPositionAfter("G");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveUp();

        assertThat(newCursor).isEqualTo(getCursorPositionAfter("q"));
    }

    @Test
    public void should_move_cursor_up_correctly_if_cursor_is_in_the_first_line() throws Exception {
        int cursor = getCursorPositionAfter("b");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        int newCursor = upDownHandler.moveUp();
        assertThat(newCursor).isEqualTo(0);
    }

    @Test
    public void should_move_up_sequentially_across_lines_with_different_lengths() throws Exception {
        int cursor = getCursorPositionAfter("F");
        TextOperations.MoveUpDownHandler upDownHandler = new TextOperations(CONTENT).createUpDownHandler(cursor);
        assertThat(upDownHandler.moveUp()).isEqualTo(getCursorPositionAfter("q"));
        assertThat(upDownHandler.moveUp()).isEqualTo(getCursorPositionAfter("6"));
    }

    public static final String CONTENT = "abc\n123456\nopq\nABCDEFGHIJ\nOPQR";

    private int getLineIndex(int cursor) {
        return new TextOperations(CONTENT).whichLine(cursor);
    }
}

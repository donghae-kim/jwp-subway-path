package subway.exception;

public class DuplicateLineNameException extends RuntimeException {
    public DuplicateLineNameException() {
        super("중복된 line 이름입니다.");
    }
}
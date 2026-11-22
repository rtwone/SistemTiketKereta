package tiketkereta.util;

public class ValidasiException extends Exception {

    public ValidasiException(String message) {
        super(message);
    }

    public ValidasiException(String message, Throwable cause) {
        super(message, cause);
    }
}

package kr.co.private_box.domain.exception;

public class LackOfPrivateUrlResource extends RuntimeException {
    public LackOfPrivateUrlResource(String message) {
        super(message);
    }
}

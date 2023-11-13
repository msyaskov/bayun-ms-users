package dev.bayun.ms.users.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Максим Яськов
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class NicknameOccupiedException extends RuntimeException {

    public NicknameOccupiedException() {
        super();
    }

    public NicknameOccupiedException(String message) {
        super(message);
    }

    public NicknameOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NicknameOccupiedException(Throwable cause) {
        super(cause);
    }
}

package subway.Exception;

import org.springframework.http.HttpStatus;

import java.security.PrivateKey;

public enum ErrorCode {
    CANNOT_ADD_SECTION(HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_SECTION(HttpStatus.BAD_REQUEST),
    LINE_NOT_FOUND(HttpStatus.BAD_REQUEST),
    STATION_NOT_FOUND(HttpStatus.BAD_REQUEST);
    private HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

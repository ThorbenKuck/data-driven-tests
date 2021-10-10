package com.github.thorbenkuck.framework;

public class ResponseEntity {

    private final Object body;
    private final ResponseStatus responseStatus;

    public ResponseEntity(Object body, ResponseStatus responseStatus) {
        this.body = body;
        this.responseStatus = responseStatus;
    }

    public static ResponseEntity ok() {
        return new ResponseEntity(null, ResponseStatus.OK);
    }

    public static ResponseEntity unauthorized() {
        return new ResponseEntity(null, ResponseStatus.UNAUTHORIZED);
    }

    public static ResponseEntity notFound() {
        return new ResponseEntity(null, ResponseStatus.NOT_FOUND);
    }

    public Object getBody() {
        return body;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }
}

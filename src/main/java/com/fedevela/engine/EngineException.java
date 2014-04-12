package com.fedevela.engine;

/**
 * Created by fvelazquez on 11/04/14.
 */
public class EngineException extends Exception {

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Throwable thrwbl) {
        super(message, thrwbl);
    }

}

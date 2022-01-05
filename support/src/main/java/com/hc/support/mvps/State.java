package com.hc.support.mvps;

public enum State {
    INIT, CREATE, BIND, UNBIND, DESTROY;

    public State next() {
        switch (this) {
            case INIT:
                return CREATE;
            case CREATE:
                return BIND;
            case BIND:
                return UNBIND;
            case UNBIND:
                return DESTROY;
        }
        return INIT;
    }
}

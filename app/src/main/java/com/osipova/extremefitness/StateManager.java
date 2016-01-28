package com.osipova.extremefitness;

import android.os.Bundle;

/**
 * Created by Osipova Ekaterina on 27.01.2016.
 */
public abstract class StateManager {

    public static final int STATE_PRELOAD = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_POSTLOAD = 2;

    private static final String KEY_LOAD_STATE = "KEY_LOAD_STATE";

    private int currentState;

    abstract void showPreLoadState();

    abstract void showLoadingState();

    abstract void showPostLoadState();

    private void showState() {
        switch (currentState) {
            case STATE_PRELOAD:
                showPreLoadState();
                break;
            case STATE_LOADING:
                showLoadingState();
                break;
            case STATE_POSTLOAD:
                showPostLoadState();
                break;
        }
    }

    public void setState(int state) {
        if (state != STATE_PRELOAD && state != STATE_LOADING && state != STATE_POSTLOAD)
            return;
        currentState = state;
        showState();
    }

    public void onSaveInstanceState (Bundle outState) {
        outState.putInt(KEY_LOAD_STATE, currentState);
    }

    public void onRestoreInstanceState (Bundle savedInstanceState) {
        setState(savedInstanceState.getInt(KEY_LOAD_STATE));
    }

    public boolean isLoading() {
        return currentState == STATE_LOADING;
    }
}

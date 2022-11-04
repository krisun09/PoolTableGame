package PoolGame.State;

import PoolGame.App;
import PoolGame.ConfigReader;

import java.lang.reflect.InvocationTargetException;

public abstract class AppState {
    protected App app;

    AppState(App app) {
        this.app = app;
    }

    public AppState getAppState() {
        return this;
    }

    public abstract ConfigReader playCurrLevel() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    public abstract ConfigReader levelUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    public abstract ConfigReader levelDown() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}

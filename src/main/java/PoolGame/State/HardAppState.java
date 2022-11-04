package PoolGame.State;

import PoolGame.App;
import PoolGame.ConfigReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HardAppState extends AppState {
    public HardAppState(App app) {
        super(app);
    }

    public AppState getAppState() {
        return this;
    }

    @Override
    public ConfigReader playCurrLevel() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod
                = App.class.getDeclaredMethod("loadConfig", List.class);

        // Set the accessibility as true
        privateMethod.setAccessible(true);

        List<String> arg = new ArrayList<>();
        arg.add("src/main/resources/config_hard.json");

        app.setAppState(this);

        return (ConfigReader) privateMethod.invoke(app,arg);
    }

    @Override
    public ConfigReader levelUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod
                = App.class.getDeclaredMethod("loadConfig", List.class);

        // Set the accessibility as true
        privateMethod.setAccessible(true);

        List<String> arg = new ArrayList<>();
        arg.add("src/main/resources/config_hard.json");

        app.setAppState(this);

        return (ConfigReader) privateMethod.invoke(app,arg);
    }

    @Override
    public ConfigReader levelDown() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method privateMethod
                = App.class.getDeclaredMethod("loadConfig", List.class);

        // Set the accessibility as true
        privateMethod.setAccessible(true);

        List<String> arg = new ArrayList<>();
        arg.add("src/main/resources/config_normal.json");

        app.setAppState(new NormalAppState(app));

        return (ConfigReader) privateMethod.invoke(app,arg);

    }
}

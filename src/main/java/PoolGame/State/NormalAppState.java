package PoolGame.State;

import PoolGame.App;
import PoolGame.ConfigReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NormalAppState extends AppState {

    public NormalAppState(App app) {
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
        arg.add("src/main/resources/config_normal.json");

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

        app.setAppState(new HardAppState(app));

        return (ConfigReader) privateMethod.invoke(app, arg);
    }

    @Override
    public ConfigReader levelDown() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod
                = App.class.getDeclaredMethod("loadConfig", List.class);

        // Set the accessibility as true
        privateMethod.setAccessible(true);

        List<String> arg = new ArrayList<>();
        arg.add("src/main/resources/config_easy.json");

        app.setAppState(new EasyAppState(app));

        return (ConfigReader) privateMethod.invoke(app, arg);
    }
}

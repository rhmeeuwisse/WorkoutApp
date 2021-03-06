package alobar.workout.app;

import android.app.Activity;
import android.app.Application;

import alobar.android.dagger.SystemModule;

/**
 * Workout App's application class
 */
public class WorkoutApp extends Application {

    public static WorkoutApp from(Activity activity) {
        return (WorkoutApp) activity.getApplication();
    }

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().systemModule(new SystemModule(this)).build();
    }

    public AppComponent getComponent() {
        return this.component;
    }
}

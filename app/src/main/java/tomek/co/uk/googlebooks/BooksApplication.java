package tomek.co.uk.googlebooks;

import android.app.Application;
import android.content.Context;

import tomek.co.uk.googlebooks.dependency.component.DaggerMainComponent;
import tomek.co.uk.googlebooks.dependency.component.MainComponent;
import timber.log.Timber;
import tomek.co.uk.googlebooks.dependency.module.AndroidModule;
import tomek.co.uk.googlebooks.dependency.module.MainDependencyModule;

/**
 * Main Application class.
 *
 * Created by tomek on 21/02/16.
 */
public class BooksApplication extends Application {

    private MainComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // enable timber lib for logging in debug
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        }

        // create dagger component
        mApplicationComponent = createAndGetMainComponent();
    }


    private MainComponent createAndGetMainComponent() {
        return DaggerMainComponent.builder().androidModule(new AndroidModule(this))
                .mainDependencyModule(new MainDependencyModule()).build();
    }

    public MainComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static BooksApplication getAppContext(Context context) {
        return (BooksApplication) context.getApplicationContext();
    }

}

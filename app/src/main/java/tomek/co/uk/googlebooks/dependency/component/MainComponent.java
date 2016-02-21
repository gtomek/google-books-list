package tomek.co.uk.googlebooks.dependency.component;

import javax.inject.Singleton;

import dagger.Component;
import tomek.co.uk.googlebooks.activity.MainActivity;
import tomek.co.uk.googlebooks.dependency.module.AndroidModule;
import tomek.co.uk.googlebooks.dependency.module.MainDependencyModule;

/**
 * Main dagger tomek.co.uk.googlebooks.dependency component of the app,
 *
 * Created by tomek on 21/02/16.
 */
@Singleton
@Component(modules = {
        AndroidModule.class,
        MainDependencyModule.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}

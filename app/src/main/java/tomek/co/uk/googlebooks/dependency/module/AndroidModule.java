package tomek.co.uk.googlebooks.dependency.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tomek.co.uk.googlebooks.BooksApplication;

/**
 * Dagger module for android context etc.
 */
@Module
public class AndroidModule {

    private final BooksApplication mApplication;

    public AndroidModule(BooksApplication application) {
        mApplication = application;
    }


    @Provides
    @Singleton
    Context provideContext() {
        return mApplication.getApplicationContext();
    }

}

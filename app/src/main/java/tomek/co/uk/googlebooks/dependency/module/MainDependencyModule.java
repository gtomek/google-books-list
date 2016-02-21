package tomek.co.uk.googlebooks.dependency.module;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import tomek.co.uk.googlebooks.BuildConfig;
import tomek.co.uk.googlebooks.network.BooksService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tomek.co.uk.googlebooks.R;

/**
 * Main dagger tomek.co.uk.googlebooks.dependency module.
 * <p/>
 * Created by Tomek on 06/08/15.
 */
@Module(includes = AndroidModule.class)
public class MainDependencyModule {

    @Provides
    @Singleton
    BooksService provideBooksService(Context app, OkHttpClient httpClient) {
        Retrofit retrofit = new Retrofit.Builder().client(httpClient)
                .baseUrl(app.getString(R.string.google_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit.create(BooksService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            // add HTTP logging
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder.build();
    }

}

package tomek.co.uk.googlebooks.dependency.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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

}

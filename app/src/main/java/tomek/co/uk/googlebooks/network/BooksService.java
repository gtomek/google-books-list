package tomek.co.uk.googlebooks.network;

import tomek.co.uk.googlebooks.model.BooksSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service for Google Books API.
 *
 * Created by tomek on 21/02/16.
 */
public interface BooksService {

    @GET("/https://www.googleapis.com/books/v1/volumes?q=android")
    Call<BooksSearchResponse> getBooksList(@Query("q") String bookSearchString);
}

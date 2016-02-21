package tomek.co.uk.googlebooks.network;

import rx.Observable;
import tomek.co.uk.googlebooks.model.BooksSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service for Google Books API.
 *
 * Created by tomek on 21/02/16.
 */
public interface BooksService {

    @GET("/books/v1/volumes")
    Observable<BooksSearchResponse> getBooksList(@Query("q") String bookSearchString);
}

package tomek.co.uk.googlebooks.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tomek.co.uk.googlebooks.BooksApplication;
import tomek.co.uk.googlebooks.R;
import tomek.co.uk.googlebooks.adapter.BooksAdapter;
import tomek.co.uk.googlebooks.model.BooksSearchResponse;
import tomek.co.uk.googlebooks.network.BooksService;

public class MainActivity extends AppCompatActivity {

    @Inject
    BooksService mBooksService;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.progress_bar)
    View mProgressBar;
    private LinearLayoutManager mLayoutManager;
    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BooksApplication.getAppContext(this).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();
        getBooksSearchObservable(mBooksService).subscribe(getBooksSubscriber(mProgressBar,
                mRecyclerView, mAdapter));
    }

    /**
     * Starts loading android books from the server.
     *
     * @param booksService
     */
    private Observable<BooksSearchResponse> getBooksSearchObservable(final BooksService booksService) {

        Timber.v("Requesting books list");

        return booksService.getBooksList("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscriber to the books service.
     *
     * @param progressBar
     * @param recyclerView
     * @param adapter
     */
    @NonNull
    private Subscriber<BooksSearchResponse> getBooksSubscriber(final View progressBar,
                                                               final RecyclerView recyclerView,
                                                               final BooksAdapter adapter) {
        return new Subscriber<BooksSearchResponse>() {
            @Override
            public void onCompleted() {
                Timber.v("get books completed");
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                Timber.e("get books error:%s", e);
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(BooksSearchResponse booksSearchResponse) {
                Timber.v("get books got response onNext");
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setData(booksSearchResponse.getItems());
            }
        };
    }

    public void setupRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BooksAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
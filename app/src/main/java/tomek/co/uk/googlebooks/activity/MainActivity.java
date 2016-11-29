package tomek.co.uk.googlebooks.activity;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tomek.co.uk.googlebooks.BooksApplication;
import tomek.co.uk.googlebooks.R;
import tomek.co.uk.googlebooks.adapter.BooksAdapter;
import tomek.co.uk.googlebooks.model.BooksSearchResponse;
import tomek.co.uk.googlebooks.network.BooksService;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_API_RESULTS = 40;
    private static final int MIN_SEARCH_TERM_LENGTH = 2;
    private static final long SEARCH_QUERY_UPDATE_DELAY = 400;
    private final String ARG_PAGE_START_INDEX = "arg_page_start_index";

    @Inject
    BooksService mBooksService;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.progress_bar)
    View mProgressBar;

    @Bind(R.id.search_field)
    EditText mSearchField;

    private LinearLayoutManager mLayoutManager;
    private BooksAdapter mAdapter;
    // start page index for the books API
    private int mStartIndex;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BooksApplication.getAppContext(this).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_PAGE_START_INDEX)) {
            mStartIndex = savedInstanceState.getInt(ARG_PAGE_START_INDEX);
            Timber.v("Saved page index restored:%d", mStartIndex);
        }

        setupRecyclerView();

        mSubscription = getSearchFieldObservable(mSearchField, mProgressBar)
                .flatMap(new Func1<String, Observable<BooksSearchResponse>>() {
                    @Override
                    public Observable<BooksSearchResponse> call(String searchTerm) {
                        return getBooksSearchObservable(mBooksService, mStartIndex, searchTerm);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getBooksSubscriber(mProgressBar,
                        mRecyclerView, mAdapter));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSubscription.isUnsubscribed()) {
            // FIXME: 28/02/16 try to subscribe here? instead of onCreate
            Timber.w("Warning, problem detected,");
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSubscription.unsubscribe();
    }


    /**
     * Get search field observable.
     *
     * @return observable to the search field
     */
    private Observable<String> getSearchFieldObservable(final  EditText searchField, final View progressBar) {
        return RxTextView.afterTextChangeEvents(searchField)
                .map(new Func1<TextViewAfterTextChangeEvent, String>() {
                    @Override
                    public String call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        return textViewAfterTextChangeEvent.editable().toString().trim(); // remove empty spaces
                    }
                })
                .filter(new Func1<String, Boolean>() { // check if search term is long enough
                    @Override
                    public Boolean call(String searchString) {
                        boolean isLongEnough = searchString.length() > MIN_SEARCH_TERM_LENGTH;
                        if (isLongEnough) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        return isLongEnough;
                    }
                })
                .debounce(SEARCH_QUERY_UPDATE_DELAY, TimeUnit.MILLISECONDS); // prevent too quick typing

    }

    /**
     * Starts loading android books from the server.
     *
     * @param booksService
     * @param startIndex
     */
    private Observable<BooksSearchResponse> getBooksSearchObservable(final BooksService booksService,
                                                                     int startIndex,
                                                                     final String searchTerm) {

        Timber.v("Requesting books list");
        // TODO: 21/02/16 Add pagination support
        return booksService.getBooksList(searchTerm, startIndex, MAX_API_RESULTS)
                .subscribeOn(Schedulers.io());
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
                Timber.e("get books error:%s", e.getMessage());
                progressBar.setVisibility(View.GONE);
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(ARG_PAGE_START_INDEX, mStartIndex);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}

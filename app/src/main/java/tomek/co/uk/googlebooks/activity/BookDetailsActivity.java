package tomek.co.uk.googlebooks.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import tomek.co.uk.googlebooks.R;
import tomek.co.uk.googlebooks.model.VolumeInfo;

public class BookDetailsActivity extends AppCompatActivity {

    public static String ARG_BOOK_INFO = "arg_book_info";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.text_book_title)
    TextView mTitle;

    @Bind(R.id.text_book_description)
    TextView mDescription;

    @Bind(R.id.book_image)
    ImageView mBookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(ARG_BOOK_INFO)) {
                VolumeInfo info = (VolumeInfo) extras.getParcelable(ARG_BOOK_INFO);
                bindVolumeInfo(info);
            }
        }
    }

    /**
     * Binds volume info to the view elements.
     *
     * @param info
     */
    private void bindVolumeInfo(VolumeInfo info) {
        Glide.with(this).load(info.getImageLinks().getThumbnail()).into(mBookImage);
        mTitle.setText(info.getTitle());
        mDescription.setText(info.getDescription());

    }

}

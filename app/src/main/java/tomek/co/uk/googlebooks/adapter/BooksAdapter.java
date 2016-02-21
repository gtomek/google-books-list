package tomek.co.uk.googlebooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;
import tomek.co.uk.googlebooks.R;
import tomek.co.uk.googlebooks.activity.BookDetailsActivity;
import tomek.co.uk.googlebooks.model.Item;
import tomek.co.uk.googlebooks.model.VolumeInfo;

/**
 * Adapter to the main recycler view.
 *
 * Created by tomek on 21/02/16.
 */
public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> mItems;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_book_list, parent, false);
        return new BooksListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int positionInAdapter = holder.getAdapterPosition();
        final Item item = mItems.get(positionInAdapter);

        VolumeInfo volumeInfo = item.getVolumeInfo();
        String thumbnail = volumeInfo.getImageLinks().getThumbnail();
        String title = volumeInfo.getTitle();

        ((BooksListViewHolder) holder).setData(thumbnail, title);
        // add on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("Item position:%d selected", positionInAdapter);
                // TODO: 21/02/16 Add logic switching to the details screen
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra(BookDetailsActivity.ARG_BOOK_INFO, item.getVolumeInfo());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mItems != null && !mItems.isEmpty()) ? mItems.size() : 0;
    }

    public void setData(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    /**
     * View Holder for the books list items.
     */
    public static class BooksListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_item_book_image)
        ImageView mImage;

        @Bind(R.id.list_item_book_title)
        TextView mTitle;

        public BooksListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String thumbnailUrl, final String title) {
            mTitle.setText(title);
            Glide.with(itemView.getContext()).load(thumbnailUrl).into(mImage);
        }
    }
}

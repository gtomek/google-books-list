package tomek.co.uk.googlebooks.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tomek.co.uk.googlebooks.model.Item;

/**
 * Adapter to the main recycler view.
 *
 * Created by tomek on 21/02/16.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setData(List<Item> items) {
        List<Item> mItems = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

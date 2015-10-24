package info.abdolahi.sajadlibrary.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.abdolahi.sajadlibrary.R;
import info.abdolahi.sajadlibrary.db.BookModel;

/**
 * Created by aliabdolahi on 10/24/15 AD.
 */
public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BookViewHolder> {

    List<BookModel> mData;
    private OnRecyclerViewItemClickListener listener;

    public BooksListAdapter(List<BookModel> mData) {
        this.mData = mData;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bookslist, parent, false);
        BookViewHolder pvh = new BookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.publisher.setText(mData.get(position).getPublisher());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerViewItemClicked(position, view.getId());
            }
        });
    }

    @Override
    public int getItemCount() {

        return (mData != null) ? mData.size() : 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cv)
        CardView cv;
        @Bind(R.id.booktitle)
        TextView title;
        @Bind(R.id.bookpublisher)
        TextView publisher;

        BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * everything belongs to onclick is here
     */

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        /**
         * Called when any item with in recyclerview or any item with in item
         * clicked
         *
         * @param position The position of the item
         * @param id       The id of the view which is clicked with in the item or
         *                 -1 if the item itself clicked
         */
        public void onRecyclerViewItemClicked(int position, int id);
    }


}

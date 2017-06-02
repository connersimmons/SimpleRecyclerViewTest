package connersimmons.me.simplerecyclerviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.jaychang.srv.SimpleCell;

public class CommentCell extends SimpleCell<Comment, CommentViewHolder> {

    private static final String TAG = CommentCell.class.getSimpleName();

    public CommentCell(Comment item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.view_twoline;
    }

    @NonNull
    @Override
    protected CommentViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new CommentViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(CommentViewHolder holder, int position, Context context, Object payload) {
        holder.bind(this.getItem().getName(), this.getItem().getEmail());
    }

    @Override
    protected long getItemId() {
        return this.getItem().getId();
    }
}

package connersimmons.me.simplerecyclerviewtest;

import android.view.View;
import android.widget.TextView;

import com.jaychang.srv.SimpleViewHolder;

public class CommentViewHolder extends SimpleViewHolder {

    private final View rootView;
    private final TextView tvItem;
    private final TextView tvItemDetail;

    public CommentViewHolder(View view) {
        super(view);

        rootView = view;
        tvItem = (TextView) view.findViewById(R.id.tvItem);
        tvItemDetail = (TextView) view.findViewById(R.id.tvItemDetail);
    }

    public View getRootView() {
        return rootView;
    }

    public void bind(final String name, final String detail) {
        tvItem.setText(name);
        tvItemDetail.setText(detail);
    }
}

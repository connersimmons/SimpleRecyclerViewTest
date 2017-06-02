package connersimmons.me.simplerecyclerviewtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SectionHeaderProviderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private SimpleRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (SimpleRecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void onResume() {
        setupRecyclerView();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                setupRecyclerView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        new ReceiveCommentsAsyncTask().execute();
    }

    private class ReceiveCommentsAsyncTask extends AsyncTask<Void, Void, List<CommentCell>> {

        private final String TAG = ReceiveCommentsAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.removeAllCells();
        }

        @Override
        protected List<CommentCell> doInBackground(Void... voids) {
            String url = "https://jsonplaceholder.typicode.com/comments";

            List<CommentCell> cells = new ArrayList<>();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                Log.d(TAG, responseData);
                JSONArray dataArray = new JSONArray(responseData);

                Log.d(TAG, dataArray.toString());

                List<Comment> itemList = new ArrayList<>();
                for (int i=0; i<dataArray.length(); i++) {
                    JSONObject commentsArray = dataArray.getJSONObject(i);

                    long postId = Long.parseLong(commentsArray.getString("postId"));
                    long id = Long.parseLong(commentsArray.getString("id"));
                    String name = commentsArray.getString("name");
                    String email = commentsArray.getString("email");
                    String body = commentsArray.getString("body");

                    Comment item = new Comment(postId, id, name, email, body);
                    itemList.add(item);
                }

                for (Comment item : itemList) {
                    CommentCell cell = new CommentCell(item);
                    cells.add(cell);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return cells;
        }

        @Override
        protected void onPostExecute(List<CommentCell> list) {
            super.onPostExecute(list);

            mRecyclerView.addCells(list);
            setupRecyclerViewHeaders();
        }

        private void setupRecyclerViewHeaders() {
            mRecyclerView.setSectionHeader(new SectionHeaderProviderAdapter<Comment>() {
                @NonNull
                @Override
                public View getSectionHeaderView(Comment item, int position) {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_section_header, null, false);
                    TextView textView = (TextView) view.findViewById(R.id.textView);
                    String text = "Post ID: " + item.getPostId();
                    textView.setText(text);
                    return view;
                }

                @Override
                public boolean isSameSection(Comment item, Comment nextItem) {
                    return item.getPostId() == nextItem.getPostId();
                }

                @Override
                public boolean isSticky() {
                    return true;
                }

                @Override
                public int getSectionHeaderMarginTop(Comment item, int position) {
                    return 0;
                }
            });
        }
    }
}

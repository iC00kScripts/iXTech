package ng.iceetech2016.teamui.ixtech.activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.adapter.PostAdapter;
import ng.iceetech2016.teamui.ixtech.app.VolleyController;
import ng.iceetech2016.teamui.ixtech.fragment.FeedbackForm;
import ng.iceetech2016.teamui.ixtech.model.FeedbackPOJO;
import ng.iceetech2016.teamui.ixtech.model.PostPOJO;
import ng.iceetech2016.teamui.ixtech.util.Messager;
import ng.iceetech2016.teamui.ixtech.util.iXTechUtils;

public class CDNetViewPostActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String API_LOCATION="12", JSONpersist="CDNetPost";
    private String type="";
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private List<PostPOJO> postPOJOList;
    private PostAdapter postAdapter;
    private PostPOJO postPOJO;

    private String TAG="POSTListing",post_id="",
            url="http://www.uitilities.com/iCeeTech2016/api/LoadAdminPosts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdnet_view_post);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(iXTechUtils.POST_TYPE))
            type=getIntent().getStringExtra(iXTechUtils.POST_TYPE);

        if(!type.equals("")&&!type.equals("CDNet"))
            fab.setVisibility(View.GONE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CDNet Announcements.");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postPOJOList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postPOJOList);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(postAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchList();
                                    }
                                }
        );

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                postPOJO = postPOJOList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //// TODO: 16/08/16 delete the comment on verification as listing owner
            }
        }));
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        fetchList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab)
    public void Clicked(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment= FeedbackForm.newInstance("CDNet");
        fragment.show(fragmentManager,"Add Feedback");
    }

    public void fetchList(){
        swipeRefreshLayout.setRefreshing(true);
        StringRequest req = new StringRequest(url, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String s) {
                swipeRefreshLayout.setRefreshing(false);
                iXTechUtils.SaveJSONResponse(CDNetViewPostActivity.this,s,JSONpersist);
                Log.d(TAG, "Comments:- "+s);
                try {
                    //convert json string array response to JSONArray Object and parse
                    JSONObject arr = new JSONObject(s);
                    iXTechUtils.SaveJSONResponse(CDNetViewPostActivity.this,arr.getString("posts"),JSONpersist);
                    ParseJSON(new JSONArray(arr.getString("posts")));

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG,error.toString());
                ParseJSON(iXTechUtils.ReadJSONFile(CDNetViewPostActivity.this,JSONpersist));
                if (error instanceof NoConnectionError)
                    new Messager(CDNetViewPostActivity.this).NoInternetDialog();
                else if (error instanceof TimeoutError)
                    new Messager(CDNetViewPostActivity.this).TimeoutErrorDialog();
                else
                    new Messager(CDNetViewPostActivity.this).ErrorDialog();
            }
        });/*{
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                //Adding parameters
                Log.d(TAG,"Post Params accessed");
                params.put("api_location", API_LOCATION);
                //params.put("post_id", post_id);
                return params;
            }
        };*/
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(req,TAG);
    }


    private void ParseJSON(JSONArray response) {
        if (!(response == null) && response.length() > 0) {
            postPOJOList.clear();
            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    JSONObject feedback = response.getJSONObject(i);
                    PostPOJO postPOJO = new PostPOJO(
                            feedback.getString("adminPostID"),
                            feedback.getString("adminPostContent"),
                            feedback.getString("adminEmail"),
                            feedback.getString("postDate")
                    );
                    postPOJOList.add(postPOJO);

                } catch (Exception e) {
                    Log.e(TAG, "Parse Response:-" + e.toString());
                }
            }
            postAdapter.notifyDataSetChanged();
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {this.clickListener = clickListener; gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                }
            }
        });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

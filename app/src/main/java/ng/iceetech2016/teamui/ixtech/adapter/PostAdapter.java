package ng.iceetech2016.teamui.ixtech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.model.PostPOJO;

/**
 * Created by unorthodox on 17/11/16.
 * Adapter class for populating the posts list
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private Context context;
    private List<PostPOJO> postPOJOs;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.comment_name) TextView Name;
        @Bind(R.id.comment_msg)TextView message;
        @Bind(R.id.date_posted)TextView datePosted;
        @Bind(R.id.comment_email)TextView email;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public PostAdapter(Context context, List<PostPOJO> postPOJOs){
        this.context=context;
        this.postPOJOs = postPOJOs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_comments_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostPOJO postPOJO = postPOJOs.get(position);
        holder.Name.setText("CDNetNG");
        holder.message.setText(postPOJO.getMessage());
        holder.datePosted.setText(postPOJO.getDate_added());
        holder.email.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return postPOJOs.size();
    }

}

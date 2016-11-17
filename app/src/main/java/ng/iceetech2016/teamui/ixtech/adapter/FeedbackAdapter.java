package ng.iceetech2016.teamui.ixtech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.model.FeedbackPOJO;

/**
 * Created by unorthodox on 17/11/16.
 * Adapter class for populating the feedback list
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>{
    private Context context;
    private List<FeedbackPOJO> feedbackPOJOs;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.comment_name) TextView Name;
        @Bind(R.id.comment_msg)TextView message;
        @Bind(R.id.date_posted)TextView datePosted;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public FeedbackAdapter(Context context, List<FeedbackPOJO> feedbackPOJOs){
        this.context=context;
        this.feedbackPOJOs = feedbackPOJOs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_comments_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeedbackPOJO feedbackPOJO = feedbackPOJOs.get(position);
        holder.Name.setText(feedbackPOJO.getCname());
        holder.message.setText(feedbackPOJO.getMessage());
        holder.datePosted.setText(feedbackPOJO.getDate_added());
    }
    @Override
    public int getItemCount() {
        return feedbackPOJOs.size();
    }

}

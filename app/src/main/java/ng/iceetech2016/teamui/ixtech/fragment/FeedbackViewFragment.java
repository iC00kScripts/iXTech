package ng.iceetech2016.teamui.ixtech.fragment;


import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.iceetech2016.teamui.ixtech.R;
import ng.iceetech2016.teamui.ixtech.model.FeedbackPOJO;

/**
 * A simple {@link Fragment} subclass.
 * used to display the comments info on click of a comment in the comments listing activity
 */
public class FeedbackViewFragment extends DialogFragment {

    @Bind(R.id.comment_name) TextView Name;
    @Bind(R.id.comment_email) TextView Email;
    @Bind(R.id.comment_msg) TextView Message;
    @Bind(R.id.date_posted) TextView Date;


    public FeedbackViewFragment() {
        // Required empty public constructor
    }

    public static FeedbackViewFragment newInstance(FeedbackPOJO feedbackPOJO) {
        FeedbackViewFragment feedbackViewFragment = new FeedbackViewFragment();
        Bundle args = new Bundle();
        args.putString("Name", feedbackPOJO.getCname());
        args.putString("Email", feedbackPOJO.getEmail());
        args.putString("Message", feedbackPOJO.getMessage());
        args.putString("Date", feedbackPOJO.getDate_added());
        feedbackViewFragment.setArguments(args);
        return feedbackViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_feedback_view, container, false);

        ButterKnife.bind(this,v);
        getDialog().setTitle("Feedback");
        getDialog().setCancelable(true);

        // Title divider
        final Resources res = getResources();
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(res.getColor(R.color.white));
        }

        Email.setText(getArguments().getString("Email"));
        Name.setText(getArguments().getString("Name"));
        Message.setText(getArguments().getString("Message"));
        Date.setText(getArguments().getString("Date"));
        return v;
    }

    @OnClick(R.id.commentOK)
    public void Close(){
        getDialog().dismiss();
    }

    public void onResume() {
        // Store access variables for window
        Window window = getDialog().getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = getDisplaySize(display);
        // Set the width of the dialog proportional to 95% of the screen width
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    @TargetApi(13)
    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }
}

package com.mobiledev.mail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobiledev.mail.R;
import com.mobiledev.mail.models.ResponseMail;
import com.mobiledev.mail.utils.GlobalClass;
import java.util.ArrayList;


/**
 * Created by hp on 17-Aug-16.
 */
public class MailListViewAdapter extends RecyclerView.Adapter<MailListViewAdapter.ViewHolder> {

    private static Context mCtx;
    ArrayList<ResponseMail> mResponseMail = new ArrayList<>();
    private OnItemClickListener mListener;

    public MailListViewAdapter(ArrayList<ResponseMail> mResponseMail, Context ctx) {
        this.mResponseMail = mResponseMail;
        mCtx = ctx;
    }

    @Override
    public MailListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_view,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MailListViewAdapter.ViewHolder holder, final int position) {
        holder.previewText.setText(mResponseMail.get(position).getPreview());
        holder.subjectText.setText(mResponseMail.get(position).getSubject());
        String text = mResponseMail.get(position).getParticipant().toString().replace("[", "").replace("]", "");
        holder.participantText.setText(text);
        if(mResponseMail.get(position).getIsRead().equals("true")){
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        if(mResponseMail.get(position).getIsStarred().equals("true")) {
            holder.mFavCheck.setButtonDrawable(R.drawable.ic_fav_fill);
        }
        Long timeStamp = Long.valueOf(mResponseMail.get(position).getTimeStamp());

        String result = GlobalClass.getTimeAgo(timeStamp,mCtx);
        result = result.replace("hours ago", "h");
        holder.timeText.setText(result);
        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, position);
                }
            });
        }


    }
    @Override
    public int getItemCount() {
        return mResponseMail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView participantText,subjectText,previewText,timeText;
        public CheckBox mFavCheck;
        public ViewHolder(View itemView) {
            super(itemView);

            participantText = (TextView) itemView.findViewById(R.id.participant_textView);
            subjectText = (TextView) itemView.findViewById(R.id.subject_textView);
            previewText = (TextView) itemView.findViewById(R.id.preview_textView);
            timeText = (TextView) itemView.findViewById(R.id.time_textView);
            mFavCheck = (CheckBox) itemView.findViewById(R.id.fav_check);
        }
    }



    public  interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    public void setOnClickListner(OnItemClickListener mListener){
        this.mListener = mListener;
    }
}

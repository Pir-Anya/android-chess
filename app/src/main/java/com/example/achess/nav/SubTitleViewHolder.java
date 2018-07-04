package com.example.achess.nav;

import android.view.View;
import android.widget.TextView;
import com.example.achess.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;



public class SubTitleViewHolder extends ChildViewHolder {

    private TextView subTitleTextView;

    public SubTitleViewHolder(View itemView) {
        super(itemView);
        subTitleTextView = (TextView) itemView.findViewById( R.id.subtitle);
    }

    public void setSubTitletName(String name) {
        subTitleTextView.setText(name);
    }
}

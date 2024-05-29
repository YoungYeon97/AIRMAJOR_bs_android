package com.woodnsoft.airmajor_bs_android.DataListView.DataGet;

import com.woodnsoft.airmajor_bs_android.bsHandax2.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DataGetView extends LinearLayout  {

    private TextView mText01;
    private TextView mText02;
    private TextView mText03;
    private TextView mText04;

    public DataGetView(Context context, DataGetItem aItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_dataget, this, true);

        // Set Text
        mText01 = (TextView) findViewById(R.id.dataItem01);
        mText01.setText(aItem.getData(0));
        mText02 = (TextView) findViewById(R.id.dataItem02);
        mText02.setText(aItem.getData(1));
        mText03 = (TextView) findViewById(R.id.dataItem03);
        mText03.setText(aItem.getData(2));
        mText04 = (TextView) findViewById(R.id.dataItem04);
        mText04.setText(aItem.getData(3));
        setColor(aItem.getData(3));
    }

    public void setText(int index, String data) {
        if (index == 0) {
            mText01.setText(data);
        } else if (index == 1) {
            mText02.setText(data);
        } else if (index == 2) {
            mText03.setText(data);
        } else if (index == 3) {
            mText04.setText(data);
            setColor(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setColor(String data) {
        if (data.equals("´ë±â")) {
            mText04.setTextColor(getResources().getColor(R.color.lk0));  //mText04.setTextColor(Color.parseColor("#333333"));
        } else if (data.equals("ÁøÇà")) {
            mText04.setTextColor(getResources().getColor(R.color.lk1));
        } else if (data.equals("½ÇÆÐ")) {
            mText04.setTextColor(getResources().getColor(R.color.lk9));
        } else {  //¿Ï·á
            mText04.setTextColor(getResources().getColor(R.color.lk2));
        }

    }


}
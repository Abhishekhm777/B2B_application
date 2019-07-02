package com.example.compaq.b2b_application.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public
class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private final GestureDetector mGestureDetector;
    private OnItemClickListener mListener;



    public
    interface OnItemClickListener {
        public
        void onItemClick(View view, int position);
    }

    public
    RecyclerItemClickListener(Context context, OnItemClickListener item_clicked) {
        mListener = (OnItemClickListener) item_clicked;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public
            boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public
    boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;

    }

    @Override
    public
    void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public
    void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }




}

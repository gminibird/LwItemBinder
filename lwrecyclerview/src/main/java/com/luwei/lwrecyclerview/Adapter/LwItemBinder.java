package com.luwei.lwrecyclerview.Adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by Mr_Zeng
 *
 * @date 2018/9/5
 */
public abstract class LwItemBinder<T> extends ItemViewBinder<T, LwViewHolder> {

    private OnItemClickListener<T> mListener;
    private OnItemLongClickListener<T> mLongListener;
    private SparseArray<OnChildClickListener<T>> mChildListenerMap = new SparseArray<>();
    private SparseArray<OnChildLongClickListener<T>> mChildLongListenerMap = new SparseArray<>();

    protected abstract View getView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBind(@NonNull LwViewHolder holder, @NonNull T item);

    @NonNull
    @Override
    protected final LwViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new LwViewHolder(getView(inflater, parent));
    }

    @Override
    protected final void onBindViewHolder(@NonNull LwViewHolder holder, @NonNull T item) {
        bindRootViewListener(holder, item);
        bindChildViewListener(holder, item);
        onBind(holder, item);
    }

    /**
     * 绑定子View点击事件
     *
     * @param holder
     * @param item
     */
    private void bindChildViewListener(LwViewHolder holder, T item) {
        //点击事件
        for (int i = 0; i < mChildListenerMap.size(); i++) {
            int id = mChildListenerMap.keyAt(i);
            View view = holder.getView(id);
            if (view != null) {
                view.setOnClickListener(v -> {
                    OnChildClickListener<T> l = mChildListenerMap.get(id);
                    if (l!=null){
                        l.onChildClick(holder,view,item);
                    }
                });
            }
        }
        //长按点击
        for (int i = 0; i < mChildLongListenerMap.size(); i++) {
            int id = mChildLongListenerMap.keyAt(i);
            View view = holder.getView(id);
            if (view != null) {
                view.setOnClickListener(v -> {
                    OnChildLongClickListener<T> l = mChildLongListenerMap.get(id);
                    if (l != null) {
                        l.onChildLongClick(holder,view, item);
                    }
                });
            }
        }
    }


    /**
     * 绑定根view
     *
     * @param holder
     * @param item
     */
    private void bindRootViewListener(LwViewHolder holder, T item) {
        //根View点击事件
        holder.getView().setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(holder, item);
            }
        });
        //根View长按事件
        holder.getView().setOnLongClickListener(v -> {
            boolean result = false;
            if (mLongListener != null) {
                result = mLongListener.onItemLongClick(holder, item);
            }
            return result;
        });
    }


    /**
     * 点击事件
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mListener = listener;
    }

    /**
     * 点击事件
     *
     * @param id 控件id，可传入子view ID
     * @param listener
     */
    public void setOnChildClickListener(@IdRes int id, OnChildClickListener<T> listener){
        mChildListenerMap.put(id,listener);
    }

    public void setOnChildLongClickListener(@IdRes int id, OnChildLongClickListener<T> listener){
        mChildLongListenerMap.put(id,listener);
    }

    /**
     * 长按点击事件
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<T> l) {
        mLongListener = l;
    }

    /**
     * 长按点击事件
     *
     * @param id 控件id，可传入子view ID
     */
    public void removeChildClickListener(@IdRes int id){
        mChildListenerMap.remove(id);
    }

    public void removeChildLongClickListener(@IdRes int id){
        mChildLongListenerMap.remove(id);
    }

    /**
     * 移除点击事件
     */
    public void removeItemClickListener() {
        mListener = null;
    }



    public void removeItemLongClickListener() {
        mLongListener = null;
    }


    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(LwViewHolder holder, T item);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(LwViewHolder holder, T item);
    }

    public interface OnChildClickListener<T> {
        void onChildClick(LwViewHolder holder, View child, T item);
    }

    public interface OnChildLongClickListener<T> {
        void onChildLongClick(LwViewHolder holder, View child, T item);
    }

}
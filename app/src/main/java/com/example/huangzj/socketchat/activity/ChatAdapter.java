package com.example.huangzj.socketchat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huangzj.socketchat.R;
import com.example.huangzj.socketchat.common.DLog;
import com.example.huangzj.socketchat.model.Message;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    /**
     * 发出去的查询消息
     */
    public static final int VIEW_TYPE_SEND = 0;
    /**
     * 收到的查询结果
     */
    public static final int VIEW_TYPE_RECEIVE = 1;
    /**
     * 消息的类型总数
     */
    public static final int VIEW_TYPE_COUNT = 2;
    private ArrayList<Message> dataList;
    private LayoutInflater layoutInflater; // 布局加载器
    private String name;

    public ChatAdapter(Context context, ArrayList<Message> dataList, String name) {
        super();
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
        this.name = name;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != dataList) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // Item的类型
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        Message entity = dataList.get(position);
        boolean flag = isComMsg(entity);

        if (flag) {
            return VIEW_TYPE_SEND;
        } else {
            return VIEW_TYPE_RECEIVE;
        }

    }

    /**
     * 判断当前是接收到的消息还是发送的消息
     *
     * @param entity
     * @return 发送：true；接收：false
     */
    private boolean isComMsg(Message entity) {
        String clientName = entity.getName();
        if (name != null && name.equals(clientName)) {
            return true;
        }
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            holder = new Holder();
            if (getItemViewType(position) == VIEW_TYPE_SEND) {// 发的提示语，在右边
                convertView = layoutInflater.inflate(
                        R.layout.chat_listview_send_item, parent, false);
            } else {
                convertView = layoutInflater.inflate(
                        R.layout.chat_listview_receive_item, parent, false);
            }

            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        refreshView(holder, position);
        return convertView;
    }

    private void refreshView(Holder holder, int position) {
        Message message = dataList.get(position);

        holder.name.setText(message.getName());
        holder.content.setText(message.getData());
    }

    /**
     * 持有者对象
     */
    class Holder {
        public TextView name;
        public TextView content;
    }
}

package com.example.huangzj.socketchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huangzj.socketchat.common.Constant;
import com.example.huangzj.socketchat.common.PersistentDataUtil;
import com.example.huangzj.socketchat.model.Message;
import com.example.huangzj.socketchat.service.ClientThread;
import com.example.huangzj.socketchat.common.Config;
import com.example.huangzj.socketchat.common.IpUtils;
import com.example.huangzj.socketchat.R;
import com.example.huangzj.socketchat.model.OnMessageReceiveListener;

import java.util.ArrayList;

/**
 * Created by huangzj on 2015/10/6.
 * <p/>
 * 客户端消息接收与发送
 */
public class ClientActivity extends Activity implements View.OnClickListener, OnMessageReceiveListener {

    private TextView ipTv;
    private ListView chatListView;
    private EditText inputContext;
    private Button sendBt;

    private ArrayList<Message> dataList = new ArrayList<>();

    private ChatAdapter adapter;

    private String name;

    private String serverIpAddress;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initWidget();

        getIpAddress();

        initListView();

        connectChat();
    }

    private void initListView() {
        adapter = new ChatAdapter(this, dataList, name);
        chatListView.setAdapter(adapter);
        chatListView.setSelection(chatListView.getCount() - 1);
    }

    private void initWidget() {
        ipTv = (TextView) findViewById(R.id.ip_tv);

        inputContext = (EditText) findViewById(R.id.input_content);
        sendBt = (Button) findViewById(R.id.send_bt);
        sendBt.setOnClickListener(this);

        chatListView = (ListView) findViewById(R.id.chat_list);
    }

    private void send() {

        if (clientThread == null) {
            Log.i("MainActivity", "clientThread == null");
            return;
        }
        clientThread.sendData(getData());
    }

    private Message getData() {
        String data = inputContext.getText().toString();
        inputContext.setText("");

        Message message = new Message();
        message.setData(data);
        message.setName(name);
        message.setType(Constant.Type_Data_Message);

        return message;
    }

    private void connectChat() {
        Config.isServer = false;
        Log.i("MainActivity", "connectChat()");
        clientThread = new ClientThread(this, serverIpAddress);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();
    }

    private void getIpAddress() {
        name = PersistentDataUtil.getInstance(this).getString("ClientName", "");

        serverIpAddress = getIntent().getStringExtra("ServerIp");
        ipTv.setText("当前连接的远程服务器：" + serverIpAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bt:
                send();
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceive(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (message.getType() == Constant.Type_Data_Message) {
                    dataList.add(message);
                    adapter.notifyDataSetChanged();
                    chatListView.setSelection(chatListView.getCount() - 1);
                } else {
                    Toast.makeText(ClientActivity.this, message.getData(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onFail(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ClientActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}

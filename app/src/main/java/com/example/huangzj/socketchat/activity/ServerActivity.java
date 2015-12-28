package com.example.huangzj.socketchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huangzj.socketchat.common.IpUtils;
import com.example.huangzj.socketchat.service.MyService;
import com.example.huangzj.socketchat.R;
import com.example.huangzj.socketchat.model.OnClientConnectListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzj on 2015/10/6.
 */
public class ServerActivity extends Activity implements OnClientConnectListener {

    private TextView ipAddressTV;
    private ListView clientListView;

    private List<String> dataList = new ArrayList<>();

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initWidget();
        showIpAddress();

        build();
    }

    private void initWidget() {
        ipAddressTV = (TextView) findViewById(R.id.ip_address);
        clientListView = (ListView) findViewById(R.id.client_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, dataList);
        clientListView.setAdapter(adapter);
    }

    private void showIpAddress() {
        String ipAddress = IpUtils.getIP();
        ipAddressTV.setText("本机ip地址：" + ipAddress);
    }

    private void build() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyService.build(ServerActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onClientConnect(final String name) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataList.add(name);
                adapter = new ArrayAdapter<>(ServerActivity.this, android.R.layout.simple_expandable_list_item_1, dataList);
                clientListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClientDisConnect(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String client = null;

                for (String clientName : dataList) {
                    if (clientName.equals(name)) {
                        client = clientName;
                        break;
                    }
                }
                dataList.remove(client);
                adapter = new ArrayAdapter<>(ServerActivity.this, android.R.layout.simple_expandable_list_item_1, dataList);
                clientListView.setAdapter(adapter);
            }
        });
    }
}


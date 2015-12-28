package com.example.huangzj.socketchat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.huangzj.socketchat.common.Config;
import com.example.huangzj.socketchat.R;
import com.example.huangzj.socketchat.common.PersistentDataUtil;

/**
 * Created by huangzj on 2015/10/6.
 */
public class FirstActivity extends Activity implements View.OnClickListener {

    private Button server_bt, client_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initWidget();

    }

    private void initWidget() {
        server_bt = (Button) findViewById(R.id.server_bt);
        client_bt = (Button) findViewById(R.id.client_bt);


        server_bt.setOnClickListener(this);
        client_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.server_bt:
                asServer();
                break;
            case R.id.client_bt:
                showSetNameDialog();
                break;
            default:
                break;
        }
    }

    private void asClient(String ip) {
        Config.isServer = false;

        PersistentDataUtil.getInstance(this).putString("ConnectedIp", ip);

        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("ServerIp", ip);
        startActivity(intent);

        finish();
    }

    private void asServer() {
        Config.isServer = true;

        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);

        finish();
    }

    private Dialog dialog;

    private void showSetNameDialog() {

        if (dialog != null && dialog.isShowing()) {
            return;
        }

        String name = PersistentDataUtil.getInstance(this).getString("ClientName", "");

        View dialogView = View.inflate(this,
                R.layout.dialog_set_name, null);

        dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        dialog.show();

        final EditText nameET = (EditText) dialogView.findViewById(R.id.ip_content);
        nameET.setText(name);
        Button sureBtn = (Button) dialogView.findViewById(R.id.sure);
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                PersistentDataUtil.getInstance(FirstActivity.this).putString("ClientName", nameET.getText().toString());
                showSetIpDialog();
            }
        });

    }

    private void showSetIpDialog() {

        if (dialog != null && dialog.isShowing()) {
            return;
        }

        String ip = PersistentDataUtil.getInstance(this).getString("ConnectedIp", "");

        View dialogView = View.inflate(this,
                R.layout.dialog_set_ip, null);

        dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        dialog.show();

        final EditText ipContent = (EditText) dialogView.findViewById(R.id.ip_content);
        ipContent.setText(ip);
        Button sureBtn = (Button) dialogView.findViewById(R.id.sure);
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                asClient(ipContent.getText().toString());
            }
        });

    }

}

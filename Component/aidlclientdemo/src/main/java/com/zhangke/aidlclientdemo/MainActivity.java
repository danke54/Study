package com.zhangke.aidlclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhangke.aidlserverdemo.Contacts;
import com.zhangke.aidlserverdemo.IContactsService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Contacts> contactsList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<Contacts>(this, android.R.layout.simple_list_item_1, contactsList);
        listView.setAdapter(arrayAdapter);
    }

    private IContactsService iContactsService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iContactsService = IContactsService.Stub.asInterface(service);

            try {
                // 设置重连
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iContactsService = null;

            // 设置重连
            System.out.println("onServiceDisconnected");
            bindRemteService(null);
        }
    };

    final IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            System.out.println("binder death");

            //1、设置重连
            bindRemteService(null);
        }
    };

    public void bindRemteService(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zhangke.aidlserverdemo", "com.zhangke.aidlserverdemo.ContactManagerService"));
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }



    private int id;
    public void sava(View view) {

        try {
            id++;
            iContactsService.saveContacts(new Contacts("tom" + id, "1390000" + (int) (Math.random() * 10000)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void get(View view) {
        try {
            contactsList.clear();
            contactsList.addAll(iContactsService.getContactsList());
            arrayAdapter.notifyDataSetChanged();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

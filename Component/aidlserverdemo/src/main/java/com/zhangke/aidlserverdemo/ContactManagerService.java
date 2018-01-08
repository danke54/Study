package com.zhangke.aidlserverdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class ContactManagerService extends Service {
    public ContactManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("远程服务开始连接...");
        return mBinder;
    }

    private List<Contacts> list = new ArrayList<>();
    /**
     * 创建binder对象
     */
    private IContactsService.Stub mBinder = new IContactsService.Stub() {
        @Override
        public int saveContacts(Contacts contacts) throws RemoteException {
            System.out.println("添加联系人：" + contacts.toString());
            list.add(contacts);
            return 0;
        }

        @Override
        public List<Contacts> getContactsList() throws RemoteException {
            System.out.println("查看联系人列表");
            return list;
        }
    };
}

// IContactsService.aidl
package com.zhangke.aidlserverdemo;

// Declare any non-default types here with import statements
import com.zhangke.aidlserverdemo.Contacts;

interface IContactsService {
    /**
    *保存联系人
    */
    int saveContacts(in Contacts contacts);
    /**
     *获取联系人列表
     */
    List<Contacts> getContactsList();
}

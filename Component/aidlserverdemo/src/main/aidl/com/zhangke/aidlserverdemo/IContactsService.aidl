
package com.zhangke.aidlserverdemo;

import com.zhangke.aidlserverdemo.Contacts;

interface IContactsService {
    /**
     *  保存联系人
     */
    int saveContacts(in Contacts contacts);
    /**
     *  获取联系人列表
     */
    List<Contacts> getContactsList();
}

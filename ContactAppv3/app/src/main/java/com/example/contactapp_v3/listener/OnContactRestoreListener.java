package com.example.contactapp_v3.listener;

import com.example.contactapp_v3.models.Contact;

public interface OnContactRestoreListener {
    void onRestoreClick(Contact contact);
    boolean onContactInsert(Contact contact);
    boolean onContactRemoveFromTrash(Contact contact);
}

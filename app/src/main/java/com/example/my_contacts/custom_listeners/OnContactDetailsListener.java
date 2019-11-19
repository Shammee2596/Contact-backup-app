package com.example.my_contacts.custom_listeners;

import com.example.my_contacts.models.ModelContact;

import java.io.Serializable;

public interface OnContactDetailsListener extends Serializable {

    void onContactDetails(ModelContact contact);

}

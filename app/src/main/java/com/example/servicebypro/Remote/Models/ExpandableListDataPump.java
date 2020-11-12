package com.example.servicebypro.Remote.Models;

import com.example.servicebypro.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData(List<String> services) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        expandableListDetail.put("My services", services);
        return expandableListDetail;
    }

}

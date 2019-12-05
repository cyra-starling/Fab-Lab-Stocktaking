package com.example.login.staff;

import java.util.Date;
import java.util.HashMap;

public class TransactionEntry{
    public String name;
    public String item;
    public String purpose;
    public String pillar;
    public Date id;
    public String studentId;
    public long quantity;

    public HashMap<String,Object> updates = new HashMap<String,Object>();

    TransactionEntry(){}

    TransactionEntry(String name, String item, String purpose, String pillar, long quantity, String studentId){
        this.name = name;
        this.item = item;
        this.pillar = pillar;
        this.purpose = purpose;
        this.quantity = quantity;
        this.studentId = studentId;

        this.updates.put("name", name);
        this.updates.put("item", item);
        this.updates.put("pillar", pillar);
        this.updates.put("purpose", purpose);
        this.updates.put("quantity",quantity);
        this.updates.put("studentId", studentId);
    }

    TransactionEntry(String name, String item, String purpose, String pillar, long quantity, Date id, String studentId){
        this.name = name;
        this.item = item;
        this.pillar = pillar;
        this.purpose = purpose;
        this.quantity = quantity;
        this.id = id;
        this.studentId = studentId;

        this.updates.put("name", name);
        this.updates.put("item", item);
        this.updates.put("pillar", pillar);
        this.updates.put("purpose", purpose);
        this.updates.put("quantity",quantity);
        this.updates.put("studentId", studentId);
    }
    @Override
    public String toString(){
        return "Name\t\t\t: " + name + "\n" +
                "Student ID\t\t: " + studentId + "\n" +
                "Item\t\t\t: " + item + "\n" +
                "Quantity\t\t: " + quantity +"\n" +
                "Pillar\t\t\t: " + pillar + "\n" +
                "Purpose\t\t\t: " + purpose;
    }
}
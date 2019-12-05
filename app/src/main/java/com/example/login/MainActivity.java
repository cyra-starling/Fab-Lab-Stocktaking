package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import com.example.login.parse.*;
import java.util.*;
import android.os.Parcelable;


import com.example.login.staff.Staff;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {
    /**
     * Context variable to be used in Toast.makeText to show errors
     */
    public static Context context;

    /**
     * Database references to access the real-time database in firebase
     */
    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference cardList = fablabStock.getReference("cardList");
    public static ArrayList<String> studentcard = new ArrayList<String>();
    public static ArrayList<String> staffcard = new ArrayList<String>();

    /**
     * Get list of cards registered in the database, both Staff's cards and Student's cards.
     */
    public static void getCards(){
        cardList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot studentCards = dataSnapshot.child("studentCards");
                DataSnapshot staffCards = dataSnapshot.child("staffCards");
                for (DataSnapshot card: studentCards.getChildren()){
                    DataSnapshot child = studentCards.child(card.getKey());
                    studentcard.add(child.getValue().toString());
                }
                for (DataSnapshot card: staffCards.getChildren()) {
                    DataSnapshot child = staffCards.child(card.getKey());
                    staffcard.add(child.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String text = "Database error: " + databaseError + ", Please contact admin";
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Runs when the app starts. Get the list of cards and get a reference to NFC reader in the
     * phone. If there's no NFC reader in the phone, it will give an alert to the user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        getCards();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC reader found", Toast.LENGTH_LONG).show();
            //finish();
            return;
        }
    }

    /**
     * Runs when the app is resumed. Get a reference to NFC reader in the phone. If NFC is not
     * turned on, it will open the Wireless setting for user to turn it on.
     */
    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
        else{
            Toast.makeText(this, "No NFC reader found", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Opens the phone's wireless settings
     */
    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    /**
     * Sets the new intent and resolves it
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    /**
     * Get the card's id to display message according to the card's id (registered as staff or
     * student or not registered)
     * @param intent
     */
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
        }
    }


    /**
     * Checks whether the scanned card is registered or not. If it is a student card, goes to
     * student page. If it is staff card, goes to staff page. If it is not registered, shows a text
     * to let the student know.
     * @param msgs
     */
    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str);
        }
        TextView text = findViewById(R.id.text);

        if (studentcard.contains(builder.toString())){
            Intent studentpg = new Intent(this, Student.class);
            startActivity(studentpg);
        }
        else if (staffcard.contains(builder.toString())){
            Intent staffpg = new Intent(this, Staff.class);
            startActivity(staffpg);
        }
        else{
            text.setText("Unrecognised card\nPlease contact FabLab Staff to register your card.\n");
        }
    }

    /**
     * Turns the tag's id to hex and append it to the string.
     * @param tag
     * @return
     */
    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append(toHex(id));
        return sb.toString();
    }

    /**
     * Convers a byte array into a hex string
     * @param bytes
     * @return Hex String
     */
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}

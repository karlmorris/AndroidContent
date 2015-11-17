package edu.temple.androidcontent;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.security.Permission;

public class MainActivity extends Activity {

    ContentResolver cr;
    Cursor cursor;

    TextView contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get a content resolver object
        cr = getContentResolver();


        // Check for permission to read contacts and get cursor
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1234);
        else
            cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


        contactName = (TextView) findViewById(R.id.nameTextView);

        // Step to next item in cursor
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = cursor.moveToNext();
                if (success)
                    contactName.setText(cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                    ));
            }
        });


        // Step to previous item in cursor
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean success = cursor.moveToPrevious();
                if (success)
                    contactName.setText(cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                    ));
            }
        });



        // Create cursor adapter and populate ListView
        findViewById(R.id.allButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView contactsList = (ListView) findViewById(R.id.contactsListView);

                // Columns to display
                String[] FROM_COLUMNS = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};

                // View ids to display respective column
                int[] TO_IDS = {android.R.id.text1};

                SimpleCursorAdapter contactsCurser = new SimpleCursorAdapter(
                        MainActivity.this,
                        R.layout.contact_list_item,
                        cursor,
                        FROM_COLUMNS, TO_IDS,
                        0);
                ;

                contactsList.setAdapter(contactsCurser);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }
}

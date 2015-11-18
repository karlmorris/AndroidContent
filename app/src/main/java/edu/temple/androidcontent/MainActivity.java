package edu.temple.androidcontent;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    ContentResolver cr;
    Cursor cursor;

    TextView contactName;
    EditText symbolEditText;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get a content resolver object
        cr = getContentResolver();


        // Check for permission to read contacts and get cursor
        if ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1234);

        contactName = (TextView) findViewById(R.id.nameTextView);
        symbolEditText = (EditText) findViewById(R.id.symbolEditText);

        // Step to next item in cursor
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cursor = cr.query(Uri.parse("content://edu.temple.androidcontentprovider.provider.STOCK_DATA")
                        , null
                        , "symbol=?"
                        , new String[]{symbolEditText.getText().toString()}
                        , null);

                boolean success = cursor.moveToFirst();
                if (success)
                    contactName.setText(cursor.getString(1));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        else {
            Toast.makeText(MainActivity.this, "This app cannot function without access to the data source", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}

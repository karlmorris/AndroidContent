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


}

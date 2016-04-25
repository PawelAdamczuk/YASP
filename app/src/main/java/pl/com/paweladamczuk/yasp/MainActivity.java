package pl.com.paweladamczuk.yasp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import jcifs.smb.SmbFile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jcifs.Config.setProperty( "jcifs.netbios.wins", "192.168.1.3" );
    }

    public void listLocalServers(View v) {
        final EditText IP = (EditText) findViewById(R.id.editText_ipAddress);
        final EditText username = (EditText) findViewById(R.id.editText_username);
        final EditText password = (EditText) findViewById(R.id.editText_password);

        Intent intent = new Intent(this, ShareList.class);

        intent.putExtra("IP", IP.getText().toString());
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());

        startActivity(intent);
    }
}

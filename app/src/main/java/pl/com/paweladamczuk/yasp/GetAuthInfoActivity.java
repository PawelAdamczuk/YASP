package pl.com.paweladamczuk.yasp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GetAuthInfoActivity extends AppCompatActivity {

    static final int GET_LOGIN_INFO = 0;
    static final int RESULT_OK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_auth_info);
    }

    public void connect(View view) {
        TextView username = (TextView) findViewById(R.id.editText_GetAuthInfo_username);
        TextView password = (TextView) findViewById(R.id.editText_GetAuthInfo_password);
        Intent result = new Intent();
        result.putExtra("username", username.getText().toString());
        result.putExtra("password", password.getText().toString());

        setResult(RESULT_OK, result);
        finish();
    }
}

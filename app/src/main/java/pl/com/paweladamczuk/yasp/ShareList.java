package pl.com.paweladamczuk.yasp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbFile;

public class ShareList extends AppCompatActivity {

    static final int GET_LOGIN_INFO = 0;
    static final int RESULT_OK = 0;

    ListView lv;
    SmbFile currentSmbContext;
    ArrayAdapter<SmbFile> adapter;
    SmbFile[] currentFileList;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);

        Intent intent = getIntent();

        lv = (ListView) findViewById(R.id.listView_folderSelect);
        lv.setOnItemClickListener(new ListClickHandler());
        new getServerInfo().execute();
    }

    private void redrawList() {

    }

    private class getServerInfo extends AsyncTask<Void, Void, SmbFile[]> {

        protected SmbFile[] doInBackground(Void... voids) {
            //android.os.Debug.waitForDebugger();
            SmbFile[] result = null;
            try {
                SmbFile handler = new SmbFile("smb://", NtlmPasswordAuthentication.ANONYMOUS);
                handler.setConnectTimeout(5000);
                result = handler.listFiles();
                Log.e("smb", Integer.toString(result.length));
                currentSmbContext = handler;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(SmbFile[] smb) {
            try {
                currentFileList = smb;
                adapter = new ArrayAdapter<SmbFile>(ShareList.this, android.R.layout.simple_list_item_1, currentFileList);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class updateFilesList extends AsyncTask<TaskArgsWrapper, Void, SmbFile[]> {

        protected SmbFile[] doInBackground(TaskArgsWrapper... args) {
            SmbFile[] result = null;
            try {
                SmbFile handler = new SmbFile(args[0].smbFile.getPath(), NtlmPasswordAuthentication.ANONYMOUS);
                handler.setConnectTimeout(5000);
                result = handler.listFiles();
                Log.e("smb", Integer.toString(result.length));
                //android.os.Debug.waitForDebugger();
                currentSmbContext = args[0].smbFile;
            } catch (SmbAuthException ex) {
                Intent i = new Intent(args[0].context, GetAuthInfoActivity.class);
                startActivityForResult(i, GET_LOGIN_INFO);
                android.os.Debug.waitForDebugger();
                SmbFile handler = new SmbFile(args[0].smbFile.getPath(), new NtlmPasswordAuthentication("", args[0].username, args[0].password));
                handler.setConnectTimeout(5000);
                result = handler.listFiles();
                Log.e("smb", Integer.toString(result.length));
                //android.os.Debug.waitForDebugger();
                currentSmbContext = args[0].smbFile;

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(SmbFile[] smb) {
            try {
                currentFileList = smb;
                adapter = new ArrayAdapter<SmbFile>(ShareList.this, android.R.layout.simple_list_item_1, currentFileList);
                lv.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            TaskArgsWrapper wrapper = new TaskArgsWrapper(currentFileList[position], username, password, ShareList.this.getApplicationContext());
            new updateFilesList().execute(wrapper);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_LOGIN_INFO) {
            if (resultCode == RESULT_OK) {
                this.username = data.getStringExtra("username");
                this.password = data.getStringExtra("password");
            }
        }


    }

    public class TaskArgsWrapper {
        public SmbFile smbFile;
        public String username;
        public String password;
        public Context context;

        public TaskArgsWrapper(SmbFile smbFile, String username, String password, Context context) {
            this.smbFile = smbFile;
            this.username = username;
            this.password = password;
            this.context = context;
        }
    }


}

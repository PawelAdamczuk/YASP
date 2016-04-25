package pl.com.paweladamczuk.yasp;

import android.util.Log;

import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by Admin on 22.04.2016.
 */
public class Smb {
    public static SmbFile[] lanList() throws MalformedURLException{
        SmbFile[] result = null;
        try {
            SmbFile handler = new SmbFile("smb://", NtlmPasswordAuthentication.ANONYMOUS);
            handler.setConnectTimeout(5000);
            handler.connect();
            result = handler.listFiles();
            Log.e("smb", Integer.toString(result.length));
        }
        catch (SmbException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
            Log.e("error", Integer.toString(e.getNtStatus()));
            //jcifs.smb.NtStatus
        }
        finally {
            //SmbFile[] test = {new SmbFile("smb://yasp_testing:yasp_testing")};
            //return test;
            return result;
        }
    }
}

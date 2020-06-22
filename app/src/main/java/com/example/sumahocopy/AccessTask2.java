package com.example.sumahocopy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class AccessTask2 extends AsyncTask<Integer, Integer, String> {
    private TextView textView;

    public AccessTask2(TextView txtView)
    {
        super();
        textView = txtView;
    }

    @Override
    protected String doInBackground(Integer... value)
    {
        File dir;
        String host = "192.168.1.200";
        String user = "user";
        String pswd = "pass";
        String str="**";

        String pathFrom = "/mnt/sdcard/DCIM/Camera/"; //Androidのupload 元

        try
        {
            // String path = "smb://" + user + ":" + pswd + "@" + host + "/disk2/temp/";

            String path_to = "smb://"  + host + "/disk/Photos/";



            dir = new File(pathFrom);
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {

                String filePath = files[i].getPath();
                Log.i("log",filePath);

                FileInputStream sfis = new FileInputStream(filePath);


                SmbFile dirTo = new SmbFile(path_to);
                if (!dirTo.exists()) {
                    dirTo.mkdirs();
                }

                SmbFileOutputStream fos = new SmbFileOutputStream(path_to + files[i].getName());


                byte buf[] = new byte[1024];
                int len;
                while ((len =sfis.read(buf)) != -1){
                    fos.write(buf, 0, len);
                }

                fos.flush();
                fos.close();
                sfis.close();

                files[i].delete();
            }
            str = Integer.toString(files.length) + "個のファイルをNASへ移動しました";
        }
        catch(Exception e)
        {
            Log.e("Err", e.toString());
        }

        return str;
    }

    @Override
    protected void onPostExecute(String param)
    {
        textView.setText(param);
    }
}


package com.example.sumahocopy;

import java.io.IOException;
import jcifs.smb.SmbFile;


public class Samba
{
    private int _isConnectionCode = -1;
    /**
     * 指定されたホストへのSamba接続を試行します。
     * 接続に成功したらtrue、失敗したらfalseを返します。
     */
    public boolean connect(
            final String host) throws  InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //thread.start()で処理される
                try {
                    //smb://{host}の接続文字列を作る
                    SmbFile smb = new SmbFile("smb://" + host);
                    smb.connect();
                    _isConnectionCode = 0;
                } catch (IOException e) {
                    //Process of error.
                    _isConnectionCode = 1;
                }
            }
        });
        thread.start();
        thread.join();
        return _isConnectionCode != 0 ? false : true;
    }
}
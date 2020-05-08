package com.mut.test_printer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.szzk.usb_printportlibs.USBFactory;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    //打印
    private UsbManager mUsbManager;
    private Context mContext;
    private HashMap<String, UsbDevice> deviceList;
    private Iterator<UsbDevice> deviceIterator ;
    public static USBFactory usbfactory;
    private Handler mHandler;
    private boolean t=false;
    private  UsbDevice device;
    private final int CONNECTRESULT=0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=(Button)findViewById(R.id.button);

        mContext=this;
        usbfactory= USBFactory.getUsbFactory(mContext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection();
                print();
            }
        });
    }
    private void Connection() {
        if(!usbfactory.is_connecusb())
        {
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            deviceList=mUsbManager.getDeviceList();
            deviceIterator=deviceList.values().iterator();
            if(deviceList.size()>0)
            {
                device=deviceIterator.next();
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(getApplicationInfo().packageName), 0);
                if (!mUsbManager.hasPermission(device)) {
                    mUsbManager.requestPermission(device,
                            mPermissionIntent);
                } else {
                    t=usbfactory.connectUsb(mUsbManager, device);
                    if(t)
                    {
                        // Toast_utils.ToastString(mContext, "连接成功!");

                    }else {
                        Toast_utils.ToastString(mContext, "USB连接失败");

                    }
                }
            }
            else
            {
                Toast_utils.ToastString(mContext, "没找到打印机");
                //搜索不到
            }
            //搜索打印机 并 连接
        }
        else
        {
            //打印机已经连接

        }
        mHandler=new MHandler();
        new mthread().start();
    }
    private void print()
    {  if(t)
    {

        usbfactory.PrintText("字体倍宽放大一倍","1","3",0);
        usbfactory.PrintText("正常大小打印正常大小打印正","1","1",10);
        usbfactory.PrintText("1字体倍宽放大一倍字体倍宽","1","1",20);
        usbfactory.PrintText("2字体倍宽放大一倍字体倍宽","1","1",30);
        usbfactory.PrintText("3正常大小打印正常大小打印","1","1",40);
        usbfactory.PrintText("4正常大小打印正常大小打印","1","1",50);
        usbfactory.PrintText("小票机器测试成功","2","1",50);
        usbfactory.PaperCut();
        }
        else
    {
        Toast_utils.ToastString(mContext, "USB连接失败!!!无法打印");
        }


    }
    int i=0;
    class mthread extends Thread{
        @Override
        public void run() {
            super.run();
            while(true)
            {
                i++;
                mHandler.sendEmptyMessage(CONNECTRESULT);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MHandler extends Handler {
        @SuppressLint("HandlerLeak")
        @SuppressWarnings("static-access")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case CONNECTRESULT: {

                    if(usbfactory!=null)
                    {
                        if(usbfactory.is_connecusb())
                        {

                        }else {

                            if(t)
                            {

                                t=false;
                            }
                        }
                    }
                    break;
                }

            }
        }
    }
}

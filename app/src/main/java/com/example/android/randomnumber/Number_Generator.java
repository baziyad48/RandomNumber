package com.example.android.randomnumber;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static androidx.core.content.ContextCompat.getSystemService;

public class Number_Generator extends Fragment {
    private static int flag = 0;
    //deklarasi shared prefernces untuk default
    private final String INTERVAL = "INTERVAL";
    private final int DEFAULT_INTERVAL = 500;
    private final String JACKPOT = "JACKPOT";
    private final int DEFAULT_JACKPOT = 8;
    //untuk memanipulasai view
    private TextView tv_number_one, tv_number_two, tv_number_three;
    private Button btn_start, btn_stop;
    //deklarasi thread
    private Thread bgthread_one, bgthread_two, bgthread_three;
    private boolean isPaused_one, isPaused_two, isPaused_three;
    //untuk nanti mengambil dari shared preferences
    private long delay;
    private int number;
    private SharedPreferences mPreferences;

    public Number_Generator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_number__generator, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mengambil nilai sesusai id nya
        tv_number_one = (TextView) getActivity().findViewById(R.id.frag_tv_number_one);
        tv_number_two = (TextView) getActivity().findViewById(R.id.frag_tv_number_two);
        tv_number_three = (TextView) getActivity().findViewById(R.id.frag_tv_number_three);
        //khusus untuk button
        btn_start = (Button) getActivity().findViewById(R.id.frag_btn_start);
        btn_stop = (Button) getActivity().findViewById(R.id.frag_btn_stop);
        //mengambil shraed prefernces
        mPreferences = getActivity().getSharedPreferences(getActivity().getApplication().toString(), Context.MODE_PRIVATE);
        delay = mPreferences.getLong(INTERVAL, DEFAULT_INTERVAL);
        number = mPreferences.getInt(JACKPOT, DEFAULT_JACKPOT);
        //jika button start di klik
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(bgthread_one == null || bgthread_one.getState() == Thread.State.TERMINATED){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                    isPaused_one = false;
                    try {
                        while(!isPaused_one){
                            Thread.sleep(delay);
                            tv_number_one.post(new Runnable() {
                                @Override
                                public void run() {
                                final Random random = new Random();
                                int number = random.nextInt(9);
                                tv_number_one.setText(Integer.toString(number));
                                }
                            });
                        }
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    }
                };
                bgthread_one = new Thread(runnable);
                bgthread_one.start();
            }
            //ini thread kedua
            if(bgthread_two == null || bgthread_two.getState() == Thread.State.TERMINATED){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                    isPaused_two = false;
                    try {
                        while(!isPaused_two){
                            Thread.sleep(delay);
                            tv_number_one.post(new Runnable() {
                                @Override
                                public void run() {
                                final Random random = new Random();
                                int number = random.nextInt(9);
                                tv_number_two.setText(Integer.toString(number));
                                }
                            });
                        }
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    }
                };
                bgthread_two = new Thread(runnable);
                bgthread_two.start();
            }
            //ini thread ketiga
            if(bgthread_three == null || bgthread_three.getState() == Thread.State.TERMINATED){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                    isPaused_three = false;
                    try {
                        while(!isPaused_three){
                            Thread.sleep(delay);
                            tv_number_one.post(new Runnable() {
                                @Override
                                public void run() {
                                final Random random = new Random();
                                int number = random.nextInt(9);
                                tv_number_three.setText(Integer.toString(number));
                                }
                            });
                        }
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    }
                };
                bgthread_three = new Thread(runnable);
                bgthread_three.start();
            }
            }
        });
        //ketika button stop di klik
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    isPaused_one = true;
                    bgthread_one.interrupt();
                    bgthread_one = null;
                    flag++;
                } else if(flag == 1){
                    isPaused_two = true;
                    bgthread_two.interrupt();
                    bgthread_two = null;
                    flag++;
                } else if(flag == 2){
                    isPaused_three = true;
                    bgthread_three.interrupt();
                    bgthread_three = null;
                    flag = 0;
                }
                //mengecek apakah jackpot
                if(isPaused_one && isPaused_two && isPaused_three){
                    int one = Integer.parseInt(tv_number_one.getText().toString());
                    int two = Integer.parseInt(tv_number_two.getText().toString());
                    int three = Integer.parseInt(tv_number_three.getText().toString());

                    if(one == number && two == number && three == number){
                        showNotification(getContext(),"You got Jackpot", "Congratulation!", new Intent());
                    }
                }
            }
        });
    }
    //menampilkan notifikasi apabila jackpot
    public void showNotification(Context context, String title, String body, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}

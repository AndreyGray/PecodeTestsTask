package com.example.pecodetesttask.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.pecodetesttask.Interface.onEventListener;
import com.example.pecodetesttask.MainActivity;
import com.example.pecodetesttask.R;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BlankFragment extends Fragment {

    private static final String TAG = "BlankFragment";

    public static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private int pageNumber;
    private String pageNum;
    private static final String CHANNEL_NAME = "my_channel_01";

    private  onEventListener mEventListener;

    public BlankFragment() {
    }

    public static BlankFragment newInstance(int page) {

        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mEventListener = (onEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
            pageNum = String.valueOf(pageNumber + 1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView tvPage = view.findViewById(R.id.page_number_tv);
        tvPage.setText(pageNum);

        //plusButton

        ImageButton plusButton = view.findViewById(R.id.button_plus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventListener.onEvent(pageNumber, "plus");
            }
        });

        //minusButton

        ImageButton minusButton = view.findViewById(R.id.button_minus);
        ImageView minus = view.findViewById(R.id.minus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete notification
                NotificationManager notificationManager =
                        (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(NOTIFICATION_SERVICE);
                Objects.requireNonNull(notificationManager).cancel(pageNumber);

                mEventListener.onEvent(pageNumber, "minus");
            }
        });
        if (pageNumber == 0) {
            minusButton.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
        }

        //notificationButton

        ImageButton notificationButton = view.findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationCreator();
            }
        });
        return view;
    }

    private void notificationCreator() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(Objects.requireNonNull(getActivity()), CHANNEL_NAME)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle("Chat heads active")
                        .setContentText("Notification " + pageNum)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(getPendingIntent())
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.blue));
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_NAME,
                    "Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
        if (notificationManager != null) {
            notificationManager.notify(pageNumber, notification);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        resultIntent.putExtra("NumPage", pageNumber);
        return PendingIntent.getActivity(getActivity(), pageNumber, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public int getPageNumber() {
        return pageNumber;
    }
}

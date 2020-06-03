package com.x1aolata.easyword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.User;
import com.x1aolata.easyword.db.WordInfo;
import com.x1aolata.easyword.util.Constants;
import com.x1aolata.easyword.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends AppCompatActivity {


    private TextView personal_username;
    private TextView personal_user_id;
    private TextView personal_word_counts;
    private TextView personal_persistence_days;

    private TextView personal_current_dictionary;
    private TextView personal_vocabulary;
    private TextView personal_about;
    private CircleImageView personal_avatar;
    private ImageView personal_back;

    private static Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        init();


        personal_current_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
            }
        });
        personal_vocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, WordInfoActivity.class);
                startActivity(intent);
            }
        });

        personal_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonalActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

                    setAttribute(Constants.username, Constants.user_id, msg.arg1, 5, Constants.avatar);
                }


            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {

                int counts = DAO.getInstance().getCountsOfUser(Constants.user_id);

                Message message = handler.obtainMessage();
                message.what = 1;
                message.arg1 = counts;
                handler.sendMessage(message);


            }
        }).start();


    }


    public void init() {
        // 获取实例
        personal_username = findViewById(R.id.personal_username);
        personal_user_id = findViewById(R.id.personal_user_id);
        personal_word_counts = findViewById(R.id.personal_word_counts);
        personal_persistence_days = findViewById(R.id.personal_persistence_days);
        personal_current_dictionary = findViewById(R.id.personal_current_dictionary);
        personal_vocabulary = findViewById(R.id.personal_vocabulary);
        personal_about = findViewById(R.id.personal_about);
        personal_avatar = findViewById(R.id.personal_avatar);
        personal_back = findViewById(R.id.personal_back);
        Utils.setTranslucent(this);
    }


    public void setAttribute(String username, String user_id, int word_counts, int persistence_days, Bitmap avatar) {
        personal_username.setText(String.valueOf(username));
        personal_user_id.setText("ID: " + String.valueOf(user_id));
        personal_word_counts.setText(String.valueOf(word_counts));
        personal_persistence_days.setText(String.valueOf(persistence_days));
        personal_avatar.setImageBitmap(avatar);

    }
}

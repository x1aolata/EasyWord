package com.x1aolata.easyword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.User;
import com.x1aolata.easyword.db.Word;
import com.x1aolata.easyword.helper.DBHelper;
import com.x1aolata.easyword.util.Constants;
import com.x1aolata.easyword.util.Utils;
import com.x1aolata.easyword.util.wave.Util;

import java.sql.Connection;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    private EditText login_user_id;
    private EditText login_password;
    private Button login_button;
    private static Handler handler;
    private TextView login_forgetpassword;
    private TextView login_register;
    private CheckBox login_remember_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    boolean status = (boolean) msg.obj;
                    if (status) {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Constants.user_id = login_user_id.getText().toString();
                        // 跳转到主界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 登陆失败
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        };


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utils.isConnected(getBaseContext())) {
                    Toast.makeText(LoginActivity.this, "无网络连接，请打开网络后重试", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationEqual(login_user_id, 11)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的账号", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationGreater(login_password, 6)) {
                    Toast.makeText(LoginActivity.this, "密码不得少于6位", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String user_id;
                            String password;
                            user_id = login_user_id.getText().toString();
                            password = login_password.getText().toString();
                            password = EncryptUtils.encryptSHA1ToString(password);

                            // 验证状态
                            boolean status = DAO.getInstance().verification_account(user_id, password);

                            if (status) {
                                User user = DAO.getInstance().getUserInfo(user_id);
                                Constants.phone_number = user.getPhone_number();
                                Constants.username = user.getUsername();
                                Constants.avatar = DAO.getInstance().getAvatar(user_id);
                            }
                            // 异步 向主线程发送消息
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            message.obj = status;
                            handler.sendMessage(message);

                        }
                    }).start();
                }


            }
        });


        login_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
            }
        });


        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    public void init() {
        // 获取实例
        login_user_id = findViewById(R.id.login_user_id);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        login_forgetpassword = findViewById(R.id.login_forgetpassword);
        login_register = findViewById(R.id.login_register);
        login_remember_password = findViewById(R.id.login_remember_password);
        Utils.setTranslucent(this);
        getSharedPreferences();
    }


    private void setSharedPreferences(String user_id, String password, boolean isChecked) {
        //定义SharedPreferences.Editor接口对象
        SharedPreferences.Editor edit = getPreferences(0).edit();
        if (!isChecked) {
            password = "";
        }
        //把登陆信息保存到preferences中
        edit.putString("user_id", user_id);
        edit.putString("password", password);
        edit.putBoolean("isChecked", isChecked);
        //提交信息
        edit.commit();
    }

    private void getSharedPreferences() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        login_user_id.setText(sp.getString("user_id", ""));
        login_password.setText(sp.getString("password", ""));
        login_remember_password.setChecked(sp.getBoolean("isChecked", false));
    }

    @Override
    protected void onDestroy() {
        setSharedPreferences(login_user_id.getText().toString(), login_password.getText().toString(), login_remember_password.isChecked());
        super.onDestroy();
    }
}

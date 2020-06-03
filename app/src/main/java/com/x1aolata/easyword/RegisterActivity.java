package com.x1aolata.easyword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.User;
import com.x1aolata.easyword.util.Utils;
import com.x1aolata.easyword.util.wave.Util;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private ImageView register_back;
    private EditText register_user_id;
    private EditText register_phone_number;
    private EditText register_password;
    private EditText register_username;
    private Button register_button;
    private static Handler handler;
    private CircleImageView register_avatar;
    private Bitmap avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    int result = (int) msg.obj;
                    if (result > 0) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
                if (msg.what == 2) {

                    Toast.makeText(RegisterActivity.this, "该账号已被注册", Toast.LENGTH_SHORT).show();

                }


            }
        };


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isConnected(getBaseContext())) {
                    Toast.makeText(RegisterActivity.this, "无网络连接，请打开网络后重试", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationEqual(register_user_id, 11)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的账号", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationEqual(register_username, 1)) {
                    Toast.makeText(RegisterActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationEqual(register_phone_number, 11)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else if (!Utils.verificationGreater(register_password, 6)) {
                    Toast.makeText(RegisterActivity.this, "密码不得少于6位", Toast.LENGTH_SHORT).show();
                } else if (register_avatar.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.drawable.login_image_logo).getConstantState())) {
                    Toast.makeText(RegisterActivity.this, "请点击Logo更换头像", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String user_id = register_user_id.getText().toString();
                            String phone_number = register_phone_number.getText().toString();
                            String username = register_username.getText().toString();
                            String password = register_password.getText().toString();
                            // 使用SHA1加密密码
                            password = EncryptUtils.encryptSHA1ToString(password);


                            if (DAO.getInstance().isRegistered(user_id)) {
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                handler.sendMessage(message);
                            } else {
                                User user = new User(user_id, phone_number, username, password);
                                int result = DAO.getInstance().create_account(user);

                                // 异步 向主线程发送消息
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                message.obj = result;
                                handler.sendMessage(message);


                                DAO.getInstance().uploadAvatar(avatar, user_id);
                            }

                        }
                    }).start();

                }


            }
        });


        register_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(RegisterActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true, 400, 400, 1, 1);
            }
        });

        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                if (pictureBean.isCut()) {

                    avatar = BitmapFactory.decodeFile(pictureBean.getPath());
                    register_avatar.setImageBitmap(avatar);

                } else {
                    register_avatar.setImageURI(pictureBean.getUri());
                }


            }
        }
    }

    public void init() {
        // 获取实例
        register_back = findViewById(R.id.register_back);
        register_user_id = findViewById(R.id.register_user_id);
        register_phone_number = findViewById(R.id.register_phone_number);
        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_button = findViewById(R.id.register_button);
        register_avatar = findViewById(R.id.register_avatar);
        Utils.setTranslucent(this);
    }
}

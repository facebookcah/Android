package vn.com.concatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import vn.com.database.UserDatabase;
import vn.com.model.User;

public class UpdateActivity extends AppCompatActivity {

    private EditText edtUpName,edtUpID,edtUpSDT,edtUpEmail;
    private ImageView btnDone,btnBack;
    private Toolbar toolbar;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControl();
        setSupportActionBar(toolbar);
       addEvent();
    }

    private void addEvent() {
         user= (User) getIntent().getExtras().get("o_user");

        edtUpName.setText(user.getUserName());
        edtUpSDT.setText(user.getSdt());
        edtUpEmail.setText(user.getEmail());
        btnDone=findViewById(R.id.btnDone);
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }



    private void addControl() {
        toolbar=findViewById(R.id.toolbar);
        edtUpEmail=findViewById(R.id.edtUpEmail);

        edtUpName=findViewById(R.id.edtUpName);
        edtUpSDT=findViewById(R.id.edtUpSDT);
        btnBack=findViewById(R.id.btnBack);
        btnDone=findViewById(R.id.btnDone);
    }
    private void update() {
        String name=edtUpName.getText().toString().trim();

        String SDT=edtUpSDT.getText().toString().trim();
        String email=edtUpEmail.getText().toString().trim();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(SDT))
        {
            return;
        }
        user.setUserName(name);
        user.setEmail(email);
        user.setSdt(SDT);
        UserDatabase.getInstance(this).userDAO().updateUser(user);
        Toast.makeText(this,"Update Success^^",Toast.LENGTH_SHORT).show();

        Intent intentResult=new Intent();
        setResult(MainActivity.RESULT_OK,intentResult);
        finish();


    }
}
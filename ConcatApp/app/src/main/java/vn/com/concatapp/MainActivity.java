package vn.com.concatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.adapter.UserAdapter;
import vn.com.database.UserDatabase;
import vn.com.model.User;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    private RecyclerView rcv;
    private FloatingActionButton btnAdd;
    private UserAdapter userAdapter;
    private List<User> mListUser;

    private ImageView btnSearch;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();

        addEvent();
    }

    private void addEvent() {
        userAdapter = new UserAdapter(new UserAdapter.IClickIconUser() {
            @Override
            public void updateUser(User user) {
                updateInfomation(user);

            }

            @Override
            public void deleteUser(User user) {
                deleteinfimation(user);
            }
        });
        mListUser = new ArrayList<>();
        userAdapter.setData(mListUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(userAdapter);
        loadData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        rcv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) btnAdd.hide();
                else btnAdd.show();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtSearch.requestFocus();
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH)
                {
                    searchUser();
                    hideKeyBoard();

                }


                return  false;
            }
        });




    }
    public void hideKeyBoard()
    {
        try
        {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (NullPointerException nl)
        {
            nl.printStackTrace();
        }
    }

    private void searchUser() {
        String keyWord=edtSearch.getText().toString().trim();
        mListUser=new ArrayList<>();
        mListUser=UserDatabase.getInstance(MainActivity.this).userDAO().mListUser(keyWord);
        userAdapter.setData(mListUser);
        if(mListUser==null) Toast.makeText(MainActivity.this,"Người dừng không tồn tại",Toast.LENGTH_SHORT).show();

    }

    private void deleteinfimation(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete user")
                .setMessage("Do you want delete this user ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDatabase.getInstance(MainActivity.this)
                                .userDAO().deleteUser(user);
                        Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("no", null)
                .show();
    }


    private void addUser() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_layout);

        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtSDT = dialog.findViewById(R.id.edtSDT);
        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        Button btnAction = dialog.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                String sdt = "";
                String email = "";
                int id = (int) (Math.random() * 1000);
                if (!edtName.getText().toString().trim().equals(""))
                    name = edtName.getText().toString().trim();
                else
                    Toast.makeText(MainActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                if (!edtEmail.getText().toString().trim().equals(""))
                    email = edtEmail.getText().toString().trim();
                else
                    Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                if (!edtSDT.getText().toString().trim().equals("")) {

                    sdt = edtSDT.getText().toString().trim();
                    User user = new User(id, name, sdt, email);
                    if (isExist(user))
                        Toast.makeText(MainActivity.this, "Người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                    else {
                        UserDatabase.getInstance(MainActivity.this).userDAO().insertUser(user);
                        loadData();
                        Toast.makeText(MainActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();

                        edtEmail.setText("");
                        edtName.setText("");
                        edtSDT.setText("");
                        dialog.dismiss();
                    }
                } else
                    Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void loadData() {
        mListUser = UserDatabase.getInstance(this).userDAO().mListUser();
        Collections.sort(mListUser);
        userAdapter.setData(mListUser);
    }

    private boolean isExist(User user) {
        List<User> list = UserDatabase.getInstance(this).userDAO().checkUser(user.getUserName());
        return list != null && !list.isEmpty();
    }

    private void updateInfomation(User user) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("o_user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    private void addControl() {
        btnSearch=findViewById(R.id.btnSearch);

        edtSearch=findViewById(R.id.edtSearch);

        rcv = findViewById(R.id.rcv);
        btnAdd = findViewById(R.id.btnAdd);
    }
}
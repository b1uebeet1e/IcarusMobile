package mobile.icarus;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private TextView error;
    private EditText user, pwd;
    private Document doc;
    private Button login;
    private Map<String, String> cookies;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=(EditText)findViewById(R.id.user);
        pwd=(EditText)findViewById(R.id.pwd);
        error=(TextView)findViewById(R.id.error);
        login=(Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_NULL){
                    login.performClick();
                }
                return false;
            }
        });
    }

    private void changeActivity(){
        Intent i=new Intent(LoginActivity.this, TabActivity.class);
        i.putExtra("doc", doc.html());
        i.putExtra("cookies", (HashMap<String, String>)cookies);
        startActivity(i);
        finish();
    }

    private void logIn(){
        if(isLogInEmpty()) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder str = new StringBuilder();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login.setEnabled(false);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        error.setText("");
                    }
                });
                try{
                    Connection.Response loginForm = Jsoup
                            .connect("https://icarus-icsd.aegean.gr/authentication.php")
                            .referrer("https://icarus-icsd.aegean.gr/")
                            .method(Connection.Method.POST)
                            .data("username", user.getText().toString())
                            .data("pwd", pwd.getText().toString())
                            .execute();

                    cookies=loginForm.cookies();

                    doc=loginForm.parse();

                    if(doc.select("div[id=header_login]").select("u").html().length()==0) str.append("invalid credentials");

                }catch (Exception e){
                    str.append("network error");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                        error.setTextColor(Color.RED);
                        if(str.length()>0){
                            error.setText(str.toString());
                            login.setEnabled(true);
                        }
                        else{
                            error.setText("success");
                            error.setTextColor(Color.GREEN);
                            changeActivity();

                        }
                    }
                });
            }
        }).start();
    }


    private boolean isLogInEmpty(){
        boolean empty=false;
        if(TextUtils.isEmpty(user.getText().toString())){
            user.setError("username cannot be empty");
            empty=true;
        }
        if(TextUtils.isEmpty(pwd.getText().toString())){
            pwd.setError("password cannot be empty");
            empty=true;
        }
        return empty;

    }
}

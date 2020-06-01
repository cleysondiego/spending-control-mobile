package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.LoginRequest;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;

    EditText etLogin;
    EditText etPassword;

    Button btnLogin;
    Button btnRegister;

    private ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        apiRequest = new ApiRequest();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogin:
            case R.id.etPassword:
                boolean isEmailValid = validateField(etLogin);
                if (isEmailValid) {
                    boolean isPasswordValid = validateField(etPassword);
                    if (isPasswordValid) {
                        doLogin();
                    }
                }
                break;
            case R.id.btnRegister:
                goToRegisterActivity();
                break;
            default:
                break;
        }
    }

    private void doLogin() {
        final HashMap<String, String> headers = new HashMap<>();
        headers.put(ApiRequest.CONTENT_TYPE, "application/json");

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail(etLogin.getText().toString());
        loginRequest.setPassword(etPassword.getText().toString());

        final String requestParams = loginRequest.getEventString();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                apiRequest.post(ApiRequest.BASE_URL + "/sessions", headers, requestParams, new ApiRequest.OnResponse() {
                    @Override
                    public void onResponse(int statusCode, byte[] response) {
//                        Toast.makeText(context, "Deu sucesso no login.", Toast.LENGTH_SHORT).show();
//                        goToMainActivity();
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        if (statusCode == 400) {
                            Toast.makeText(context, "Usuário inválido e/ou senha incorreta!", Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(context, "Erro desconhecido, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        thread.start();
    }

    private boolean validateField(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Esse campo não pode ser vazio");
            field.requestFocus();
            return false;
        } else if (field.getText().toString().length() < 5) {
            field.setError("O campo deve conter mais que 5 caracteres.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

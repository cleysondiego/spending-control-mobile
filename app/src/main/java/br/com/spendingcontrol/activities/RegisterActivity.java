package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.usecases.RegisterUseCase;
import br.com.spendingcontrol.usecases.ThreadExecutor;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.RegisterRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etName;
    EditText etEmail;
    EditText etRegPassword;
    EditText etConfirmPassword;

    Button btnRegRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegRegister = findViewById(R.id.btnRegRegister);

        btnRegRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(validateFields(etEmail)) {
            if (validateFields(etRegPassword) && validateFields(etConfirmPassword)) {
                if (validatePasswords(etRegPassword, etConfirmPassword)) {
                    switch (v.getId()) {
                        case R.id.btnRegister:
                        case R.id.etConfirmPassword:
                            registerUser();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setName(etName.getText().toString());
        registerRequest.setEmail(etEmail.getText().toString());
        registerRequest.setPassword(etRegPassword.getText().toString());

        ApiRequest apiRequest = new ApiRequest();

        RegisterUseCase registerUseCase = new RegisterUseCase(ThreadExecutor.getInstance(), apiRequest, registerRequest);
        registerUseCase.setCallback(new RegisterUseCase.OnRegisterCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Registrado com sucesso, por favor efetue o login.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode) {
                if (statusCode == 400) {
                    Toast.makeText(getApplicationContext(), "Email já cadastrado", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Ocorreu um erro durante o cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });

        registerUseCase.execute();
    }

    private boolean validateFields(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Esse campo não pode ser vazio");
            field.requestFocus();
            return false;
        } else if (field.getText().toString().length() < 5) {
            field.setError("Esse campo deve conter mais que 5 caracteres.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePasswords(EditText password, EditText confirmPassword) {
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            password.setError("As senhas devem ser iguais");
            confirmPassword.setError("As senhas devem ser iguais");
            password.requestFocus();
            return false;
        }
        return true;
    }
}

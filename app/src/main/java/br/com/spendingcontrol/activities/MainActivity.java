package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private boolean isLogged = false;
    private String userToken = "";

    TextView txtBalanceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        getDataFromSharedPreferences(context);

        if (!isLogged || userToken.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        txtBalanceNumber = findViewById(R.id.txtBalanceNumber);

        txtBalanceNumber.setText("R$: 500");
    }

    @Override
    public void onResume() {
        super.onResume();

        getDataFromSharedPreferences(context);

        if (!isLogged || userToken.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getDataFromSharedPreferences(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        isLogged = sharedPreferencesUtils.getLoggedStatus(context);
        userToken = sharedPreferencesUtils.getUserToken(context);
    }
}

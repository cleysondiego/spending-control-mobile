package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.usecases.GetSpendingsUseCase;
import br.com.spendingcontrol.usecases.ThreadExecutor;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;
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

        getDataFromAPI();
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

    private void getDataFromSharedPreferences(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        isLogged = sharedPreferencesUtils.getLoggedStatus(context);
        userToken = sharedPreferencesUtils.getUserToken(context);
    }

    private void getDataFromAPI() {
        ApiRequest apiRequest = new ApiRequest();

        GetSpendingsUseCase getSpendingsUseCase = new GetSpendingsUseCase(ThreadExecutor.getInstance(), apiRequest, userToken);
        getSpendingsUseCase.setCallback(new GetSpendingsUseCase.OnGetSpendingsCallback() {
            @Override
            public void onSuccess(GetSpendingsResponseStructure getSpendingsResponseStructure) {
                Integer balance = getSpendingsResponseStructure.getBalance();

                if (balance < 0) {
                    txtBalanceNumber.setTextColor(Color.RED);
                } else {
                    txtBalanceNumber.setTextColor(Color.GREEN);
                }

                txtBalanceNumber.setText("" + balance);
            }

            @Override
            public void onFailure(int statusCode) {

            }
        });

        getSpendingsUseCase.execute();
    }
}

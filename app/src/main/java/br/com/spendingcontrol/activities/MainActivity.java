package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.usecases.GetSpendingsUseCase;
import br.com.spendingcontrol.usecases.ThreadExecutor;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;
import br.com.spendingcontrol.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private boolean isLogged = false;
    private String userToken = "";
    private GetSpendingsResponseStructure spendingsResponseStructure;

    TextView txtBalanceNumber;
    ListView listSpending;

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
        listSpending = findViewById(R.id.listSpending);

        listSpending.setOnItemClickListener(this);

        spendingsResponseStructure = new GetSpendingsResponseStructure();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        sharedPreferencesUtils.setLogoff(context);

        recreate();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        goToDetailsActivity(parent, position);
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
                spendingsResponseStructure = getSpendingsResponseStructure;
                populateBalance(spendingsResponseStructure.getBalance());
                populateListViewSpendings(spendingsResponseStructure.getSpendings());
            }

            @Override
            public void onFailure(int statusCode) {

            }
        });

        getSpendingsUseCase.execute();
    }

    private void populateBalance(Integer balance) {
        if (balance < 0) {
            txtBalanceNumber.setTextColor(Color.RED);
        } else {
            txtBalanceNumber.setTextColor(Color.GREEN);
        }

        txtBalanceNumber.setText(balance.toString());
    }

    private void populateListViewSpendings(List<GetSpendingsResponseStructure.Spendings> spendingsList) {
        List<String> title = new ArrayList<>();

        for (GetSpendingsResponseStructure.Spendings spendings : spendingsList) {
            title.add(spendings.getTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, title);

        listSpending.setAdapter(arrayAdapter);
    }

    private void goToDetailsActivity(AdapterView<?> parent, int position) {
        String spendTitle = parent.getItemAtPosition(position).toString();

        List<GetSpendingsResponseStructure.Spendings> spendingsList = spendingsResponseStructure.getSpendings();

        GetSpendingsResponseStructure.Spendings spend = new GetSpendingsResponseStructure.Spendings();

        for (GetSpendingsResponseStructure.Spendings spendings : spendingsList) {
            if (spendings.getTitle().equals(spendTitle)) {
                spend = spendings;
            }
        }

        Intent intent = new Intent(this, SpendingDetailsActivity.class);
        intent.putExtra("SPENDINGS_OBJECT", spend);
        startActivity(intent);
    }
}

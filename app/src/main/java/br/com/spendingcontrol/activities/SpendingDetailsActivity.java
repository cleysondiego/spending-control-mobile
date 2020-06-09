package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.usecases.SpendDeleteUseCase;
import br.com.spendingcontrol.usecases.ThreadExecutor;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;
import br.com.spendingcontrol.utils.SharedPreferencesUtils;

public class SpendingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String userToken = "";

    GetSpendingsResponseStructure.Spendings spend;

    EditText etTitleDetails;
    EditText etDescriptionDetails;
    EditText etValueDetails;
    EditText etTypeDetails;

    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_details);

        spend = (GetSpendingsResponseStructure.Spendings) getIntent().getSerializableExtra("SPENDINGS_OBJECT");

        etTitleDetails = findViewById(R.id.etTitleDetails);
        etDescriptionDetails = findViewById(R.id.etDescriptionDetails);
        etValueDetails = findViewById(R.id.etValueDetails);
        etTypeDetails = findViewById(R.id.etTypeDetails);

        fillEt();
        getDataFromSharedPreferences(getApplicationContext());

        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        deleteItem();
    }

    public void deleteItem() {
        ApiRequest apiRequest = new ApiRequest();
        SpendDeleteUseCase spendDeleteUseCase = new SpendDeleteUseCase(ThreadExecutor.getInstance(), spend, apiRequest, userToken);
        spendDeleteUseCase.setCallback(new SpendDeleteUseCase.OnSpendDeleteCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Registro deletado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode) {
                Toast.makeText(getApplicationContext(), "Não foi possivel deletar o registro no momento, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });

        spendDeleteUseCase.execute();
    }

    public void fillEt() {
        etTitleDetails.setText(spend.getTitle());
        etDescriptionDetails.setText(spend.getDescription());
        etValueDetails.setText(spend.getValue().toString());
        etTypeDetails.setText(spend.getType().equals("credit") ? "Crédito" : "Débito");
    }

    private void getDataFromSharedPreferences(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        userToken = sharedPreferencesUtils.getUserToken(context);
    }
}

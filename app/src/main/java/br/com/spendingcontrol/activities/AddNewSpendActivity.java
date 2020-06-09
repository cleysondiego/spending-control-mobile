package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.usecases.AddNewSpendUseCase;
import br.com.spendingcontrol.usecases.ThreadExecutor;
import br.com.spendingcontrol.utils.AddNewSpendRequest;
import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.SharedPreferencesUtils;

public class AddNewSpendActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String userToken = "";

    EditText etTitle;
    EditText etDescription;
    EditText etValue;

    Spinner spnType;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_spend);

        context = getApplicationContext();

        getDataFromSharedPreferences(context);

        spnType = findViewById(R.id.spnType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etValue = findViewById(R.id.etValue);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        saveSpend();
    }

    private void saveSpend() {
        AddNewSpendRequest addNewSpendRequest = new AddNewSpendRequest();

        addNewSpendRequest.setTitle(etTitle.getText().toString());
        addNewSpendRequest.setDescription(etDescription.getText().toString());
        addNewSpendRequest.setValue(Double.parseDouble(etValue.getText().toString()));
        addNewSpendRequest.setType(spnType.getSelectedItem().toString().equals("Crédito") ? "credit" : "debit");

        ApiRequest apiRequest = new ApiRequest();

        AddNewSpendUseCase addNewSpendUseCase = new AddNewSpendUseCase(ThreadExecutor.getInstance(), apiRequest, addNewSpendRequest, userToken);
        addNewSpendUseCase.setCallback(new AddNewSpendUseCase.OnAddNewSpendCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode) {
                Toast.makeText(context, "Falha ao salvar o registro, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });

        addNewSpendUseCase.execute();
    }

    private void getDataFromSharedPreferences(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        userToken = sharedPreferencesUtils.getUserToken(context);
    }
}

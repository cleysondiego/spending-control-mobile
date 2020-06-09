package br.com.spendingcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.spendingcontrol.R;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;

public class SpendingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
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

        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        deleteItem();
    }

    public void deleteItem() {
        // CALL API TO DELETE THIS SPEND.
        // finish();
    }

    public void fillEt() {
        etTitleDetails.setText(spend.getTitle());
        etDescriptionDetails.setText(spend.getDescription());
        etValueDetails.setText(spend.getValue().toString());
        etTypeDetails.setText(spend.getType().equals("credit") ? "Crédito" : "Débito");
    }
}

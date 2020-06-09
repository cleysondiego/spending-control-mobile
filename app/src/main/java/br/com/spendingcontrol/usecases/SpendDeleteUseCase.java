package br.com.spendingcontrol.usecases;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;

import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;

public class SpendDeleteUseCase extends UseCaseAbstract {
    public interface OnSpendDeleteCallback {
        void onSuccess();

        void onFailure(int statusCode);
    }

    private SpendDeleteUseCase.OnSpendDeleteCallback callback;

    private GetSpendingsResponseStructure.Spendings spendings;
    private ApiRequest apiRequest;
    private String token;

    public SpendDeleteUseCase(Executor executor,
                              GetSpendingsResponseStructure.Spendings spendings,
                              ApiRequest apiRequest,
                              String token) {
        super(executor);
        this.spendings = spendings;
        this.apiRequest = apiRequest;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            apiRequest.delete(ApiRequest.BASE_URL + "/spend/" + spendings.getId(), headers, null, new ApiRequest.OnResponse() {
                @Override
                public void onResponse(int statusCode, byte[] response) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });
                }

                @Override
                public void onFailure(final int statusCode) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(statusCode);
                        }
                    });
                }
            });

        } catch (Exception ignored) {
            callback.onFailure(400);
        }
    }

    public void setCallback(SpendDeleteUseCase.OnSpendDeleteCallback callback) {
        this.callback = callback;
    }
}

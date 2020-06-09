package br.com.spendingcontrol.usecases;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;

import br.com.spendingcontrol.utils.AddNewSpendRequest;
import br.com.spendingcontrol.utils.ApiRequest;

public class AddNewSpendUseCase extends UseCaseAbstract {
    public interface OnAddNewSpendCallback {
        void onSuccess();

        void onFailure(int statusCode);
    }

    private AddNewSpendUseCase.OnAddNewSpendCallback callback;

    private ApiRequest apiRequest;
    private AddNewSpendRequest addNewSpendRequest;
    private String token;

    public AddNewSpendUseCase(Executor executor,
                              ApiRequest apiRequest,
                              AddNewSpendRequest addNewSpendRequest,
                              String token) {
        super(executor);

        this.apiRequest = apiRequest;
        this.addNewSpendRequest = addNewSpendRequest;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(ApiRequest.CONTENT_TYPE, "application/json");
            headers.put("Authorization", "Bearer " + token);

            String addNewSpendParams = addNewSpendRequest.getEventString();

            apiRequest.post(ApiRequest.BASE_URL + "/spend", headers, addNewSpendParams, new ApiRequest.OnResponse() {
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

    public void setCallback(AddNewSpendUseCase.OnAddNewSpendCallback callback) {
        this.callback = callback;
    }
}

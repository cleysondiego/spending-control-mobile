package br.com.spendingcontrol.usecases;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;

import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.RegisterRequest;

public class RegisterUseCase extends UseCaseAbstract {
    public interface OnRegisterCallback {
        void onSuccess();

        void onFailure(int statusCode);
    }

    private RegisterUseCase.OnRegisterCallback callback;

    private ApiRequest apiRequest;
    private RegisterRequest registerRequest;

    public RegisterUseCase(Executor executor,
                           ApiRequest apiRequest,
                           RegisterRequest registerRequest) {
        super(executor);

        this.apiRequest = apiRequest;
        this.registerRequest = registerRequest;
    }

    @Override
    public void run() {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(ApiRequest.CONTENT_TYPE, "application/json");

            String requestParams = registerRequest.getEventString();

            apiRequest.post(ApiRequest.BASE_URL + "/users", headers, requestParams, new ApiRequest.OnResponse() {
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

    public void setCallback(RegisterUseCase.OnRegisterCallback callback) {
        this.callback = callback;
    }
}

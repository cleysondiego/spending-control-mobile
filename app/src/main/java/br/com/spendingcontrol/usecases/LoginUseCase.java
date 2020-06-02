package br.com.spendingcontrol.usecases;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.LoginRequest;
import br.com.spendingcontrol.utils.LoginResponseStructure;
import br.com.spendingcontrol.utils.SharedPreferencesUtils;

public class LoginUseCase extends UseCaseAbstract {

    public interface OnLoginCallback{
        void onSuccess();

        void onFailure(int statusCode);
    }

    private LoginUseCase.OnLoginCallback callback;

    private Context context;
    private ApiRequest apiRequest;
    private LoginRequest loginRequest;

    public LoginUseCase(Executor executor,
                        Context context,
                        ApiRequest apiRequest,
                        LoginRequest loginRequest) {
        super(executor);

        this.context = context;
        this.apiRequest = apiRequest;
        this.loginRequest = loginRequest;
    }

    @Override
    public void run() {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(ApiRequest.CONTENT_TYPE, "application/json");

            String requestParams = loginRequest.getEventString();

            apiRequest.post(ApiRequest.BASE_URL + "/sessions", headers, requestParams, new ApiRequest.OnResponse() {
                @Override
                public void onResponse(final int statusCode, final byte[] response) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LoginResponseStructure loginResponseStructure = new LoginResponseStructure().fromJson(new JSONObject(new String(response)));
                                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
                                sharedPreferencesUtils.setLoggedIn(context, true, loginResponseStructure.getToken(), loginResponseStructure.getUser().getName());
                                callback.onSuccess();
                            } catch (JSONException jsonException) {
                                callback.onFailure(100);
                            }
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
        } catch (Exception exception) {
            callback.onFailure(400);
        }
    }

    public void setCallback(LoginUseCase.OnLoginCallback callback) {
        this.callback = callback;
    }
}

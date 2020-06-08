package br.com.spendingcontrol.usecases;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.spendingcontrol.utils.ApiRequest;
import br.com.spendingcontrol.utils.GetSpendingsResponseStructure;

public class GetSpendingsUseCase extends UseCaseAbstract {
    public interface OnGetSpendingsCallback {
        void onSuccess(GetSpendingsResponseStructure getSpendingsResponseStructure);

        void onFailure(int statusCode);
    }

    private GetSpendingsUseCase.OnGetSpendingsCallback callback;

    private ApiRequest apiRequest;
    private String token;

    public GetSpendingsUseCase(Executor executor,
                               ApiRequest apiRequest,
                               String token) {
        super(executor);

        this.apiRequest = apiRequest;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            apiRequest.get(ApiRequest.BASE_URL + "/spend", headers, null, new ApiRequest.OnResponse() {
                @Override
                public void onResponse(int statusCode, final byte[] response) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GetSpendingsResponseStructure getSpendingsResponseStructure = new GetSpendingsResponseStructure().fromJson(new JSONObject(new String(response)));
                                callback.onSuccess(getSpendingsResponseStructure);
                            } catch (JSONException ignored) {
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
        } catch (Exception ignored) {
            callback.onFailure(400);
        }
    }

    public void setCallback(GetSpendingsUseCase.OnGetSpendingsCallback callback) {
        this.callback = callback;
    }
}

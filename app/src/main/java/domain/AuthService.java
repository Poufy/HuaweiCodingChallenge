package domain;

import android.content.Context;

import com.example.huaweicodingchallenge.ui.HuaweiLoginActivity;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class AuthService {
    public HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;
    private static AuthService service_instance = null;
    private static Context ctx;

    private AuthService(){
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();

        mAuthManager = HuaweiIdAuthManager.getService(ctx, mAuthParam);
    }

    public static void setContext(Context ctx){
        AuthService.ctx = ctx;
    }

    public static AuthService getInstance(){
        if(service_instance == null)
            service_instance = new AuthService();
        return service_instance;
    }

}

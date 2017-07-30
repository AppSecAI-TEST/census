package census.com.census;

import android.text.TextUtils;

public class LoginPresenterImpl implements LoginPresenter,LoginModel.OnLoginListener{

    LoginView loginView;
    LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    @Override
    public void checkCredentials(String username, String password) {
        if(TextUtils.isEmpty(username)){
            loginView.setErrorUsername("This is required!");
            return;
        }

        if(!isEmail(username)){
            loginView.setErrorUsername("Invalid email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            loginView.setErrorPassword("This is required");
            return;
        }

        loginModel.login(username,password,this);
    }

    private boolean isEmail(String username){
        return username.contains("@");
    }

    @Override
    public void onSuccess() {
        loginView.onSuccess();
    }

    @Override
    public void onErrorPassword(String message) {
        loginView.setErrorPassword("Wrong password!");
    }
}
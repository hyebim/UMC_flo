package umcandroid.essential.week02_flo_1.data.remote

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umcandroid.essential.week02_flo_1.data.entities.LoginRequest
import umcandroid.essential.week02_flo_1.ui.signin.LoginView
import umcandroid.essential.week02_flo_1.ui.signup.SignUpView
import umcandroid.essential.week02_flo_1.data.entities.User
import umcandroid.essential.week02_flo_1.utils.getRetrofit

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())

                val resp = response.body()
                if (resp != null) {
                    when (resp.code) {
                        "COMMON200" -> signUpView.onSignUpSuccess()
                        else -> signUpView.onSignUpFailure()
                    }
                } else {
                    Log.e("SIGNUP/ERROR", "Response body is null")
                    signUpView.onSignUpFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })
        Log.d("SIGNUP", "HELLO")
    }

    fun login(request: LoginRequest) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())

                val resp = response.body()
                if (resp != null) {
                    if (resp.code == "COMMON200" && resp.result != null) {
                        loginView.onLoginSuccess(resp.code, resp.result)
                    } else {
                        loginView.onLoginFailure(resp.message)
                    }
                } else {
                    Log.e("LOGIN/ERROR", "Response body is null")
                    loginView.onLoginFailure("응답이 없습니다.")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
                loginView.onLoginFailure("네트워크 오류: ${t.message}")
            }
        })
        Log.d("LOGIN", "HELLO")
    }
}
package umcandroid.essential.week02_flo_1.ui.signin

import umcandroid.essential.week02_flo_1.data.remote.Result

interface LoginView {
    fun onLoginSuccess(code: String, result: Result)
    fun onLoginFailure(message : String)
}
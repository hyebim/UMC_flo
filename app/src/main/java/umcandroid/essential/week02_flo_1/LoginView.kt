package umcandroid.essential.week02_flo_1

interface LoginView {
    fun onLoginSuccess(code: String, result: Result)
    fun onLoginFailure(message : String)
}
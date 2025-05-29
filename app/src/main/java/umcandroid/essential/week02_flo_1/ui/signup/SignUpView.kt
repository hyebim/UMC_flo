package umcandroid.essential.week02_flo_1.ui.signup

//activity와 AuthService 연결
interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure()
}
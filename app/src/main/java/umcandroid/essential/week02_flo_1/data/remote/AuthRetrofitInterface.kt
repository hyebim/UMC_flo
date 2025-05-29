package umcandroid.essential.week02_flo_1.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import umcandroid.essential.week02_flo_1.data.entities.LoginRequest
import umcandroid.essential.week02_flo_1.data.entities.User

interface AuthRetrofitInterface {
    @POST("/join")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}
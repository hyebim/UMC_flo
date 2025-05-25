package umcandroid.essential.week02_flo_1

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://aos.inyro.site"

fun getRetrofit(): Retrofit {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY // 요청/응답 본문까지 모두 로그 찍음

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)  // 여기에 OkHttpClient 넣기
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

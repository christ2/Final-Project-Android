package edu.cs371m.triviagame.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("/api.php?amount=5&category=18&type=multiple")
    suspend fun getQuestions(@Query("difficulty") level: String) : TriviaResponse

    data class TriviaResponse(val results: List<TriviaQuestion>)

    companion object {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("opentdb.com")
            .build()

        fun create(): TriviaApi = create(url)
        private fun create(httpUrl: HttpUrl): TriviaApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TriviaApi::class.java)
        }
    }
}
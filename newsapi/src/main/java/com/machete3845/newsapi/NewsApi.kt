package com.machete3845.newsapi

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.machete3845.newsapi.models.Article
import com.machete3845.newsapi.models.Language
import com.machete3845.newsapi.models.Response
import com.machete3845.newsapi.models.SortBy
import com.machete3845.newsapi.utils.NewsApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * [API docs](https://newsapi.org/docs/get-started)
 */
interface NewsApi {

    /**
     * API details [here](https://newsapi.org/v2/everything)
     */
    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("from") to: Date? = null,
        @Query("language") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 999) pageSize: Int = 999,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ) : Result<Response<Article>>

}

fun NewsApi(
    apiKey: String,
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): NewsApi {
    val retrofit = retrofit(apiKey, baseUrl, okHttpClient, json)
    return retrofit.create()
}

private fun retrofit(
    apiKey: String,
    baseUrl: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory(MediaType.get("application/json"))

    val modifiedOkHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
    return retrofit
}


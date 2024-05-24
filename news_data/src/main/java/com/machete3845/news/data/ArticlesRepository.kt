package com.machete3845.news.data

import com.machete3845.news.data.models.Article
import com.machete3845.news.database.NewsDatabase
import com.machete3845.news.database.models.ArticleDBO
import com.machete3845.newsapi.NewsApi
import com.machete3845.newsapi.models.ArticleDTO
import com.machete3845.newsapi.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) {


    fun getAll(requestResponseMergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResultMergeStrategy()): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> =
            getAllFromDatabase()
        val remoteArticles: Flow<RequestResult<List<Article>>> =
            getAllFromServer()

        return cachedAllArticles.combine(remoteArticles, requestResponseMergeStrategy::merge).flatMapLatest { result ->
            if (result is RequestResult.Success)
            {
                database.articlesDao.observeAll().map { dbos -> dbos.map { it.toArticle() } }.map { RequestResult.Success(it) }
            }
            else
            {
                flowOf(result)
            }
        }

    }

    private fun getAllFromServer(): Flow<RequestResult<List<Article>>> {

        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(checkNotNull(result.getOrThrow().articles))
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<Response<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start).map { result -> result.map { response -> response.articles.map { it.toArticle() } } }

    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO ->
            articleDTO.toArticleDBO()
        }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest: Flow<RequestResult<List<ArticleDBO>>> = database.articlesDao::getAll.asFlow().map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest).map { result -> result.map { articlesDBO -> articlesDBO.map { it.toArticle() } } }

    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }

    fun fetchLatest(): Flow<RequestResult<List<Article>>> {
        return getAllFromServer()
    }

}





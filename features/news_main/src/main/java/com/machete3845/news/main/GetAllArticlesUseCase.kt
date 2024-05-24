package com.machete3845.news.main

import com.machete3845.news.data.ArticlesRepository
import com.machete3845.news.data.RequestResult
import com.machete3845.news.data.RequestResultMergeStrategy
import com.machete3845.news.data.map
import com.machete3845.news.data.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {
    operator fun invoke(): Flow<RequestResult<List<com.machete3845.news.main.Article>>> {
        return repository.getAll().map{ requestResult ->
                requestResult.map { articles -> articles.map { it.toUiArticle() } }
            }

    }
    private fun Article.toUiArticle(): com.machete3845.news.main.Article {
        TODO("Not yet implemented")
    }

}
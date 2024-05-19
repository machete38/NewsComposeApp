package com.machete3845.news.main

import com.machete3845.news.data.ArticlesRepository
import com.machete3845.news.data.models.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {
    operator fun invoke(): Flow<Article> {
        return repository.getAll()
    }
}
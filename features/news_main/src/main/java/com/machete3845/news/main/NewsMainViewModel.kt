package com.machete3845.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machete3845.news.data.ArticlesRepository
import com.machete3845.news.data.RequestResult
import com.machete3845.news.data.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val repository: ArticlesRepository) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate(){
        val requestResultFlow = repository.fetchLatest()

    }

}



private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(checkNotNull(data))
    }
}

sealed class State {
    object None : State()
    class Loading(val articles: List<Article>? = null) : State()
    class Error(val articles: List<Article>? = null) : State()
    class Success(val articles: List<Article>) : State()
}
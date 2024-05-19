package com.machete3845.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.machete3845.news.database.dao.ArticleDAO
import com.machete3845.news.database.models.ArticleDBO
import com.machete3845.news.database.utils.Converters

class NewsDatabase internal constructor(private val database: NewsRoomDatabase){
  val articlesDao: ArticleDAO
      get() = database.articlesDao()
}
@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase(){
    abstract fun articlesDao(): ArticleDAO
}

fun NewsDatabase(applicationContext: Context): NewsDatabase
{
    val db = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDatabase::class.java,
        "news"
    ).build()
    return NewsDatabase(db)
}
package com.phicdy.mycuration.articlelist.action

import com.phicdy.mycuration.core.Action
import com.phicdy.mycuration.entity.Article

sealed class ArticleListAction<out T> : Action<T>

data class FetchArticleAction(
        override val value: List<Article>
) : ArticleListAction<List<Article>>()

data class ReadArticleAction(
        override val value: Int
) : ArticleListAction<Int>()

data class FinishAction(
        override val value: Unit
) : ArticleListAction<Unit>()

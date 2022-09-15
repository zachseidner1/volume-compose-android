package com.cornellappdev.volume.data.repositories

import com.cornellappdev.volume.*
import com.cornellappdev.volume.data.NetworkApi
import com.cornellappdev.volume.data.models.Article
import com.cornellappdev.volume.data.models.Publication
import com.cornellappdev.volume.data.models.Social
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Article Repository. Encapsulate the logic required to access data sources, particularly
 * those depending on the Article model and mutations/queries on Articles.
 *
 * @see Article
 */
@Singleton
class ArticleRepository @Inject constructor(private val networkApi: NetworkApi) {
    suspend fun fetchAllArticles(limit: Double? = null): List<Article> =
        networkApi.fetchAllArticles(limit).dataAssertNoErrors.mapDataToArticles()

    suspend fun fetchTrendingArticles(limit: Double? = null): List<Article> =
        networkApi.fetchTrendingArticles(limit).dataAssertNoErrors.mapDataToArticles()

    suspend fun fetchArticlesByPublicationID(pubID: String): List<Article> =
        networkApi.fetchArticleByPublicationID(pubID).dataAssertNoErrors.mapDataToArticles()

    suspend fun fetchArticlesByPublicationIDs(pubIDs: List<String>): List<Article> =
        networkApi.fetchArticlesByPublicationIDs(pubIDs).dataAssertNoErrors.mapDataToArticles()

    suspend fun fetchArticlesByIDs(ids: List<String>): List<Article> =
        networkApi.fetchArticlesByIDs(ids).dataAssertNoErrors.mapDataToArticles()

    suspend fun fetchArticleByID(id: String): Article =
        networkApi.fetchArticleByID(id).dataAssertNoErrors.mapDataToArticles().first()

    suspend fun incrementShoutout(id: String, uuid: String): IncrementShoutoutMutation.Data =
        networkApi.incrementShoutout(id, uuid).dataAssertNoErrors

    private fun AllArticlesQuery.Data.mapDataToArticles(): List<Article> {
        return this.getAllArticles.map { articleData ->
            val publication = articleData.publication
            Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            )
        }
    }

    private fun TrendingArticlesQuery.Data.mapDataToArticles(): List<Article> {
        return this.getTrendingArticles.map { articleData ->
            val publication = articleData.publication
            Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            )
        }
    }

    private fun ArticlesByPublicationIDQuery.Data.mapDataToArticles(): List<Article> {
        return this.getArticlesByPublicationID.map { articleData ->
            val publication = articleData.publication
            Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            )
        }
    }

    private fun ArticlesByPublicationIDsQuery.Data.mapDataToArticles(): List<Article> {
        return this.getArticlesByPublicationIDs.map { articleData ->
            val publication = articleData.publication
            Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            )
        }
    }

    private fun ArticlesByIDsQuery.Data.mapDataToArticles(): List<Article> {
        return this.getArticlesByIDs.map { articleData ->
            val publication = articleData.publication
            Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            )
        }
    }

    private fun ArticleByIDQuery.Data.mapDataToArticles(): List<Article> {
        return this.getArticleByID?.let { articleData ->
            val publication = articleData.publication
            listOf(Article(
                title = articleData.title,
                articleURL = articleData.articleURL,
                date = articleData.date.toString(),
                id = articleData.id,
                imageURL = articleData.imageURL,
                publication = Publication(
                    id = publication.id,
                    backgroundImageURL = publication.backgroundImageURL,
                    bio = publication.bio,
                    name = publication.name,
                    profileImageURL = publication.profileImageURL,
                    rssName = publication.rssName,
                    rssURL = publication.rssURL,
                    slug = publication.slug,
                    shoutouts = publication.shoutouts,
                    websiteURL = publication.websiteURL,
                    socials = publication.socials
                        .map { Social(it.social, it.url) }
                ),
                shoutouts = articleData.shoutouts,
                nsfw = articleData.nsfw
            ))
        } ?: listOf()
    }
}

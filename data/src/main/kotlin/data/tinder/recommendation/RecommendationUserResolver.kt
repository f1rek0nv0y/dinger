package data.tinder.recommendation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.database.SQLException
import domain.recommendation.DomainRecommendationUser
import reporter.CrashReporter

internal class RecommendationUserResolver(
        private val userDao: RecommendationUserDao,
        private val commonFriendDaoDelegate: RecommendationCommonFriendDaoDelegate,
        private val instagramDaoDelegate: RecommendationInstagramDaoDelegate,
        private val likeDaoDelegate: RecommendationLikeDaoDelegate,
        private val photoDaoDelegate: RecommendationPhotoDaoDelegate,
        private val jobDaoDelegate: RecommendationJobDaoDelegate,
        private val schoolDaoDelegate: RecommendationSchoolDaoDelegate,
        private val teaserDaoDelegate: RecommendationTeaserDaoDelegate,
        private val spotifyThemeTrackDaoDelegate: RecommendationSpotifyThemeTrackDaoDelegate,
        private val crashReporter: CrashReporter) {
    fun insert(user: DomainRecommendationUser) =
            try {
                user.apply {
                    instagramDaoDelegate.insertResolved(instagram)
                    teaserDaoDelegate.insertResolved(teaser)
                    spotifyThemeTrackDaoDelegate.insertResolved(spotifyThemeTrack)
                    commonFriendDaoDelegate.insertResolvedForUserId(id, commonFriends)
                    likeDaoDelegate.insertResolvedForUserId(id, commonLikes)
                    photoDaoDelegate.insertResolvedForUserId(id, photos)
                    jobDaoDelegate.insertResolvedForUserId(id, jobs)
                    schoolDaoDelegate.insertResolvedForUserId(id, schools)
                    teaserDaoDelegate.insertResolvedForUserId(id, teasers)
                    userDao.insertUser(RecommendationUserEntity(
                            bio = bio,
                            distanceMiles = distanceMiles,
                            commonFriendCount = commonFriendCount,
                            commonLikeCount = commonLikeCount,
                            contentHash = contentHash,
                            id = id,
                            birthDate = birthDate,
                            name = name,
                            instagram = instagram?.username,
                            teaser = teaser.id,
                            sNumber = sNumber,
                            spotifyThemeTrack = spotifyThemeTrack?.id,
                            gender = gender,
                            birthDateInfo = birthDateInfo,
                            groupMatched = groupMatched,
                            liked = liked,
                            matched = matched
                    ))
                }
            } catch (sqlException: SQLException) {
                crashReporter.report(sqlException)
            }

    fun selectById(id: String): LiveData<List<DomainRecommendationUser>> =
            Transformations.map(userDao.selectUserById(id)) { it.map { from(it) } }

    fun selectByFilterOnName(filter: String)
            : DataSource.Factory<Int, DomainRecommendationUser> =
            userDao.selectUsersByFilterOnName(filter).map { from(it) }

    private fun from(source: RecommendationUserWithRelatives): DomainRecommendationUser {
        val commonFriends =
                commonFriendDaoDelegate.collectByPrimaryKeys(source.commonFriends)
        val commonLikes = likeDaoDelegate.collectByPrimaryKeys(source.commonLikes)
        val photos = photoDaoDelegate.collectByPrimaryKeys(source.photos)
        val jobs = jobDaoDelegate.collectByPrimaryKeys(source.jobs)
        val schools = schoolDaoDelegate.collectByPrimaryKeys(source.schools)
        val teasers = teaserDaoDelegate.collectByPrimaryKeys(source.teasers)
        source.recommendationUserEntity.let {
            return DomainRecommendationUser(
                    bio = it.bio,
                    distanceMiles = it.distanceMiles,
                    commonFriends = commonFriends,
                    commonFriendCount = it.commonFriendCount,
                    commonLikes = commonLikes,
                    commonLikeCount = it.commonLikeCount,
                    contentHash = it.contentHash,
                    id = it.id,
                    birthDate = it.birthDate,
                    name = it.name,
                    instagram = instagramDaoDelegate.selectByPrimaryKey(it.instagram),
                    teaser = teaserDaoDelegate.selectByPrimaryKey(it.teaser),
                    sNumber = it.sNumber,
                    spotifyThemeTrack =
                    spotifyThemeTrackDaoDelegate.selectByPrimaryKey(it.spotifyThemeTrack),
                    gender = it.gender,
                    birthDateInfo = it.birthDateInfo,
                    groupMatched = it.groupMatched,
                    liked = it.liked,
                    matched = it.matched,
                    photos = photos,
                    jobs = jobs,
                    schools = schools,
                    teasers = teasers)
        }
    }
}

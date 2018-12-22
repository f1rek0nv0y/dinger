package data.tinder.recommendation

import data.ObjectMapper
import domain.DomainException
import domain.recommendation.DomainRecommendationCommonFriend
import domain.recommendation.DomainRecommendationCommonFriendPhoto
import domain.recommendation.DomainRecommendationCompany
import domain.recommendation.DomainRecommendationInstagram
import domain.recommendation.DomainRecommendationInstagramPhoto
import domain.recommendation.DomainRecommendationJob
import domain.recommendation.DomainRecommendationLike
import domain.recommendation.DomainRecommendationPhoto
import domain.recommendation.DomainRecommendationProcessedFile
import domain.recommendation.DomainRecommendationSchool
import domain.recommendation.DomainRecommendationSpotifyAlbum
import domain.recommendation.DomainRecommendationSpotifyArtist
import domain.recommendation.DomainRecommendationSpotifyThemeTrack
import domain.recommendation.DomainRecommendationTeaser
import domain.recommendation.DomainRecommendationTitle
import domain.recommendation.DomainRecommendationUser

internal class RecommendationResponseObjectMapper(
    private val eventTracker: RecommendationEventTracker,
    private val commonFriendDelegate
    : ObjectMapper<RecommendationUserCommonFriend, DomainRecommendationCommonFriend>,
    private val instagramDelegate
    : ObjectMapper<RecommendationUserInstagram?, DomainRecommendationInstagram?>,
    private val teaserDelegate
    : ObjectMapper<RecommendationUserTeaser, DomainRecommendationTeaser>,
    private val spotifyThemeTrackDelegate
    : ObjectMapper<RecommendationUserSpotifyThemeTrack?, DomainRecommendationSpotifyThemeTrack?>,
    private val commonLikeDelegate
    : ObjectMapper<RecommendationLike, DomainRecommendationLike>,
    private val photoDelegate
    : ObjectMapper<RecommendationUserPhoto, DomainRecommendationPhoto>,
    private val jobDelegate: ObjectMapper<RecommendationUserJob, DomainRecommendationJob>,
    private val schoolDelegate: ObjectMapper<RecommendationUserSchool,
        DomainRecommendationSchool>)
  : ObjectMapper<RecommendationResponse, List<DomainRecommendationUser>> {
  override fun from(source: RecommendationResponse): List<DomainRecommendationUser> =
      source.let {
        eventTracker.track(it)
        return it.recommendations.let {
          when (it) {
            null -> throw when (source.message) {
              is String -> DomainException(source.message)
              else -> IllegalStateException(
                  "Unexpected 2xx (${source.status}) recommendation response without message." +
                      "Array size: ${source.recommendations?.size ?: 0}")
            }
            else -> it.map { transformRecommendation(it) }
          }
        }
      }


  private fun transformRecommendation(source: Recommendation) = DomainRecommendationUser(
      bio = source.bio,
      distanceMiles = source.distanceMiles,
      commonFriendCount = source.commonFriendCount,
      commonFriends = source.commonFriends.map {
        commonFriendDelegate.from(it)
      },
      commonLikeCount = source.commonLikeCount,
      commonLikes = source.commonLikes.map {
        commonLikeDelegate.from(it)
      },
      id = source.id,
      birthDate = source.birthDate,
      name = source.name,
      instagram = instagramDelegate.from(source.instagram),
      teaser = teaserDelegate.from(source.teaser),
      spotifyThemeTrack = spotifyThemeTrackDelegate.from(source.spotifyThemeTrack),
      gender = source.gender,
      birthDateInfo = source.birthDateInfo,
      contentHash = source.contentHash,
      groupMatched = source.groupMatched,
      sNumber = source.sNumber,
      photos = source.photos.map { photoDelegate.from(it) },
      jobs = source.jobs.map { jobDelegate.from(it) },
      schools = source.schools.map { schoolDelegate.from(it) },
      teasers = source.teasers.map { teaserDelegate.from(it) })
}

internal class RecommendationUserCommonFriendObjectMapper(
    private val commonFriendPhotoDelegate
    : ObjectMapper<RecommendationUserCommonFriendPhoto, DomainRecommendationCommonFriendPhoto>)
  : ObjectMapper<RecommendationUserCommonFriend, DomainRecommendationCommonFriend> {
  override fun from(source: RecommendationUserCommonFriend) =
      DomainRecommendationCommonFriend(
          id = source.id,
          name = source.name,
          degree = source.degree,
          photos = source.photos?.map { commonFriendPhotoDelegate.from(it) })
}

internal class RecommendationUserCommonFriendPhotoObjectMapper
  : ObjectMapper<RecommendationUserCommonFriendPhoto, DomainRecommendationCommonFriendPhoto> {
  override fun from(source: RecommendationUserCommonFriendPhoto) =
      DomainRecommendationCommonFriendPhoto(
          small = source.small,
          medium = source.medium,
          large = source.large)
}

internal class RecommendationInstagramObjectMapper(
    private val instagramPhotoDelegate
    : ObjectMapper<RecommendationUserInstagramPhoto, DomainRecommendationInstagramPhoto>)
  : ObjectMapper<RecommendationUserInstagram?, DomainRecommendationInstagram?> {
  override fun from(source: RecommendationUserInstagram?) = source?.let {
    DomainRecommendationInstagram(
        profilePictureUrl = it.profilePictureUrl,
        lastFetchTime = it.lastFetchTime,
        mediaCount = it.mediaCount,
        completedInitialFetch = it.completedInitialFetch,
        username = it.username,
        photos = it.photos?.map { instagramPhotoDelegate.from(it) })
  }
}

internal class RecommendationInstagramPhotoObjectMapper
  : ObjectMapper<RecommendationUserInstagramPhoto, DomainRecommendationInstagramPhoto> {
  override fun from(source: RecommendationUserInstagramPhoto) =
      DomainRecommendationInstagramPhoto(
          link = source.link,
          imageUrl = source.imageUrl,
          thumbnailUrl = source.thumbnailUrl,
          ts = source.ts)
}

internal class RecommendationTeaserObjectMapper
  : ObjectMapper<RecommendationUserTeaser, DomainRecommendationTeaser> {
  override fun from(source: RecommendationUserTeaser) =
      DomainRecommendationTeaser(
          id = RecommendationUserTeaserEntity.createId(
              description = source.description,
              type = source.type),
          description = source.description,
          type = source.type)
}

internal class RecommendationSpotifyThemeTrackObjectMapper(
    private val albumDelegate
    : ObjectMapper<RecommendationUserSpotifyThemeTrackAlbum, DomainRecommendationSpotifyAlbum>,
    private val artistDelegate
    : ObjectMapper<RecommendationUserSpotifyThemeTrackArtist, DomainRecommendationSpotifyArtist>)
  : ObjectMapper<RecommendationUserSpotifyThemeTrack?, DomainRecommendationSpotifyThemeTrack?> {
  override fun from(source: RecommendationUserSpotifyThemeTrack?) = source?.let {
    DomainRecommendationSpotifyThemeTrack(
        album = albumDelegate.from(it.album),
        artists = it.artists.map { artistDelegate.from(it) },
        id = it.id,
        name = it.name,
        previewUrl = it.previewUrl,
        uri = it.uri)
  }
}

internal class RecommendationSpotifyThemeTrackAlbumObjectMapper(
    private val processedFileDelegate
    : ObjectMapper<RecommendationUserPhotoProcessedFile, DomainRecommendationProcessedFile>)
  : ObjectMapper<RecommendationUserSpotifyThemeTrackAlbum, DomainRecommendationSpotifyAlbum> {
  override fun from(source: RecommendationUserSpotifyThemeTrackAlbum) =
      DomainRecommendationSpotifyAlbum(
          id = source.id,
          name = source.name,
          images = source.images.map { processedFileDelegate.from(it) })
}

internal class RecommendationProcessedFileObjectMapper
  : ObjectMapper<RecommendationUserPhotoProcessedFile, DomainRecommendationProcessedFile> {
  override fun from(source: RecommendationUserPhotoProcessedFile) =
      DomainRecommendationProcessedFile(
          widthPx = source.widthPx,
          url = source.url,
          heightPx = source.heightPx)
}

internal class RecommendationSpotifyThemeTrackArtistObjectMapper
  : ObjectMapper<RecommendationUserSpotifyThemeTrackArtist, DomainRecommendationSpotifyArtist> {
  override fun from(source: RecommendationUserSpotifyThemeTrackArtist) =
      DomainRecommendationSpotifyArtist(id = source.id, name = source.name)
}

internal class RecommendationLikeObjectMapper
  : ObjectMapper<RecommendationLike, DomainRecommendationLike> {
  override fun from(source: RecommendationLike) =
      DomainRecommendationLike(id = source.id, name = source.name)
}

internal class RecommendationPhotoObjectMapper(
    private val processedFileDelegate
    : ObjectMapper<RecommendationUserPhotoProcessedFile, DomainRecommendationProcessedFile>)
  : ObjectMapper<RecommendationUserPhoto, DomainRecommendationPhoto> {
  override fun from(source: RecommendationUserPhoto) = DomainRecommendationPhoto(
      id = source.id,
      url = source.url,
      processedFiles = source.processedFiles.map { processedFileDelegate.from(it) })
}

internal class RecommendationJobObjectMapper(
    private val companyDelegate: ObjectMapper<RecommendationUserJobCompany, DomainRecommendationCompany>,
    private val titleDelegate: ObjectMapper<RecommendationUserJobTitle, DomainRecommendationTitle>)
  : ObjectMapper<RecommendationUserJob, DomainRecommendationJob> {
  override fun from(source: RecommendationUserJob) = DomainRecommendationJob(
      id = RecommendationUserJob.createId(source.company, source.title),
      company = source.company?.let { companyDelegate.from(it) },
      title = source.title?.let { titleDelegate.from(it) })
}

internal class RecommendationJobCompanyObjectMapper
  : ObjectMapper<RecommendationUserJobCompany, DomainRecommendationCompany> {
  override fun from(source: RecommendationUserJobCompany) = DomainRecommendationCompany(
      name = source.name
  )
}

internal class RecommendationJobTitleObjectMapper
  : ObjectMapper<RecommendationUserJobTitle, DomainRecommendationTitle> {
  override fun from(source: RecommendationUserJobTitle) = DomainRecommendationTitle(
      name = source.name
  )
}

internal class RecommendationSchoolObjectMapper
  : ObjectMapper<RecommendationUserSchool, DomainRecommendationSchool> {
  override fun from(source: RecommendationUserSchool) = DomainRecommendationSchool(
      id = source.id,
      name = source.name)
}

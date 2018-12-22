package data.tinder.recommendation

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.Lazy as DaggerLazy

@Module(includes = [RecommendationSourceModule::class, RecommendationEventTrackerModule::class])
internal class GetRecommendationFacadeModule {
  @Provides
  @Singleton
  fun requestObjectMapper() = RecommendationRequestObjectMapper()

  @Provides
  @Singleton
  fun commonFriendPhotoObjectMapper() = RecommendationUserCommonFriendPhotoObjectMapper()

  @Provides
  @Singleton
  fun commonFriendObjectMapper(
      photoDelegate: RecommendationUserCommonFriendPhotoObjectMapper) =
      RecommendationUserCommonFriendObjectMapper(photoDelegate)

  @Provides
  @Singleton
  fun instagramPhotoObjectMapper() = RecommendationInstagramPhotoObjectMapper()

  @Provides
  @Singleton
  fun instagramObjectMapper(instagramPhotoDelegate: RecommendationInstagramPhotoObjectMapper) =
      RecommendationInstagramObjectMapper(instagramPhotoDelegate)

  @Provides
  @Singleton
  fun teaserObjectMapper() = RecommendationTeaserObjectMapper()

  @Provides
  @Singleton
  fun processedFileObjectMapper() = RecommendationProcessedFileObjectMapper()

  @Provides
  @Singleton
  fun spotifyAlbumObjectMapper(processedFileDelegate: RecommendationProcessedFileObjectMapper) =
      RecommendationSpotifyThemeTrackAlbumObjectMapper(processedFileDelegate)

  @Provides
  @Singleton
  fun spotifyArtistObjectMapper() = RecommendationSpotifyThemeTrackArtistObjectMapper()

  @Provides
  @Singleton
  fun spotifyThemeTrackObjectMapper(
      albumDelegate: RecommendationSpotifyThemeTrackAlbumObjectMapper,
      artistDelegate: RecommendationSpotifyThemeTrackArtistObjectMapper) =
      RecommendationSpotifyThemeTrackObjectMapper(
          albumDelegate = albumDelegate,
          artistDelegate = artistDelegate)

  @Provides
  @Singleton
  fun likeObjectMapper() = RecommendationLikeObjectMapper()

  @Provides
  @Singleton
  fun photoObjectMapper(
      processedFileDelegate: RecommendationProcessedFileObjectMapper) =
      RecommendationPhotoObjectMapper(processedFileDelegate)

  @Provides
  @Singleton
  fun jobCompanyObjectMapper() = RecommendationJobCompanyObjectMapper()

  @Provides
  @Singleton
  fun jobTitleObjectMapper() = RecommendationJobTitleObjectMapper()

  @Provides
  @Singleton
  fun jobObjectMapper(
      companyDelegate: RecommendationJobCompanyObjectMapper,
      titleDelegate: RecommendationJobTitleObjectMapper) =
      RecommendationJobObjectMapper(
          companyDelegate = companyDelegate,
          titleDelegate = titleDelegate)

  @Provides
  @Singleton
  fun schoolObjectMapper() = RecommendationSchoolObjectMapper()

  @Provides
  @Singleton
  fun responseObjectMapper(
      eventTracker: RecommendationEventTracker,
      commonFriendDelegate: RecommendationUserCommonFriendObjectMapper,
      instagramDelegate: RecommendationInstagramObjectMapper,
      teaserDelegate: RecommendationTeaserObjectMapper,
      spotifyThemeTrackDelegate: RecommendationSpotifyThemeTrackObjectMapper,
      commonLikeDelegate: RecommendationLikeObjectMapper,
      photoDelegate: RecommendationPhotoObjectMapper,
      jobDelegate: RecommendationJobObjectMapper,
      schoolDelegate: RecommendationSchoolObjectMapper) =
      RecommendationResponseObjectMapper(
          eventTracker = eventTracker,
          commonFriendDelegate = commonFriendDelegate,
          instagramDelegate = instagramDelegate,
          teaserDelegate = teaserDelegate,
          spotifyThemeTrackDelegate = spotifyThemeTrackDelegate,
          commonLikeDelegate = commonLikeDelegate,
          photoDelegate = photoDelegate,
          jobDelegate = jobDelegate,
          schoolDelegate = schoolDelegate)

  @Provides
  @Singleton
  fun facade(
      getRecommendationSource: GetRecommendationSource,
      requestObjectMapper: RecommendationRequestObjectMapper,
      responseObjectMapper: RecommendationResponseObjectMapper) =
      GetRecommendationFacade(getRecommendationSource, requestObjectMapper, responseObjectMapper)
}

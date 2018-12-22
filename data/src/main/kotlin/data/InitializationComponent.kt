package data

import dagger.Component
import data.account.AccountModule
import data.alarm.AlarmModule
import data.autoswipe.AutoSwipeModule
import data.autoswipe.AutoSwipeServiceDestructorModule
import data.network.NetworkModule
import data.seen.SeenRecommendationsModule
import data.storage.StorageClearModule
import data.stoyicker.versioncheck.VersionCheckModule
import data.tinder.dislike.DislikeRecommendationModule
import data.tinder.like.LikeRecommendationModule
import data.tinder.login.LoginModule
import data.tinder.recommendation.GetRecommendationModule
import javax.inject.Singleton

@Component(modules = [
  NetworkModule::class,
  LoginModule::class,
  GetRecommendationModule::class,
  SeenRecommendationsModule::class,
  LikeRecommendationModule::class,
  DislikeRecommendationModule::class,
  AccountModule::class,
  AlarmModule::class,
  AutoSwipeModule::class,
  VersionCheckModule::class,
  StorageClearModule::class,
  AutoSwipeServiceDestructorModule::class])
@Singleton
internal interface InitializationComponent {
  fun inject(initializationContentProvider: InitializationContentProvider)
}

package data.seen

import dagger.Module
import dagger.Provides
import data.tinder.recommendation.RecommendationUserResolver
import data.tinder.recommendation.RecommendationUserResolverModule
import javax.inject.Singleton

@Module(includes = [RecommendationUserResolverModule::class])
internal class SeenRecommendationsModule {
    @Provides
    @Singleton
    fun seenRecommendations(resolver: RecommendationUserResolver) =
            SeenRecommendationsImpl(resolver)
}

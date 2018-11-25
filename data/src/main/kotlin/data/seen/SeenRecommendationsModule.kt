package data.seen

import dagger.Module
import dagger.Provides
import data.tinder.recommendation.RecommendationUserResolver
import data.tinder.recommendation.RecommendationUserResolverModule
import domain.seen.SeenRecommendations
import javax.inject.Singleton

@Module(includes = [RecommendationUserResolverModule::class])
internal class SeenRecommendationsModule {
    @Provides
    @Singleton
    fun seenRecommendations(resolver: RecommendationUserResolver): SeenRecommendations =
            SeenRecommendationsImpl(resolver)
}

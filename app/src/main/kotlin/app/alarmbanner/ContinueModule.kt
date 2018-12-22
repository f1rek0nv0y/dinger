package app.alarmbanner

import android.view.View
import app.di.PerActivity
import dagger.Module
import dagger.Provides

@Module
@PerActivity
internal class ContinueModule(private val activity: AlarmBannerActivity, private val view: View) {
  @Provides
  fun view(): ContinueView = ContinueViewImpl(view)

  @Provides
  fun coordinator(view: ContinueView) = ContinueCoordinator(activity, view)
}

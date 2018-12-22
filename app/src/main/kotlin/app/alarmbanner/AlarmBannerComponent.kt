package app.alarmbanner

import app.di.PerActivity
import dagger.Subcomponent

@Subcomponent(modules = [AutoSwipeTriggerModule::class, ContinueModule::class])
@PerActivity
internal interface AlarmBannerComponent {
  fun inject(target: AlarmBannerActivity)
}

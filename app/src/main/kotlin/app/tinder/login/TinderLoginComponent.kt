package app.tinder.login

import app.di.PerActivity
import dagger.Subcomponent

@Subcomponent(modules = [TinderLoginModule::class])
@PerActivity
internal interface TinderLoginComponent {
  fun inject(target: TinderLoginActivity)
}

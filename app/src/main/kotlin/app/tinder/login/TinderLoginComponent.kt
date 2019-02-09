package app.tinder.login

import android.support.v4.widget.ContentLoadingProgressBar
import app.EntryScreenScope
import com.facebook.login.widget.LoginButton
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [TinderLoginModule::class])
@EntryScreenScope
internal interface TinderLoginComponent {
  fun inject(target: TinderLoginActivity)
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun activity(activity: TinderLoginActivity): Builder
    @BindsInstance
    fun loginButton(loginButton: LoginButton): Builder
    @BindsInstance
    fun contentLoadingProgressBar(contentLoadingProgressBar: ContentLoadingProgressBar): Builder
    @BindsInstance
    fun tinderFacebookLoginResultCallback(
        tinderFacebookLoginResultCallback: TinderFacebookLoginFeature.ResultCallback): Builder
    @BindsInstance
    fun tinderLoginCoordinatorResultCallback(
        tinderLoginCoordinatorResultCallback: TinderLoginCoordinator.ResultCallback): Builder
    fun build(): TinderLoginComponent
  }
}

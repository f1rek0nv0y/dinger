package app.alarmbanner

import android.content.Context
import android.view.View
import app.EntryScreenScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [AlarmBannerModule::class])
@EntryScreenScope
internal interface AlarmBannerComponent {
  fun inject(target: AlarmBannerActivity)
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    @BindsInstance
    fun view(view: View): Builder
    @BindsInstance
    fun continueResultCallback(callback: ContinueCoordinator.ResultCallback): Builder
    fun build(): AlarmBannerComponent
  }
}

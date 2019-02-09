package app.alarmbanner

import android.view.View
import app.EntryScreenScope
import dagger.Module
import dagger.Provides

@Module
internal class ContinueModule {
  @Provides
  @EntryScreenScope
  fun view(view: View): ContinueView = ContinueViewImpl(view)

  @Provides
  @EntryScreenScope
  fun coordinator(callback: ContinueCoordinator.ResultCallback, view: ContinueView) =
      ContinueCoordinator(callback, view)
}

package domain.autoswipe

object AutoSwipeHolder {
  internal lateinit var autoSwipeLauncherFactory: AutoSwipeLauncherFactory

  fun autoSwipeIntentFactory(
      autoSwipeLauncherFactory: AutoSwipeLauncherFactory) {
    this.autoSwipeLauncherFactory = autoSwipeLauncherFactory
  }
}

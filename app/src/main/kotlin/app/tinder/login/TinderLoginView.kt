package app.tinder.login

internal interface TinderLoginView {
  fun setRunning()

  fun setStale()

  fun setError()
}

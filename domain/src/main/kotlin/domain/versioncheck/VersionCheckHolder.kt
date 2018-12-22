package domain.versioncheck

object VersionCheckHolder {
  internal lateinit var versionCheck: VersionCheck

  fun versionCheck(it: VersionCheck) {
    versionCheck = it
  }
}

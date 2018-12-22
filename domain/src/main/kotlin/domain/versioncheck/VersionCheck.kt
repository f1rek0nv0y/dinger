package domain.versioncheck

import io.reactivex.Single

interface VersionCheck {
  fun versionCheck(): Single<DomainVersionCheckResponse>
}

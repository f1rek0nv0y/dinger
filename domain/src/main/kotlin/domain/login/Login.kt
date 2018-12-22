package domain.login

import io.reactivex.Single

interface Login {
  fun login(parameters: DomainAuthRequestParameters): Single<DomainAuthenticatedUser>
}

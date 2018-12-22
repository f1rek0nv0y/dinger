package domain.login

interface AccountManagement {
  fun updateOrAddAccount(facebookId: String, facebookToken: String, tinderApiKey: String): Boolean

  fun removeAccount()
}

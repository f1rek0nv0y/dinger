package domain.login

object LoginHolder {
  internal lateinit var login: Login
  internal lateinit var addAccount: AccountManagement

  fun login(it: Login) {
    login = it
  }

  fun addAccount(it: AccountManagement) {
    addAccount = it
  }
}

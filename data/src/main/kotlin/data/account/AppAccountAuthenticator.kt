package data.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import domain.loggedincheck.LoggedInCheckProvider
import domain.login.AccountManagementProvider
import org.stoyicker.dinger.data.R

internal class AppAccountAuthenticator(context: Context)
    : AccountManagementProvider, LoggedInCheckProvider, AbstractAccountAuthenticator(context) {
    private val delegate by lazy { AccountManager.get(context) }

    init { ACCOUNT_TYPE = context.getString(R.string.account_type) }

    override fun addAccount(
            p0: AccountAuthenticatorResponse?,
            p1: String?,
            p2: String?,
            p3: Array<out String>?,
            p4: Bundle?) = throw UnsupportedOperationException("Not supported")

    override fun getAuthTokenLabel(p0: String?) =
            throw UnsupportedOperationException("Not supported")

    override fun confirmCredentials(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: Bundle?) = throw UnsupportedOperationException("Not supported")

    override fun updateCredentials(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: String?,
            p3: Bundle?) = throw UnsupportedOperationException("Not supported")

    override fun getAuthToken(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: String?,
            p3: Bundle?) = throw UnsupportedOperationException("Not supported")

    override fun hasFeatures(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: Array<out String>?) = throw UnsupportedOperationException("Not supported")

    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?) =
            throw UnsupportedOperationException("Not supported")

    override fun addAccount(facebookId: String, facebookToken: String, tinderApiKey: String) =
            Account(facebookId, Companion.ACCOUNT_TYPE).let {
                if (delegate.addAccountExplicitly(it, tinderApiKey, null)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        delegate.notifyAccountAuthenticated(it)
                    }
                    true
                } else {
                    false
                }
            }

    override fun removeAccount() {
        delegate.getAccountsByType(ACCOUNT_TYPE).forEach {
            delegate.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    removeAccountExplicitly(it)
                } else {
                    @Suppress("DEPRECATION")
                    removeAccount(it, null, Handler(Looper.myLooper()))
                }
            }
        }
    }

    override fun isThereALoggedInUser() = getTinderAccountToken() != null

    fun getTinderAccountToken() = delegate.getAccountsByType(Companion.ACCOUNT_TYPE).let {
        when (it.size) {
            0 -> null
            else -> delegate.getPassword(it.first())
        }
    }

    private companion object {
        lateinit var ACCOUNT_TYPE: String
    }
}

package data

import data.account.AccountComponent
import data.autoswipe.AutoSwipeComponent
import data.tinder.TinderRepositoryComponent

internal object ComponentHolder {
    lateinit var accountComponent: AccountComponent
    lateinit var tinderRepositoryComponent: TinderRepositoryComponent
    lateinit var autoSwipeComponent: AutoSwipeComponent
}

package io.github.gubarsergey.accounting.navigation

import androidx.navigation.NavDestination
import io.github.gubarsergey.accounting.R
import java.lang.IllegalArgumentException

enum class NavProps {
    LOGIN,
    MAIN_FRAGMENT
}

sealed class ToolbarStatus {
    object Invisible : ToolbarStatus()
    data class Visible(val textRes: Int) : ToolbarStatus()
}

fun NavDestination.toToolbarStatus(): ToolbarStatus {
    return when (this.id) {
        R.id.loginFragment -> ToolbarStatus.Invisible
        R.id.nav_accounts, R.id.nav_settings -> ToolbarStatus.Visible(R.string.app_name)
        R.id.addAccountFragment -> ToolbarStatus.Visible(R.string.add_accounts)
        R.id.addCategoryFragment -> ToolbarStatus.Visible(R.string.add_category)
        R.id.addTransactionFragment -> ToolbarStatus.Visible(R.string.add_transaction)
        R.id.categoryTotalSpentFragment -> ToolbarStatus.Visible(R.string.categories_total_spent)
        R.id.timeRangeReportFragment -> ToolbarStatus.Visible(R.string.time_report)
        R.id.allTimeReportFragment -> ToolbarStatus.Visible(R.string.all_time_report)
        else -> throw IllegalArgumentException("Destination $this has no toolbar status!")
    }
}
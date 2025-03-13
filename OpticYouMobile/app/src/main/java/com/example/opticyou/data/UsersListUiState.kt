
package com.example.opticyou.data

/**
 * Data class that represents the current UI state in terms of list of users
 */
data class UsersListUiState(
    /** user list*/
    var list: List<User> = listOf()

)

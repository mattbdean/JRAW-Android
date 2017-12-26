package net.dean.jraw.android

import net.dean.jraw.http.UserAgent

/**
 * This class contains all the information required to authenticate an Android app using reddit's
 * OAuth2 authentication process.
 */
data class AppInfo(val clientId: String, val redirectUrl: String, val userAgent: UserAgent)

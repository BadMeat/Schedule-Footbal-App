package com.example.user.sceball

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Bencoleng on 27/11/2018.
 */
open class CoroutineContextProvider {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
}
package com.pichu.nanamare.di

import com.pichu.nanamare.utils.AssetFileReader
import com.pichu.nanamare.utils.PrefUtils
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DI_PREF_UTILS = "DI_PREF_UTILS"
const val DI_ASSET_UTILS = "DI_ASSET_UTILS"

val utilModule = module {
    single(named(DI_PREF_UTILS)) { PrefUtils(androidApplication()) }
    single(named(DI_ASSET_UTILS)) { AssetFileReader(androidApplication()) }
}
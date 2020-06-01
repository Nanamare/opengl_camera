package com.pichu.nanamare.di

import com.pichu.nanamare.vm.CameraFilterVieModel
import com.pichu.nanamare.vm.MainViewModel
import com.pichu.nanamare.vm.navigator.CameraFilterNavigator
import com.pichu.nanamare.vm.navigator.MainNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (navigator: MainNavigator) -> MainViewModel(navigator) }
    viewModel { (navigator: CameraFilterNavigator) -> CameraFilterVieModel(navigator) }
}

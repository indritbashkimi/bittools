package com.ibashkimi.providerstools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProviderViewModel : ViewModel() {

    val settingsChangedLiveData = MutableLiveData<Boolean>()
}
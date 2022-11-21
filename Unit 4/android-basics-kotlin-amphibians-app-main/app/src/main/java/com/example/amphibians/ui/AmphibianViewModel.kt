/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import com.example.amphibians.network.AmphibianApiService
import kotlinx.coroutines.launch
import java.lang.Exception

enum class AmphibianApiStatus { LOADING, ERROR, DONE }

class AmphibianViewModel : ViewModel() {

    // DONE: Create properties to represent MutableLiveData and LiveData for the API status
    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status

    // DONE: Create properties to represent MutableLiveData and LiveData for a list of amphibian objects
    private val _data = MutableLiveData<List<Amphibian>>()
    val data: LiveData<List<Amphibian>> = _data

    // DONE: Create properties to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    private val _item = MutableLiveData<Amphibian>()
    val item: LiveData<Amphibian> = _item

    // DONE: Create a function that gets a list of amphibians from the api service and sets the
    //  status via a Coroutine
    fun getAmphibianList() {
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try {
                _status.value = AmphibianApiStatus.DONE
                _data.value = AmphibianApi.retrofitService.getAmphibians()
            } catch (e: Exception) {
                _status.value = AmphibianApiStatus.ERROR
                _data.value = listOf()
            }
        }
    }

    fun onAmphibianClicked(amphibian: Amphibian) {
        // DONE: Set the amphibian object
        _item.value = amphibian
    }
}

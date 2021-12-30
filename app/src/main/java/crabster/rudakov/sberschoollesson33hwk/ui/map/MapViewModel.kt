package crabster.rudakov.sberschoollesson33hwk.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crabster.rudakov.sberschoollesson33hwk.data.model.Coordinates
import crabster.rudakov.sberschoollesson33hwk.data.repository.LocationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MapViewModel() : ViewModel() {

    private val coordinates: MutableLiveData<List<Coordinates>> = MutableLiveData()
    private var exception: MutableLiveData<String> = MutableLiveData()
    private lateinit var disposable: Disposable

    fun coordinates(): LiveData<List<Coordinates>> {
        return coordinates
    }

    fun exception(): LiveData<String> {
        return exception
    }

//    fun getCurrentLocation() {
//        disposable =
//            locationRepository.getCurrentLocation()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    coordinates.value = it
//                }, {
//                    exception.value = it.toString()
//                })
//    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}
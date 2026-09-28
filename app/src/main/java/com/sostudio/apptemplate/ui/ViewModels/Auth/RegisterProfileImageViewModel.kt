package com.sostudio.apptemplate.ui.ViewModels.Auth

import android.graphics.Bitmap
import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.repo.AuthRepo
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.ViewModels.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class RegisterProfileImageViewModel(
    private val authRepo: AuthRepo,
    private val prefsHelper: PrefsHelper
) : BaseViewModel() {
    private val _image = MutableStateFlow<Bitmap?>(null)
    val image: StateFlow<Bitmap?> = _image.asStateFlow()

    private val _done = MutableStateFlow(false)
    val done: StateFlow<Boolean> = _done.asStateFlow()

    fun setImage(bitmap: Bitmap) {
        _image.value = bitmap
    }

    /** The register token is only ever committed to storage here or in [uploadImage] — see the
     *  comment on `RegisterViewModel.authToken` for why. */
    fun skip(token: String) {
        prefsHelper.authToken = token
        _done.value = true
    }

    fun uploadImage(token: String) {
        val bitmap = _image.value
        if (bitmap == null) {
            showAlert(message = "Please select a photo first.")
            return
        }
        screenModelScope.launch {
            setState(UIState.Loading)
            // Stored before the upload attempt: registration itself already succeeded, so the
            // user is logged in regardless of whether the avatar upload that follows succeeds.
            prefsHelper.authToken = token
            val imageBytes = ByteArrayOutputStream().use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                stream.toByteArray()
            }
            when (val result = authRepo.uploadAvatar(imageBytes)) {
                is APIResult.Success -> {
                    setState(UIState.Loaded)
                    _done.value = true
                }

                is APIResult.Error -> {
                    setState(UIState.Loaded)
                    showAlert(message = result.message)
                }
            }
        }
    }
}

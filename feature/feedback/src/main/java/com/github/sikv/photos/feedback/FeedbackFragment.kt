package com.github.sikv.photos.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.github.sikv.photos.common.ui.BaseFragment
import com.google.android.material.composethemeadapter3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : BaseFragment() {

    private val viewModel: FeedbackViewModel by viewModels()

    override val overrideBackground: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                Mdc3Theme {
                    val uiState = viewModel.uiState.collectAsState()

                    FeedbackScreen(
                        requestStatus = uiState.value.requestStatus,
                        email = uiState.value.email ?: "",
                        description = uiState.value.description ?: "",
                        onEmailChanged = viewModel::emailChanged,
                        onDescriptionChanged = viewModel::descriptionChanged,
                        onSubmitPressed = viewModel::submit,
                        onBackPressed = { navigation?.backPressed() }
                    )
                }
            }
        }
    }
}

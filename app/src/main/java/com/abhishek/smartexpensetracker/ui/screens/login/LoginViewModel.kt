package com.abhishek.smartexpensetracker.ui.screens.login

/*@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(new: String) { _uiState.update { it.copy(email = new) } }

    fun onPasswordChange(new: String) { _uiState.update { it.copy(password = new) } }

    fun loginUser() = viewModelScope.launch {
        val result = loginUseCase(uiState.value.email, uiState.value.password)
        _uiState.update { it.copy(isSuccess = result.isSuccess) }
    }
}*/

package com.example.compose.selectionchip

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val defaultChipItems = listOf(
    SelectionChipItem(
        id = 0,
        title = "Dog",
        selected = false
    ),
    SelectionChipItem(
        id = 1,
        title = "Cat",
        selected = false
    ),
    SelectionChipItem(
        id = 2,
        title = "Crocodile",
        selected = false
    ),
    SelectionChipItem(
        id = 3,
        title = "Snake",
        selected = false
    ),
    SelectionChipItem(
        id = 4,
        title = "Lizard",
        selected = false
    ),
    SelectionChipItem(
        id = 5,
        title = "Eagle",
        selected = false
    ),
)

class SelectionChipViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SelectionChipUiState())
    val uiState: StateFlow<SelectionChipUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value
        )

    private val chipItems = mutableStateListOf<SelectionChipItem>()

    init {
        _uiState.update { state ->
            state.copy(
                chipItems = chipItems,
            )
        }

        /* Initialize with dummy items */
        chipItems.addAll(
            (1..100).map { i ->
                SelectionChipItem(
                    id = i,
                    title = "Item  $i",
                    selected = false,
                )
            }
        )
    }

    fun toggleSelection(targetItem: SelectionChipItem) = viewModelScope.launch {
        val iterator = chipItems.listIterator()

        while (iterator.hasNext()) {
            val current = iterator.next()
            if (current.id == targetItem.id) {
                iterator.set(current.copy(selected = !targetItem.selected))
                break
            }
        }
    }

}

data class SelectionChipItem(
    val id: Int,
    val title: String,
    val selected: Boolean,
)

data class SelectionChipUiState(
    val chipItems: SnapshotStateList<SelectionChipItem> = mutableStateListOf()
)
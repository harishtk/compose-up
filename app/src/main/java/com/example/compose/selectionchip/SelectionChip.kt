package com.example.compose.selectionchip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.designcomponents.recomposeHighlighter
import timber.log.Timber


@Composable
fun SelectionChipRoute(
    modifier: Modifier = Modifier,
    viewModel: SelectionChipViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SelectionChipScreen(
        modifier,
        uiState = uiState,
        onClick = viewModel::toggleSelection
    )
}

@Composable
fun SelectionChipScreen(
    modifier: Modifier = Modifier,
    uiState: SelectionChipUiState,
    onClick: (item: SelectionChipItem) -> Unit = {}
) {
    Column(
        modifier
            .windowInsetsPadding(
                WindowInsets.systemBars.only(
                    WindowInsetsSides.Vertical
                )
            )
            .fillMaxSize(),
    ) {
        SelectionChipGrid(
            modifier = Modifier.padding(8.dp),
            items = uiState.chipItems,
            onClick = onClick
        )
    }
}

@Suppress("unused")
@Composable
private fun SelectionChipList(
    modifier: Modifier,
    items: List<SelectionChipItem>,
    onClick: (item: SelectionChipItem) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items, key = { item -> item.id }) { item ->
            SelectionChipRowItem(
                Modifier
                    .fillMaxWidth()
                    .recomposeHighlighter(),
                item = item,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun SelectionChipGrid(
    modifier: Modifier,
    items: List<SelectionChipItem>,
    onClick: (item: SelectionChipItem) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(80.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items, key = { item -> item.id }) { item ->
            SelectionChipRowItem(
                Modifier
                    .fillMaxWidth()
                    .recomposeHighlighter(),
                item = item,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionChipRowItem(
    modifier: Modifier = Modifier,
    item: SelectionChipItem,
    onClick: (item: SelectionChipItem) -> Unit
) {
    SideEffect {
        Timber.d("Recomposing: SelectionChipRowItem($item)")
    }

    FilterChip(
        modifier = modifier,
        selected = item.selected,
        onClick = { onClick(item) },
        label = { Text(text = item.title) }
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectionChipListPreview() {
    Column {
        val defaultChipItems = remember {
            mutableStateListOf<SelectionChipItem>().apply {
                addAll(defaultChipItems)
            }
        }
        val onToggle: (item: SelectionChipItem) -> Unit = remember {
            { item ->
                val iterator = defaultChipItems.listIterator()
                while (iterator.hasNext()) {
                    val current = iterator.next()
                    if (current.id == item.id) {
                        iterator.set(current.copy(selected = !current.selected))
                        break
                    }
                }
            }
        }

        SelectionChipGrid(
            modifier = Modifier.fillMaxSize(),
            items = defaultChipItems,
            onClick = onToggle,
        )
    }
}
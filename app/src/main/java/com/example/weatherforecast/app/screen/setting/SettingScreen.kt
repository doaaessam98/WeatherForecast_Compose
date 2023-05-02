package com.example.weatherforecast.app.screen.setting


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.burnoutcrew.reorderable.move
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.RadioButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.weatherforecast.R
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun  SettingScreen(
    modifier: Modifier=Modifier,
    viewModel: SettingViewModel= hiltViewModel()
) {
    val radioOptions = listOf("Apple", "Melons")
    var selectedItem by remember {
        mutableStateOf(radioOptions[0])
    }

    Box(modifier = modifier.fillMaxSize()) {
        
        Column(modifier = Modifier.selectableGroup()) {
            Text(text = stringResource(id = R.string.language))
            radioOptions.forEach { label ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (selectedItem==label),
                            onClick = { selectedItem = label },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(end = 16.dp),
                        selected = (selectedItem == label),
                        onClick = null
                    )
                    Text(text = label)
                }
            }
        }
    }

   }






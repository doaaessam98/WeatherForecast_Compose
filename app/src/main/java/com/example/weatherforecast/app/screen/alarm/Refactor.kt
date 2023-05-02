//package com.example.weatherforecast.app.screen.alarm
//
//import android.annotation.SuppressLint
//import android.content.ContentValues.TAG
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material.icons.outlined.Delete
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.weatherforecast.R
//import com.example.weatherforecast.app.Utils.DataResult
//import com.example.weatherforecast.app.screen.home.LoadingScreen
//import com.example.weatherforecast.domain.models.db.Alarm
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.emptyFlow
//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
//@Composable
//fun AlarmScreen1(
//    alarmViewModel: AlarmViewModel = hiltViewModel(),
//    onAddClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val bottomNavigationHeight = with(LocalDensity.current) { 32.dp.toPx() }
//    val alarmsState by alarmViewModel.alarms.collectAsState()
//
//    val isSelectAllState = remember { mutableStateOf(false) }
//    val isClickedState = remember { mutableStateOf(false) }
//    val multipleCheckedListState = remember { mutableStateOf(emptySet<Int>()) }
//    val alarmsListState = remember { mutableStateOf(emptySet<Int>()) }
//
//    val isSelectAll: Boolean by isSelectAllState
//    val isClicked: Boolean by isClickedState
//    val multipleCheckedList: Set<Int> by multipleCheckedListState
//    val alarmsList: Set<Int> by alarmsListState
//
//    val interactionSource = remember { MutableInteractionSource() }
//    val isDeleteButtonClickable = multipleCheckedList.isNotEmpty()
//    val deleteButtonBackgroundColor = MaterialTheme.colors.background
//    val deleteButtonContentColor = if (isDeleteButtonClickable) Color.Black else Color.LightGray
//
//    LaunchedEffect(alarmsList, multipleCheckedList) {
//        isSelectAllState.value = alarmsList.isNotEmpty() && alarmsList.size == multipleCheckedList.size
//    }
//
//    Scaffold(
//        modifier = Modifier.padding(bottom = bottomNavigationHeight.dp),
//        topBar = {
//            if (!isClicked) {
//                TopAppBar(
//                    title = { Text(stringResource(id = R.string.alarms)) },
//                    actions = {
//                        IconButton(onClick = { isClickedState.value = true }) {
//                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Add")
//                        }
//                    },
//                    elevation = 0.dp,
//                    backgroundColor = Color.Transparent
//                )
//            } else {
//                TopAppBar(
//                    title = {
//                        Text(
//                            if (multipleCheckedList.isEmpty())
//                                stringResource(id = R.string.selected_items)
//                            else
//                                "${multipleCheckedList.size} ${stringResource(id = R.string.selected)}",
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    },
//                    actions = {
//                        Checkbox(
//                            checked = isSelectAll,
//                            onCheckedChange = {
//                                isSelectAllState.value = it
//                                if (it) {
//

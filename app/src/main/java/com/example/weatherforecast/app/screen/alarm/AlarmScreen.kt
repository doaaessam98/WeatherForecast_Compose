package com.example.weatherforecast.app.screen.alarm

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.app.screen.home.LoadingScreen
import com.example.weatherforecast.domain.models.db.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun AlarmScreen(
    modifier:Modifier=Modifier,
    alarmViewModel: AlarmViewModel= hiltViewModel(),
    onAddClick:()->Unit
 ){


    val bottomNavigationHeight = with(LocalDensity.current) { 32.dp.toPx() }
    val isDeleteScreen=  remember { mutableStateOf(false) }
    val selectedList = remember { mutableStateOf(emptySet<Int>()) }

        val coroutineScope = rememberCoroutineScope()
        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it!=ModalBottomSheetValue.HalfExpanded },
            skipHalfExpanded = true,
        )

    ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                Column(
                    modifier
                        .heightIn(min = 200.dp)
                        .padding(bottom = bottomNavigationHeight.dp, top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,

                    ) {
                    Text(text = stringResource(id = R.string.delete),
                        color = Color.Red,
                       modifier= modifier.clickable {
                        alarmViewModel.deleteAlarms(selectedList.value)
                        coroutineScope.launch {
                            modalSheetState.hide()
                        }
                           isDeleteScreen.value = false
                           selectedList.value = emptySet()


                    })
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                            .height(1.dp)
                    )
                    Text(text = stringResource(id = R.string.cancle),
                        modifier
                            .padding(bottom = 32.dp)
                            .clickable {
                                coroutineScope.launch {
                                    modalSheetState.hide()
                                }
                            })
                }
            },
            content = {
                AlarmScreenContent(
                    alarms = alarmViewModel.alarms,
                    onAddClick = { onAddClick.invoke() },
                    onDeleteClicked = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    isDeleteScreen = isDeleteScreen,
                    selectedList = selectedList
                )
         }
        )
    }



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AlarmScreenContent(
    modifier:Modifier = Modifier,
    alarms: StateFlow<DataResult<List<Alarm>>?>,
    onAddClick:()->Unit,
    onDeleteClicked:(Set<Int>)->Unit,
    isDeleteScreen:MutableState<Boolean>,
    selectedList:MutableState<Set<Int>>
    ) {

    val alarmsState = alarms.collectAsState().value
    val alarmsList  = remember { mutableStateOf(emptySet<Int>()) }

    var isSelectAll by remember {
        mutableStateOf(alarmsList.value.isNotEmpty() && alarmsList.value.size == selectedList.value.size)
    }
    LaunchedEffect(alarmsList.value, selectedList.value) {
        isSelectAll = alarmsList.value.isNotEmpty() && alarmsList.value.size == selectedList.value.size
    }

    val enabled by remember(selectedList) {
        derivedStateOf{selectedList.value.isNotEmpty()}
    }
    val bottomNavigationHeight = with(LocalDensity.current) { 32.dp.toPx() }

    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavigationHeight.dp),
        topBar = {
            if(!isDeleteScreen.value){
                TopAppBar(
                    title = { Text(stringResource(id = R.string.alarms)) },
                    actions = {
                        if(alarmsList.value.isNotEmpty()){
                       IconButton(onClick = {isDeleteScreen.value = true}) {
                            Icon(painter = painterResource(id = R.drawable.edit_square), contentDescription ="Add" )
                        }
                    }},
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                )
            }else{
                TopAppBar(
                    title = {
                        Text(
                            if(selectedList.value.isEmpty())
                                stringResource(id = R.string.selected_items)
                            else "${selectedList.value.size} "+ stringResource(id =R.string.selected),
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    },
                    actions = {
                        Checkbox(checked = isSelectAll , onCheckedChange = {
                            isSelectAll =it
                            if(isSelectAll){
                                selectedList.value =  alarmsList.value
                            }else{
                                selectedList.value = emptySet()
                            }
                        })
                    },
                    modifier=modifier.padding(horizontal = 8.dp),
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent,
                    navigationIcon = { Icon(imageVector =Icons.Filled.Close , contentDescription ="" ,modifier.clickable {
                        isDeleteScreen.value =false
                        selectedList.value= emptySet()
                    })}
                )
            }

        },
        floatingActionButton = {
                   CustomFloatingButton(
                      onclick = {
                        if(isDeleteScreen.value && enabled)
                            {
                             onDeleteClicked.invoke(selectedList.value)
                             }
                        else if(!isDeleteScreen.value){
                              onAddClick.invoke()
                            }
                        },
                      enabled = enabled ,
                      isDeleteScreen = isDeleteScreen.value)
                               },
        floatingActionButtonPosition =  if(!isDeleteScreen.value) FabPosition.End else FabPosition.Center
    ) {_->

        alarmsState?.let {
            when(it){
                is DataResult.Loading->{
                    LoadingScreen()
                }
                is DataResult.Success ->{
                    alarmsList.value = it.data.map {alarm ->
                        alarm.id
                    }.toSet()
                    if(it.data.isEmpty()){
                        Box(modifier.fillMaxSize()) {

                        }
                    }else{
                        AlarmsListContent(it.data,
                            multipleCheckedList = selectedList,
                            isClicked = isDeleteScreen)
                    }


                }
                is DataResult.Error->{

                }
            }
        }
    }
}



@Composable
fun  CustomFloatingButton(
    onclick: () -> Unit,
    isDeleteScreen: Boolean,
    modifier: Modifier = Modifier,
    enabled:Boolean
){
    val tintColor =if (enabled) LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    else Color.LightGray
    FloatingActionButton(
        onClick = {
           onclick.invoke()
        },
        interactionSource =if(enabled) MutableInteractionSource() else NoRippleInteractionSource(),
        shape = RoundedCornerShape(if(isDeleteScreen)16.dp else 32.dp),
        backgroundColor =MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,

        ){
        if(isDeleteScreen) {
                Column(
                    modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 48.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete",
                        tint = tintColor
                    )
                    Text(text = stringResource(id = R.string.delete),
                        color = tintColor)
                }
        }else{
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}


@Composable
fun AlarmsListContent(
    data: List<Alarm>,
    multipleCheckedList:MutableState<Set<Int>>,
    isClicked: MutableState<Boolean>) {
    LazyColumn{
        itemsIndexed(data){index,alarm->
            AlarmItem(alarm=alarm, index = index+1 ,isClicked = isClicked.value,
                onclick = { isClicked.value = true },
                isMultipleChecked = multipleCheckedList
                )
        }
    }

}

@Composable
fun AlarmItem(
    modifier: Modifier=Modifier,
    alarm: Alarm,
    isClicked :Boolean,
    index:Int,
    onclick:()->Unit,
    isMultipleChecked: MutableState<Set<Int>>,

     ) {

    Row(modifier = modifier.clickable {
         onclick.invoke()

     if(isMultipleChecked.value.contains(alarm.id)) {
         isMultipleChecked.value =  isMultipleChecked.value - alarm.id
        }else{
         isMultipleChecked.value =   isMultipleChecked.value + alarm.id
        }

    }, verticalAlignment = Alignment.CenterVertically) {

        Card(
            modifier = if(isClicked) modifier
                .width(300.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
            else modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.onSecondary,
                                MaterialTheme.colors.secondary

                            )
                        )
                    )
                    .fillMaxWidth()
                    .padding(vertical = 32.dp), horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = alarm.place)
                Column {
                    Text(text = alarm.date)
                    Text(text = alarm.time)

                }
            }
        }
        if(isClicked) {
            Checkbox(
                checked = isMultipleChecked.value.contains(alarm.id),
                onCheckedChange = {
                    if(it) {
                        isMultipleChecked.value = isMultipleChecked.value + alarm.id
                    } else {
                        isMultipleChecked.value = isMultipleChecked.value - alarm.id
                    }


                }
            )


        }

    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}


class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}



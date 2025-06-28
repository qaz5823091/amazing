package com.cppdesigns.amazing_assignment.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel = MainViewModel()) {
    val viewState by mainViewModel.viewState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 7 })
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar()
        }
    ) { innerPadding ->
        LaunchedEffect(selectedIndex) {
            pagerState.scrollToPage(selectedIndex.intValue)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            selectedIndex.intValue = pagerState.currentPage;
        }
        Column(modifier = Modifier.padding(innerPadding)) {
            TeacherSelection(
                onSelect = mainViewModel::selectTeacher
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TimeSelection(
                title = viewState.timeText,
                isFirstPage = viewState.page == 0,
                onBack = mainViewModel::previousWeek,
                onForward = mainViewModel::nextWeek,
                pagerState = pagerState,
                weeks = viewState.weeks,
                selectedIndex = selectedIndex,
                timeTable = viewState.timeTable,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = { Text("Amazing Assignment", textAlign = TextAlign.Center) },
    )
}

@Composable
fun TeacherSelection(
    onSelect: (String) -> Unit,
) {
    val buttons = listOf(
        "sakurashimone",
        "kiki-fu",
        "yunning"
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("選擇老師")
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(buttons) { index, text ->
                val isSelected = selectedIndex == index;
                Button(
                    onClick = {
                        selectedIndex = index
                        onSelect(buttons[selectedIndex])
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (isSelected) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.onPrimary
                        },
                        contentColor = when (isSelected) {
                            true -> MaterialTheme.colorScheme.onPrimary
                            false -> MaterialTheme.colorScheme.primary
                        },
                    )
                ) {
                    Text(text, minLines = 1)
                }
            }
        }
    }
}

@Composable
fun TimeSelection(
    title: String,
    isFirstPage: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit,
    pagerState: PagerState,
    weeks: List<Pair<String, String>>,
    selectedIndex: MutableIntState,
    timeTable: Map<String, List<Pair<String, Boolean>>>
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("選擇時間")
        DateSelectionBar(
            title = title,
            isFirstPage = isFirstPage,
            onBack = onBack,
            onForward = onForward,
        )
        TabView(
            pagerState = pagerState,
            weeks = weeks,
            selectedIndex = selectedIndex,
        )
        TimeView(
            pagerState = pagerState,
            weeks = weeks,
            timeTable = timeTable,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionBar(
    title: String,
    isFirstPage: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        },
        navigationIcon = {
            BackButton(
                enabled = !isFirstPage,
                onClick = onBack
            )
        },
        actions = { ForwardButton(onForward) },
    )
}

@Composable
fun BackButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Back"
        )
    }
}

@Composable
fun ForwardButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Forward"
        )
    }
}

@Composable
fun TabView(
    pagerState: PagerState,
    weeks: List<Pair<String, String>>,
    selectedIndex: MutableIntState,
) {
    val scrollScope = rememberCoroutineScope()
    ScrollableTabRow(selectedTabIndex = selectedIndex.intValue) {
        weeks.forEachIndexed { index, value ->
            Tab(
                selected = selectedIndex.intValue == index,
                onClick = {
                    selectedIndex.intValue = index
                    scrollScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                text = { Text(text = "${value.first}\n${value.second}") }
            )
        }
    }
}

@Composable
fun TimeView(
    pagerState: PagerState,
    weeks: List<Pair<String, String>>,
    timeTable: Map<String, List<Pair<String, Boolean>>>
) {
    HorizontalPager(state = pagerState) { page ->
        val week = weeks.elementAt(page).second
        val list = timeTable.entries.firstOrNull() { item ->
            item.key.contains(week)
        }?.value
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = when(list?.isNotEmpty() == true) {
                true -> Arrangement.Top
                false -> Arrangement.Center
            } ,
        ) {
            if (list?.isNotEmpty() == true) {
                list.forEach {
                    TimeButton(it.first, it.second)
                }
            } else {
                Text("沒有可選擇的時間")
            }
        }
    }
}

@Composable
fun TimeButton(text: String, enabled: Boolean) {
    ElevatedButton(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = {},
        enabled = enabled,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(),
    ) {
        Text(text)
    }
}
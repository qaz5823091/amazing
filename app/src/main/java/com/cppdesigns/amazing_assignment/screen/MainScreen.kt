package com.cppdesigns.amazing_assignment.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
            AppBar(
                title = viewState.timeText,
                onBack = mainViewModel::previousWeek,
                onForward = mainViewModel::nextWeek
            )
        }
    ) { innerPadding ->
        LaunchedEffect(selectedIndex) {
            pagerState.scrollToPage(selectedIndex.intValue)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            selectedIndex.intValue = pagerState.currentPage;
        }
        Column(modifier = Modifier.padding(innerPadding)) {
            TabView(
                pagerState = pagerState,
                weeks = viewState.weeks,
                selectedIndex = selectedIndex,
            )
            TimeView(
                pagerState = pagerState,
                weeks = viewState.weeks,
                timeTable = viewState.timeTable,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onBack: () -> Unit,
    onForward: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title, textAlign = TextAlign.Center) },
        navigationIcon = { BackButton(onBack) },
        actions = { ForwardButton(onForward) },
    )
}

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
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
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            if (list?.isNotEmpty() == true) {
                list.forEach {
                    TimeButton(it.first, it.second)
                }
            } else {
                Text("No")
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
package com.example.pagingfailure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import kotlinx.coroutines.launch

class ExamplePagingFragment: Fragment() {

	private val viewModel: ExamplePagingViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
		ComposeView(inflater.context).apply {
			setContent {
				CreateContent()
			}
		}

	@Composable
	private fun CreateContent() {
		val lazyItems = viewModel.pagingFlow.collectAsLazyPagingItems()

		val lazyListState: LazyListState = rememberLazyListState()

		val coroutineScope = rememberCoroutineScope()

		LaunchedEffect(lazyListState) {
			snapshotFlow { lazyListState.firstVisibleItemIndex }
				.collect {
					println("Scrolled to $it")
				}
		}

		Box(contentAlignment = Alignment.BottomEnd) {
			LazyColumn(Modifier.fillMaxSize(), lazyListState) {
				itemsIndexed(lazyItems) { index, item ->
					if (item != null) {
						Text(item, Modifier.padding(24.dp))
					} else {
						Text("Loading number $index...", Modifier.padding(24.dp))
					}
					Divider()
				}
			}
			FloatingActionButton(
				onClick = {
					coroutineScope.launch {
						lazyListState.animateScrollToItem(500)
					}
				},
				Modifier.padding(16.dp)
			) {
				Text("Go to #500", Modifier.padding(8.dp))
			}
		}
	}
}
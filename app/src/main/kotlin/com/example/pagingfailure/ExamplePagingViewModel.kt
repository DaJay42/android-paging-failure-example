package com.example.pagingfailure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.delay

class ExamplePagingViewModel: ViewModel() {

	private val pagingConfig = PagingConfig(
		pageSize = 15,
		//maxSize = 50,
		jumpThreshold = 40,
	)

	val pagingFlow = Pager(pagingConfig) {
		LocalPagingSource(this::getTotalSize, this::loadRange)
	}.flow.cachedIn(viewModelScope)

	private fun getTotalSize(): Int {
		return 20_000
	}

	private suspend fun loadRange(range: IntRange): List<String> {
		return range.map {
			delay(30)
			"This is Item #$it"
		}
	}
}
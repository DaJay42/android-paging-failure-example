package com.example.pagingfailure

import androidx.paging.PagingSource
import androidx.paging.PagingState

class LocalPagingSource<T : Any>(

	private val getTotalSizeCallback: () -> Int,
	private val loadRangeCallback: suspend (IntRange) -> List<T>

) : PagingSource<Int, T>() {

	override val jumpingSupported: Boolean
		get() = true

	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val startPosition = (anchorPosition - state.config.initialLoadSize / 2).coerceAtLeast(0)
			val anchorPage = state.closestPageToPosition(startPosition)
			anchorPage?.prevKey
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val maxIndex = getTotalSizeCallback() - 1
		val (firstIndex, lastIndex) = when (params) {
			is LoadParams.Refresh -> {
				println("Refresh size ${params.loadSize} at key ${params.key}")
				val key = params.key ?: 0
				Pair(
					key,
					(key + params.loadSize - 1).coerceAtMost(maxIndex)
				)
			}
			is LoadParams.Append -> {
				println("Append size ${params.loadSize} at key ${params.key}")
				Pair(
					params.key,
					(params.key + params.loadSize - 1).coerceAtMost(maxIndex)
				)
			}
			is LoadParams.Prepend -> {
				println("Prepend size ${params.loadSize} at key ${params.key}")
				Pair(
					(params.key - params.loadSize).coerceAtLeast(0),
					params.key - 1
				)
			}
		}
		val prevKey = firstIndex.takeIf { it > 0 }
		val nextKey = (lastIndex + 1).takeIf { it < maxIndex }
		val itemsBefore = firstIndex.coerceAtLeast(0)
		val itemsAfter = (maxIndex - lastIndex).coerceAtLeast(0)
		val range = firstIndex..lastIndex

		val states = loadRangeCallback(range)
		println("Result: Page $range (size ${states.size}), with prev=$prevKey, next=$nextKey, before=$itemsBefore, after=$itemsAfter")
		return LoadResult.Page(
			data = states,
			prevKey = prevKey,
			nextKey = nextKey,
			itemsBefore = itemsBefore,
			itemsAfter = itemsAfter,
		)
	}
}
package com.github.hueyra.patternlocker.bean

import com.github.hueyra.patternlocker.def.Logger

object CellFactory {

    fun buildCells(width: Int, height: Int): List<CellBean> {
        val result = ArrayList<CellBean>()
        val pWidth = width / 8f
        val pHeight = height / 8f

        for (i in 0..8) {
            result.add(
                CellBean(i,
                    i % 3,
                    i / 3,
                    (i % 3 * 3 + 1) * pWidth,
                    (i / 3 * 3 + 1) * pHeight,
                    pWidth)
            )
        }

        Logger.d("CellFactory", "result = $result")

        return result
    }
}
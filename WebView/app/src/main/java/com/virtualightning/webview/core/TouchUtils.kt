package com.virtualightning.webview.core

import java.util.*
import kotlin.random.Random

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 20:57:00
 *  Project : WebView
 *  Since Version : Alpha
 */
object TouchUtils {
    private val viewTouchBundleList = LinkedList<ViewTouchBundle>()
    private var isLooping = false

    fun addBundle(bundle: ViewTouchBundle) {
        bundle.down()
        viewTouchBundleList.add(bundle)
        if(!isLooping) {
            isLooping = true
            dispatchMove()
        }
    }

    private fun doMove() {
        if(viewTouchBundleList.size == 0) {
            isLooping = false
            return
        }

        val iterator = viewTouchBundleList.iterator()

        val itemCount = 2

        val time: Long = Random.nextLong(15, 25)

        val moveX: Float = Random.nextInt(3, 10).toFloat() + Random.nextFloat()

        val moveY: Float = Random.nextInt(3, 5).toFloat()

        val waitMoveX: Float = Random.nextInt(1, 2) + Random.nextFloat()

        while (iterator.hasNext()) {
            val bundle = iterator.next()
            if(bundle.isEnd) {
                iterator.remove()
                continue
            }

            if(bundle.isOver) {
                bundle.overMove()
                iterator.remove()
            }
            else if(bundle.isWaitOver) {
                bundle.waitMove(waitMoveX, waitMoveX, waitMoveX, time, time, time)
            }
            else {
                for(i in 0..itemCount) {
                    if(bundle.isWaitOver)
                        continue
                    bundle.move(moveX, moveY, time)
                }
            }
        }

        dispatchMove()
    }

    private fun dispatchMove() {
        SysUtils.runOnMainDelay(Random.nextLong(15, 25)) {
            doMove()
        }
    }
}
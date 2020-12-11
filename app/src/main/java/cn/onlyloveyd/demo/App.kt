package cn.onlyloveyd.demo

import android.app.Application

/**
 * 主程序
 * author: yidong
 * 2020/1/7
 */
class App : Application() {
    companion object {

        const val TAG = "LearningAndroidOpenCV"
    }


    init {
        System.loadLibrary("opencv_java4")
    }
}
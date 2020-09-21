package com.gushenge.easybarrage.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gushenge.easybarrage.Barrage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mBarrages: List<Barrage> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barrageView.setBarrages(mBarrages)
        send.setOnClickListener { barrageView.addBarrage(Barrage("111111111111")) }
    }

    override fun onDestroy() {
        super.onDestroy()
        barrageView.destroy()
    }
}
package com.example.sunsettyandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class BeforeStartFragment : Fragment() , CoroutineScope by MainScope(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_before_start, container, false)
    }

    override fun onStart() {
        super.onStart()

        requireView().findViewById<Button>(R.id.startButton).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_beforeStartFragment_to_guessInProgressFragment)
        }

        val contentViewModel = ContentViewModel.shared
        /*launch {
            contentViewModel.getCity()
            //contentViewModel.getSunriseSunset()
            //contentViewModel.getGMTOffset()
        }
        
         */

    }

}
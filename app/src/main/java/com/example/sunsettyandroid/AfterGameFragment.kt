package com.example.sunsettyandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AfterGameFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_after_game, container, false)
    }

    override fun onStart() {
        super.onStart()

        if(ContentViewModel.shared.isSunrise)
        {
            requireView().findViewById<TextView>(R.id.resultRiseOrSetTV).text = "The sun rises at:"
            requireView().findViewById<TextView>(R.id.correctTimeTV).text = ContentViewModel.shared.sunriselocalTime
        }
        else{
            requireView().findViewById<TextView>(R.id.resultRiseOrSetTV).text = "The sun sets at:"
            requireView().findViewById<TextView>(R.id.correctTimeTV).text = ContentViewModel.shared.sunsetlocalTime
        }

        requireView().findViewById<Button>(R.id.playAgainButton).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_afterGameFragment_to_guessInProgressFragment)
        }


        requireView().findViewById<TextView>(R.id.timeOffTV).text = ContentViewModel.shared.guessedTimeHourOffset!!.toInt().toString() + " hours and " + ContentViewModel.shared.guessedTimeMinuteOffset!!.toInt().toString() + " minutes off"

    }
}
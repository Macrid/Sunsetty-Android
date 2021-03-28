package com.example.sunsettyandroid

import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*


class GuessInProgressFragment : Fragment(), CoroutineScope by MainScope() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guess_in_progress, container, false)
    }

    override fun onStart() {
        super.onStart()
        ContentViewModel.shared.isSunrise = ContentViewModel.shared.getRandomBoolean()
        if (ContentViewModel.shared.isSunrise)
        {
            requireView().findViewById<TextView>(R.id.riseOrSetTV).text = "When does the sun rise in:"
        }
        else{
            requireView().findViewById<TextView>(R.id.riseOrSetTV).text = "When does the sun set in:"
        }
        launch {
            ContentViewModel.shared.getCity()
            requireView().findViewById<TextView>(R.id.cityNameTV).text = ContentViewModel.shared.cityName + ", \n" + ContentViewModel.shared.cityCountry
        }

        requireView().findViewById<Button>(R.id.pickTimeButton).setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener {timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                requireView().findViewById<TextView>(R.id.timeChosenTV).text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireContext(),timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        requireView().findViewById<Button>(R.id.GuessButton).setOnClickListener {
            //TODO snackbar vid ingen tid vald

            ContentViewModel.shared.compareTime(requireView().findViewById<TextView>(R.id.timeChosenTV).text.toString())
            Navigation.findNavController(requireView()).navigate(R.id.action_guessInProgressFragment_to_afterGameFragment)
        }
    }

}
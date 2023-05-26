

//entertainment api:  https://opentdb.com/api.php?amount=10&category=11&type=multiple

package com.example.trivia
import com.example.trivia.databinding.ArtsBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory





class Entertainment : AppCompatActivity() {
    private lateinit var binding: ArtsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArtsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recyclerView = binding.root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(emptyList())
        recyclerView.adapter = questionAdapter
        fetchJson()

    }

    private fun fetchJson() {
        val url = "https://opentdb.com/api.php?amount=10&category=11&type=multiple"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println("JSON Response: $body")

                val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(TriviaResponseArt::class.java)
                val triviaResponseArt = jsonAdapter.fromJson(body)

                if (triviaResponseArt == null || triviaResponseArt.results.isEmpty()) {
                    println("Failed to parse JSON response or no results found")
                    return
                }

                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    updateUI(triviaResponseArt.results)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }


    private fun updateUI(questions: List<TriviaQuestionArt>?) {
        if (!questions.isNullOrEmpty()) {
            recyclerView.visibility = View.VISIBLE // Make the RecyclerView visible

            questionAdapter = QuestionAdapter(questions)
            recyclerView.adapter = questionAdapter
        }
    }


    private class QuestionAdapter(private val questions: List<TriviaQuestionArt>) :
        RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false)
            return QuestionViewHolder(view)
        }


        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)

            // Set the checked state of the RadioButton based on the selected option
            val selectedOption = question.selectedOption
            val optionGroup = holder.itemView.findViewById<RadioGroup>(R.id.optionGroup)
            val option1 = holder.itemView.findViewById<RadioButton>(R.id.option_1)
            val option2 = holder.itemView.findViewById<RadioButton>(R.id.option_2)
            val option3 = holder.itemView.findViewById<RadioButton>(R.id.option_3)
            val option4 = holder.itemView.findViewById<RadioButton>(R.id.option_4)

            optionGroup.setOnCheckedChangeListener(null) // Clear previous listeners

            when (selectedOption) {
                option1.text.toString() -> option1.isChecked = true
                option2.text.toString() -> option2.isChecked = true
                option3.text.toString() -> option3.isChecked = true
                option4.text.toString() -> option4.isChecked = true
            }

            optionGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedOption: String = when (checkedId) {
                    R.id.option_1 -> option1.text.toString()
                    R.id.option_2 -> option2.text.toString()
                    R.id.option_3 -> option3.text.toString()
                    R.id.option_4 -> option4.text.toString()
                    else -> ""
                }
                // Update the selected option in the TriviaQuestionArt object
                question.selectedOption = selectedOption
            }
        }


        override fun getItemCount(): Int {
            return questions.size
        }

        inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val questionText: TextView = view.findViewById(R.id.question)

            private val option1: RadioButton = view.findViewById(R.id.option_1)
            private val option2: RadioButton = view.findViewById(R.id.option_2)
            private val option3: RadioButton = view.findViewById(R.id.option_3)
            private val option4: RadioButton = view.findViewById(R.id.option_4)

            fun bind(question: TriviaQuestionArt) {
                questionText.text = question.question
                option1.text = question.correctAnswer
                option2.text =
                    question.incorrectAnswer?.getOrElse(0) { "Incorrect answer not provided" }
                option3.text =
                    question.incorrectAnswer?.getOrElse(1) { "Incorrect answer not provided" }
                option4.text =
                    question.incorrectAnswer?.getOrElse(2) { "Incorrect answer not provided" }


            }
        }
    }
}

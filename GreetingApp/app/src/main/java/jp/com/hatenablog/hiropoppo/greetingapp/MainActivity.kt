package jp.com.hatenablog.hiropoppo.greetingapp

import android.app.TimePickerDialog
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jp.com.hatenablog.hiropoppo.greetingapp.databinding.ActivityMainBinding
import java.util.*

/**
 * Main Activity
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    enum class TimeType {
        MORNING,
        NOON,
        NIGHT
    }

    //region MARK: - private fields
    private lateinit var binding: ActivityMainBinding

    private lateinit var textToSpeechJP: TextToSpeech
    private lateinit var textToSpeechEN: TextToSpeech
    private lateinit var textToSpeechGER: TextToSpeech

    private var currentTimeType = TimeType.NOON
    private var currentHour = 0
    private var currentMinute = 0
    //endregion

    //region MARK: - activity lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        textToSpeechJP = TextToSpeech(this, this)
        textToSpeechEN = TextToSpeech(this, this)
        textToSpeechGER = TextToSpeech(this, this)

        binding.timerButton.setOnClickListener { showTimePickerDialog() }
        getCurrentDate()
        setupTextClickListener()
    }

    // TextToSpeech lifecycle methods
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeechJP.language = Locale.JAPAN
            textToSpeechEN.language = Locale.ENGLISH
            textToSpeechGER.language = Locale.GERMANY
        }
    }
    //endregion

    //region MARK: - private methods
    /**
     * タイムピッカーを表示する
     */
    private fun showTimePickerDialog() {

        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, _ ->
                setView(hour)
            },
            currentHour, currentMinute, true
        ).show()
    }

    /**
     * 現在の時刻を取得し、Viewを設定する
     */
    private fun getCurrentDate() {
        val cal = Calendar.getInstance(Locale.JAPAN)
        currentHour = cal.get(Calendar.HOUR_OF_DAY)
        currentMinute = cal.get(Calendar.MINUTE)
        setView(currentHour)
    }

    /**
     * Viewの設定
     *
     * @param hour 時間(Hour)
     */
    private fun setView(hour: Int) {
        if (hour in 2..9) {
            // 2:00 ~ 9:59
            currentTimeType = TimeType.MORNING

            binding.greetingJp.text = this.getString(R.string.morning_jp)
            binding.greetingEn.text = this.getString(R.string.morning_en)
            binding.greetingGer.text = this.getString(R.string.morning_ger)
        } else if (hour in 10..17) {
            // 10:00 ~ 17:59
            currentTimeType = TimeType.NOON

            binding.greetingJp.text = this.getString(R.string.noon_jp)
            binding.greetingEn.text = this.getString(R.string.noon_en)
            binding.greetingGer.text = this.getString(R.string.noon_ger)
        } else if ((hour in 18..24) || (hour in 0..1)) {
            // 18:00 ~ 1:59
            currentTimeType = TimeType.NIGHT

            binding.greetingJp.text = this.getString(R.string.night_jp)
            binding.greetingEn.text = this.getString(R.string.night_en)
            binding.greetingGer.text = this.getString(R.string.night_ger)
        }
    }

    /**
     * TextViewのクリックリスナーの設定
     */
    private fun setupTextClickListener() {

        // 日本語
        binding.greetingJp.setOnClickListener {
            when (currentTimeType) {
                TimeType.MORNING -> {
                    startSpeak(textToSpeechJP, this.getString(R.string.morning_jp))
                }
                TimeType.NOON -> {
                    startSpeak(textToSpeechJP, this.getString(R.string.noon_jp))
                }
                TimeType.NIGHT -> {
                    startSpeak(textToSpeechJP, this.getString(R.string.night_jp))
                }
            }
        }

        // 英語
        binding.greetingEn.setOnClickListener {
            when (currentTimeType) {
                TimeType.MORNING -> {
                    startSpeak(textToSpeechEN, this.getString(R.string.morning_en))
                }
                TimeType.NOON -> {
                    startSpeak(textToSpeechEN, this.getString(R.string.noon_en))
                }
                TimeType.NIGHT -> {
                    startSpeak(textToSpeechEN, this.getString(R.string.night_en))
                }
            }
        }

        // ドイツ語
        binding.greetingGer.setOnClickListener {
            when (currentTimeType) {
                TimeType.MORNING -> {
                    startSpeak(textToSpeechGER, this.getString(R.string.morning_ger))
                }
                TimeType.NOON -> {
                    startSpeak(textToSpeechGER, this.getString(R.string.noon_ger))
                }
                TimeType.NIGHT -> {
                    startSpeak(textToSpeechGER, this.getString(R.string.night_ger))
                }
            }
        }
    }

    /**
     * 発音を開始する
     *
     * @param textToSpeech TextToSpeech
     * @param text 読み上げる文言
     */
    private fun startSpeak(textToSpeech: TextToSpeech, text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }
    //endregion
}

package otus.homework.reactivecats

import android.content.Context
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LocalCatFactsGenerator(
    private val context: Context
) {

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFact так,
     * чтобы она возвращала Fact со случайной строкой  из массива строк R.array.local_cat_facts
     * обернутую в подходящий стрим(Flowable/Single/Observable и т.п)
     */
    fun generateCatFact(): Single<Fact> {
        val messages = context.resources.getStringArray(R.array.local_cat_facts).toList()
        return Single.create {
            it.onSuccess(Fact(messages.random()))
        }
    }

    /**
     * Реализуйте функцию otus.homework.reactivecats.LocalCatFactsGenerator#generateCatFactPeriodically так,
     * чтобы она эмитила Fact со случайной строкой из массива строк R.array.local_cat_facts каждые 2000 миллисекунд.
     * Если вновь заэмиченный Fact совпадает с предыдущим - пропускаем элемент.
     */
    fun generateCatFactPeriodically(): Flowable<Fact> {
        //val success = Fact(context.resources.getStringArray(R.array.local_cat_facts)[Random.nextInt(5)])
        val messages = context.resources.getStringArray(R.array.local_cat_facts).toList()
        val count = messages.count()
        return Flowable
            .interval(2000, TimeUnit.MILLISECONDS)
            .map { Random.nextInt(count - 1) }
            .scan { pre, current ->
                var newCurrent = current
                while (pre == newCurrent) {
                    newCurrent = Random.nextInt(count - 1)
                }
                newCurrent
            }
            .map { Fact(messages[it]) }
    }
}
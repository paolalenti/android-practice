package com.example.homework1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.homework1.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn = findViewById<Button>(R.id.send_number_btn)
        btn.setOnClickListener {
            val carsCount = findViewById<EditText>(R.id.q_text_input).text.toString().toInt()
            var carArray = Array<Pair<Car, Int>>(size = carsCount) { i ->
                Pair(
                    randomizeCar(), i + 1
                )
            }
            for (car in carArray) {
                println("#${car.second}:")
                car.first.getInfo()
            }

            while (carArray.size > 1) {
                carArray.shuffle()
                var newArray = arrayOf<Pair<Car, Int>>()
                for (i in 0..<carArray.size step 2) {
                    if (i + 1 >= carArray.size) {
                        println("${carArray[i].second} - нет пары, следующий круг")
                        newArray = newArray.plus(carArray[i])
                        break
                    }
                    val (winner, winnerId) = if (carArray[i].first.race(carArray[i + 1].first))
                        Pair(carArray[i].second, i)
                    else Pair(carArray[i + 1].second, i + 1)
                    println(
                        "Гонка между ${carArray[i].second} и ${carArray[i + 1].second}, Победитель $winner"
                    )
                    newArray = newArray.plus(carArray[winnerId])
                }
                carArray = newArray
            }
            println("Номер ${carArray[0].second} выиграл гонку!")
        }
    }
}

val listMake = listOf("BMW", "Mercedes", "Audi", "Kia", "Ford", "Subaru", "Nissan", "Mazda")
val listColour = listOf("Зелёный", "Синий", "Жёлтый", "Чёрный", "Белый", "Красный", "Розовый")
val listDrive = listOf("Задний", "Полный", "Передний")
val listWheel = listOf("Кожа","Кожезам")


open class Car(
    val make: String,
    val colour: String,
    val yearOfIssue: Int,
    val price: Int
){
    open fun getInfo() {
        println("Марка: $make, цвет: $colour, год выпуска: $yearOfIssue, стоимость: $price")
    }

    fun race(other: Car): Boolean {
        return price > other.price
    }
}

class CrossoverCar(make: String, colour: String, yearOfIssue: Int, price: Int, val drive: String, val power: Int) : Car(
    make = make,
    colour = colour,
    yearOfIssue = yearOfIssue,
    price = price
) {
    override fun getInfo() {
        super.getInfo()
        println("Привод: $drive, мощность двигателя: $power")
    }
}

class CoupeCar(make: String, colour: String, yearOfIssue: Int, price: Int, val new: Boolean, val spoiler: Boolean) : Car(
    make = make,
    colour = colour,
    yearOfIssue = yearOfIssue,
    price = price
) {
    override fun getInfo() {
        super.getInfo()
        println("Новая: ${if (new) "Да" else "Нет"}, спойлер: ${if (spoiler) "Есть" else "Нет"}")
    }
}

class HatchbackCar(make: String, colour: String, yearOfIssue: Int, price: Int, val mileage: Double, val stolen: Boolean) : Car(
    make = make,
    colour = colour,
    yearOfIssue = yearOfIssue,
    price = price
) {
    override fun getInfo() {
        super.getInfo()
        println("Пробег: $mileage, в угоне: ${if (stolen) "Да" else "Нет"}")
    }
}

class SedanCar(make: String, colour: String, yearOfIssue: Int, price: Int, val weight: Int, val wheel: String) : Car(
    make = make,
    colour = colour,
    yearOfIssue = yearOfIssue,
    price = price
) {
    override fun getInfo() {
        super.getInfo()
        println("Масса: $weight, Колесо: $wheel")
    }
}

fun randomizeCar(): Car {
    val makeR = listMake[Random.nextInt(listMake.size)]
    val colourR = listColour[Random.nextInt(listColour.size)]
    val yearOfIssueR = Random.nextInt(2000,2024)
    val priceR = Random.nextInt(100000,10000000)
    val type = Random.nextInt(4)
    when (type){
        0 -> {
            val driveR = listDrive[Random.nextInt(listDrive.size)]
            val powerR = Random.nextInt(100,400)
            val carR = CrossoverCar(makeR, colourR, yearOfIssueR, priceR, driveR, powerR)
            return carR
        }
        1 -> {
            val newR = Random.nextBoolean()
            val spoilerR = Random.nextBoolean()
            val carR = CoupeCar(makeR, colourR, yearOfIssueR, priceR, newR, spoilerR)
            return carR
        }
        2 -> {
            val mileageR = Random.nextDouble(0.0, 1000.0)
            val stolenR = Random.nextBoolean()
            val carR = HatchbackCar(makeR, colourR, yearOfIssueR, priceR, mileageR, stolenR)
            return carR
        }
        3 -> {
            val weightR = Random.nextInt(800, 2500)
            val wheelR = listWheel[Random.nextInt(listWheel.size)]
            val carR = SedanCar(makeR, colourR, yearOfIssueR, priceR, weightR, wheelR)
            return carR
        }
    }
    return TODO("Provide the return value")
}

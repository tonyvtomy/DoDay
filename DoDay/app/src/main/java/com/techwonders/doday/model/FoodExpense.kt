package com.techwonders.doday.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_food_expense")
class FoodExpense() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var title = "Food Expense"
    var timestamp = ""
    var tag = ""
    var forBreakfast = 0
    var forLunch = 0
    var forDinner = 0
    var status = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString().toString()
        timestamp = parcel.readString().toString()
        tag = parcel.readString().toString()
        forBreakfast = parcel.readInt()
        forLunch = parcel.readInt()
        forDinner = parcel.readInt()
        status = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(timestamp)
        parcel.writeString(tag)
        parcel.writeInt(forBreakfast)
        parcel.writeInt(forLunch)
        parcel.writeInt(forDinner)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodExpense> {
        override fun createFromParcel(parcel: Parcel): FoodExpense {
            return FoodExpense(parcel)
        }

        override fun newArray(size: Int): Array<FoodExpense?> {
            return arrayOfNulls(size)
        }
    }

    fun getTotalExpense() = forBreakfast + forLunch + forDinner
    override fun toString(): String {
        return "FoodExpense(id=$id, title='$title', timestamp='$timestamp', tag='$tag', forBreakfast=$forBreakfast, forLunch=$forLunch, forDinner=$forDinner, status=$status)"
    }

}

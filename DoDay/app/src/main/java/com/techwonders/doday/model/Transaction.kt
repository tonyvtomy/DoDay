package com.techwonders.doday.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techwonders.doday.utility.getDateFromTimestamp

@Entity(tableName = "my_transactions")
class Transaction() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var title = "Transaction"
    var timestamp = ""
    var total = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString().toString()
        timestamp = parcel.readString().toString()
        total = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(timestamp)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

}

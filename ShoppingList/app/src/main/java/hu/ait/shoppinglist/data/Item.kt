package hu.ait.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")

data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "category") var itemCategory: Int,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "done") var done: Boolean
) : Serializable
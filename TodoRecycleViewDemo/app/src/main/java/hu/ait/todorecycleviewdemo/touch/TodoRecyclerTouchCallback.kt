package hu.ait.todorecycleviewdemo.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import hu.ait.todorecycleviewdemo.adapter.TodoAdapter

class TodoRecyclerTouchCallback(private val todoTouchHelperAdapter: TodoTouchHelperCallback):
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        todoTouchHelperAdapter.onDismissed(viewHolder.adapterPosition)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,  //from
        target: RecyclerView.ViewHolder       //to
    ): Boolean {
        todoTouchHelperAdapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

}
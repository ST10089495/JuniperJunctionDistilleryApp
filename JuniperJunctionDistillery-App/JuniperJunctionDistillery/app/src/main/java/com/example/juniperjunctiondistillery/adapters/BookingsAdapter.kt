package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowBookingBinding
import com.example.juniperjunctiondistillery.menu.bookings.BookingDetailsActivity
import com.example.juniperjunctiondistillery.menu.bookings.EditBookingsActivity
import com.example.juniperjunctiondistillery.menu.employee.EditEmployeeActivity
import com.example.juniperjunctiondistillery.models.BookingModel
import com.google.firebase.database.FirebaseDatabase

class BookingsAdapter : RecyclerView.Adapter<BookingsAdapter.HolderBooking>{
    private val context: Context
    var bookingArrayList: ArrayList<BookingModel>
    private lateinit var binding: RowBookingBinding

    constructor(context: Context, bookingArrayList: ArrayList<BookingModel>) {
        this.context = context
        this.bookingArrayList = bookingArrayList
    }
    inner class HolderBooking(itemView: View):RecyclerView.ViewHolder(itemView){
        var bNameTv: TextView =binding.tvBookName
        var bDescrTv: TextView =binding.tvBookDescr
        var bEmailTv: TextView=binding.tvBookEmail
        var bDateTv: TextView=binding.tvBookDate
        var bTimeTv: TextView=binding.tvBookTime
        var bookMoreBtn : ImageButton =binding.SuppBookingMoreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBooking {
        binding = RowBookingBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderBooking(binding.root)
    }

    override fun onBindViewHolder(holder: HolderBooking, position: Int) {
        val model = bookingArrayList[position]
        val bookId = model.Id
        val name = model.Name
        val exp = model.ExperienceType
        val email = model.Email
        val date = model.Date
        val time =model.PreferredTime

        holder.bNameTv.text=name
        holder.bDescrTv.text=exp
        holder.bEmailTv.text=email
        holder.bDateTv.text=date
        holder.bTimeTv.text=time

        holder.bookMoreBtn.setOnClickListener {
            moreOptionsDialog(model,holder )
        }

    }

    private fun moreOptionsDialog(model: BookingModel, holder: BookingsAdapter.HolderBooking) {
        val bookId = model.Id
        val bookName = model.Name
        val options = arrayOf("Mark as Completed")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Booking Status:")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Mark as Completed")
                        .setMessage("Are you sure this booking has been completed?")
                        .setPositiveButton("Confirm") { a, d ->
                            Toast.makeText(context, "Marking booking as completed...", Toast.LENGTH_SHORT).show()
                            deleteStock(model, bookId, bookName)
                        }
                        .setNegativeButton("Cancel") { a, d ->
                            a.dismiss()
                        }
                        .show()
                }
            }.show()
    }

    private fun deleteStock(model: BookingModel, bookId: String, bookName: String) {
        val id = model.Id
        val ref = FirebaseDatabase.getInstance().getReference("bookings")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted!!!", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Unable To Delete Due To ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    override fun getItemCount(): Int {
        return bookingArrayList.size
    }
}
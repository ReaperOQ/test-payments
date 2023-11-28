package ru.reaperoq.test_task.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.reaperoq.test_task.databinding.PaymentsRowBinding
import ru.reaperoq.test_task.datasource.models.Payment
import ru.reaperoq.test_task.utils.roundOffDecimal
import java.text.SimpleDateFormat
import java.util.Locale

class PaymentsRecyclerViewAdapter : RecyclerView.Adapter<PaymentsRecyclerViewAdapter.ViewHolder>() {


    private val payments = mutableListOf<Payment>()

    fun update(newPayments: List<Payment>) {
        val diffResult = DiffUtil.calculateDiff(
            PaymentsDiffCallback(payments, newPayments),
            true
        )
        payments.clear()
        payments.addAll(newPayments)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ru.reaperoq.test_task.R.layout.payments_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun getItemCount(): Int =
        payments.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val formatter = SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale.getDefault())
        fun bind(payment: Payment) {
            val binding = PaymentsRowBinding.bind(itemView)
            binding.paymentName.text = payment.title
            if (payment.amount != null) {
                binding.paymentAmount.visibility = View.VISIBLE
                binding.paymentAmount.text = itemView.context.resources.getString(
                    ru.reaperoq.test_task.R.string.payment_amount,
                    roundOffDecimal(payment.amount)
                )
            } else {
                binding.paymentAmount.visibility = View.GONE
            }
            if (payment.created != null) {
                binding.paymentDate.visibility = View.VISIBLE
                binding.paymentDate.text = formatter.format(payment.created * 1000L)
            } else {
                binding.paymentDate.visibility = View.GONE
            }
            binding.paymentNumber.text = itemView.context.resources.getString(
                ru.reaperoq.test_task.R.string.payment_id,
                payment.id
            )
        }
    }

    class PaymentsDiffCallback(
        private val oldData: List<Payment>,
        private val newData: List<Payment>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int =
            oldData.size

        override fun getNewListSize(): Int =
            newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].id == newData[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }
    }
}
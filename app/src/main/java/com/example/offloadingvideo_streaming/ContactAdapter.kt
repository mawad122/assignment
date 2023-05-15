import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.offloadingvideo_streaming.Contact
import com.example.offloadingvideo_streaming.R

class ContactAdapter(private val context: Context, private val contactList: List<Contact>) : BaseAdapter() {

    override fun getCount(): Int {
        return contactList.size
    }

    override fun getItem(position: Int): Contact {
        return contactList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
            viewHolder = ViewHolder()
            viewHolder.textViewName = view.findViewById(R.id.textViewName)
            viewHolder.textViewNumber = view.findViewById(R.id.textViewNumber)
            viewHolder.textViewAddress = view.findViewById(R.id.textViewAddress)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val contact = getItem(position)

        viewHolder.textViewName.text = contact.name
        viewHolder.textViewNumber.text = contact.number
        viewHolder.textViewAddress.text = contact.address

        return view
    }

    private class ViewHolder {
        lateinit var textViewName: TextView
        lateinit var textViewNumber: TextView
        lateinit var textViewAddress: TextView
    }
}

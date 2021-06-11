package net.lpscream.fragmenttest


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.properties.Delegates


class FragmentPage : Fragment() {

    private val TAG = "FragmentPage_log"

    private lateinit var notificationManager: NotificationManager

    private lateinit var crtNotificationBtn: Button
    private var pageNumber by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments?.getInt(POSITION) ?: Int.SIZE_BYTES
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__page, container, false)
        crtNotificationBtn = view.findViewById(R.id.push_button)
        createRoundedCorners(crtNotificationBtn)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(POSITION)


        crtNotificationBtn.setOnClickListener {
            createNotification(position)


            val notifyIntent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val notifyPendingIntent = PendingIntent.getActivity(
                requireContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )


            val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID).apply {
                setContentIntent(notifyPendingIntent)
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle("Chat heads active")
                setContentText("Notification " + (position + 1))
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
            }
            with(NotificationManagerCompat.from(requireContext())) {
                notify(NOTIFICATION_ID, builder.build())
            }
            Toast.makeText(requireContext(), "new notification created", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "net.lpscream.fragmenttest.channel_id"
        const val POSITION = "pageNumber"
        fun getInstance(position: Int): Fragment {
            val fragment = FragmentPage()
            val bundle = Bundle()
            bundle.putInt(POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    fun createRoundedCorners(view: View) {
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, android.R.attr.radius.toFloat())
            .build()
        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.setFillColor(
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.btn_background
            )
        )
        shapeDrawable.setStroke(
            2.0f,
            ContextCompat.getColor(requireContext(), R.color.btn_background)
        )
        ViewCompat.setBackground(view, shapeDrawable)
    }


    fun createNotification(position: Int) {
        val notificationIntent = Intent(requireContext(), MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            putExtra("menuNumber", position.toString())
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireContext(), 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Chat heads active")
            .setContentText("Notification " + (position + 1))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)


        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(position, builder.build())

        Log.d(TAG, "createNotification: position " + position)
    }
}



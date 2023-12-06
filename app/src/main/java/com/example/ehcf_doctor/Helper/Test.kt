//import android.R
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import java.util.*
//
//class MainActivity : AppCompatActivity() {
//	// initialize variables
//	var textView: TextView? = null
//	lateinit var selectedLanguage: BooleanArray
//	var langList = ArrayList<Int>()
//	var langArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")
//	override fun onCreate(savedInstanceState: Bundle?) {
//		super.onCreate(savedInstanceState)
//		setContentView(R.layout.activity_main)
//
//		// assign variable
//		textView = findViewById<TextView>(R.id.textView)
//
//		// initialize selected language array
//		selectedLanguage = BooleanArray(langArray.size)
//		textView.setOnClickListener(View.OnClickListener { // Initialize alert dialog
//			val builder = AlertDialog.Builder(this@MainActivity)
//
//			// set title
//			builder.setTitle("Select Language")
//
//			// set dialog non cancelable
//			builder.setCancelable(false)
//			builder.setMultiChoiceItems(
//				langArray, selectedLanguage
//			) { dialogInterface, i, b ->
//				// check condition
//				if (b) {
//					// when checkbox selected
//					// Add position in lang list
//					langList.add(i)
//					// Sort array list
//					Collections.sort(langList)
//				} else {
//					// when checkbox unselected
//					// Remove position from langList
//					langList.remove(Integer.valueOf(i))
//				}
//			}
//			builder.setPositiveButton(
//				"OK"
//			) { dialogInterface, i -> // Initialize string builder
//				val stringBuilder = StringBuilder()
//				// use for loop
//				for (j in langList.indices) {
//					// concat array value
//					stringBuilder.append(langArray[langList[j]])
//					// check condition
//					if (j != langList.size - 1) {
//						// When j value not equal
//						// to lang list size - 1
//						// add comma
//						stringBuilder.append(", ")
//					}
//				}
//				// set text on textView
//				textView.setText(stringBuilder.toString())
//			}
//			builder.setNegativeButton(
//				"Cancel"
//			) { dialogInterface, i -> // dismiss dialog
//				dialogInterface.dismiss()
//			}
//			builder.setNeutralButton(
//				"Clear All"
//			) { dialogInterface, i ->
//				// use for loop
//				for (j in selectedLanguage.indices) {
//					// remove all selection
//					selectedLanguage[j] = false
//					// clear language list
//					langList.clear()
//					// clear text view value
//					textView.setText("")
//				}
//			}
//			// show dialog
//			builder.show()
//		})
//	}
//}
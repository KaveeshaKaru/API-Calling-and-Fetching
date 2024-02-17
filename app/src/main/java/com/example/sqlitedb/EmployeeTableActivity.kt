package com.example.sqlitedb

import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sqlitedb.databinding.ActivityEmployeeTableBinding


class EmployeeTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeTableBinding
    private lateinit var db: DatabaseHelper
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tableLayout = findViewById(R.id.employeeTable)
        db = DatabaseHelper(this)

        val allEmployees = db.getAllEmployee()
        displayEmployeeData(allEmployees)




        binding.generatePdfBtn.setOnClickListener {
            val databaseHelper = DatabaseHelper(this)

            // Check if the external storage is writable
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val pdfPath = databaseHelper.generatePdf()
                Toast.makeText(this, "PDF generated at: $pdfPath", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "External storage is not writable", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun displayEmployeeData(employeeList: List<Employee>) {
        val tableLayout: TableLayout = findViewById(R.id.employeeTable)

        // Employee Data Rows
        for (employee in employeeList) {
            val row = TableRow(this)

            val nameTextView = createDataTextView(employee.name)
            val emailTextView = createDataTextView(employee.email)
            val addressTextView = createDataTextView(employee.address)
            val phoneTextView = createDataTextView(employee.phone)

            row.addView(nameTextView)
            row.addView(emailTextView)
            row.addView(addressTextView)
            row.addView(phoneTextView)

            // Edit button
            val editButton = createActionButton("Edit") {
                // Handle edit action
                val employee = row.tag as Employee
                showEditDialog(employee)
            }


            // Delete button
            val deleteButton = createActionButton("Delete") {

                db.deleteEmployee(employee.id, tableLayout)

                val updatedEmployees = db.getAllEmployee()
                displayEmployeeData(updatedEmployees)
            }

            row.addView(editButton)
            row.addView(deleteButton)

            row.tag = employee

            tableLayout.addView(row)
        }
    }


    private fun createDataTextView(text: String): TextView {
        return TextView(this).apply {
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            this.text = text
            gravity = Gravity.CENTER
            setPadding(17, 17, 17, 17)
        }
    }

    private fun createActionButton(text: String, onClick: () -> Unit): Button {
        return Button(this).apply {
            layoutParams = TableRow.LayoutParams(
                80,  //  width in pixels
                100   // height in pixels
            ).apply {
                setMargins(0, 0, 10, 0) // left, top, right, bottom
            }

            this.text = text
            textSize = 12f // Set the text size
            setTextColor(ContextCompat.getColor(context, R.color.white)) // text color
            setBackgroundColor(ContextCompat.getColor(context, R.color.blue2)) // background color

            setOnClickListener { onClick.invoke() }
        }
    }

    private fun showEditDialog(employee: Employee) {
        val dialogView = layoutInflater.inflate(R.layout.edit_employee_dialog, null)

        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editAddress = dialogView.findViewById<EditText>(R.id.editAddress)
        val editPhone = dialogView.findViewById<EditText>(R.id.editPhone)

        // Set the current employee data in EditTexts
        editName.setText(employee.name)
        editEmail.setText(employee.email)
        editAddress.setText(employee.address)
        editPhone.setText(employee.phone)

        val alertDialog = AlertDialog.Builder(this).setTitle("").setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Handle the save button click
                val updatedName = editName.text.toString()
                val updatedEmail = editEmail.text.toString()
                val updatedAddress = editAddress.text.toString()
                val updatedPhone = editPhone.text.toString()

                // Update the employee data
                val updatedEmployee =
                    Employee(employee.id, updatedName, updatedEmail, updatedAddress, updatedPhone)

                // Call your editEmployee function here with the updated data
                val rowsUpdated = db.editEmployee(updatedEmployee)

                if (rowsUpdated > 0) {
                    // Update the UI or show a message
                    val updatedEmployees = db.getAllEmployee()
                    displayEmployeeData(updatedEmployees)
                    Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Show an error message
                    Toast.makeText(this, "Failed to update employee", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("Cancel", null).create()

        alertDialog.show()
    }


}
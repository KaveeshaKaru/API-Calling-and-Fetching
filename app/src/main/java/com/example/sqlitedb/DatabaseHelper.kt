package com.example.sqlitedb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.TableLayout
import android.widget.TableRow
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        //Employee
        private const val EMPLOYEE_TABLE_NAME = "employee"
        private const val EMPLOYEE_COLUMN_ID = "emp_id"
        private const val EMPLOYEE_COLUMN_NAME = "name"
        private const val EMPLOYEE_COLUMN_EMAIL = "employee_email"
        private const val EMPLOYEE_COLUMN_ADDRESS = "address"
        private const val EMPLOYEE_COLUMN_PHONE = "phone"

        // Items
        const val ITEMS_TABLE = "Items"
        const val ITEMS_COLUMN_ID = "items_id"
        const val ITEMS_COLUMN_NAME = "items_name"
        const val ITEMS_COLUMN_CODE = "items_code"
        const val ITEMS_COLUMN_PRINTING_NAME = "items_printing_name"
        const val ITEMS_COLUMN_PRICE = "items_price"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createTableQuery)

        //Employee Table Creation
        val createEmployeeTableQuery = (
                "CREATE TABLE $EMPLOYEE_TABLE_NAME (" +
                        "$EMPLOYEE_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$EMPLOYEE_COLUMN_NAME TEXT, " +
                        "$EMPLOYEE_COLUMN_EMAIL TEXT, " +
                        "$EMPLOYEE_COLUMN_ADDRESS TEXT, " +
                        "$EMPLOYEE_COLUMN_PHONE TEXT)"
                )
        db?.execSQL(createEmployeeTableQuery)

        // Items Table Creation
        val createItemsTableQuery = (
                "CREATE TABLE $ITEMS_TABLE (" +
                        "$ITEMS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$ITEMS_COLUMN_NAME TEXT, " +
                        "$ITEMS_COLUMN_CODE TEXT, " +
                        "$ITEMS_COLUMN_PRINTING_NAME TEXT, " +
                        "$ITEMS_COLUMN_PRICE REAL)"
                )
        db?.execSQL(createItemsTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)

        //drop Employee table
        val dropEmployeeTableQuery = "DROP TABLE IF EXISTS $EMPLOYEE_TABLE_NAME"
        db?.execSQL(dropEmployeeTableQuery)


        onCreate(db)
    }

    fun insertUser(email: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists

    }

    //Employee Insert
    fun insertEmployee(name: String, email: String, address: String, phone: String): Long {
        val values = ContentValues().apply {
            put(EMPLOYEE_COLUMN_NAME, name)
            put(EMPLOYEE_COLUMN_EMAIL, email)
            put(EMPLOYEE_COLUMN_ADDRESS, address)
            put(EMPLOYEE_COLUMN_PHONE, phone)
        }
        val db = writableDatabase
        return db.insert(EMPLOYEE_TABLE_NAME, null, values)
    }

    fun readEmployee(name: String, email: String, address: String, phone: String): Boolean {
        val db = readableDatabase
        val selection =
            "$EMPLOYEE_COLUMN_NAME = ? AND $EMPLOYEE_COLUMN_EMAIL = ? AND $EMPLOYEE_COLUMN_ADDRESS = ? AND $EMPLOYEE_COLUMN_PHONE = ?"
        val selectionArgs = arrayOf(name, email, address, phone)
        val cursor = db.query(EMPLOYEE_TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val employeeExists = cursor.count > 0

        cursor.close()
        return employeeExists
    }

    fun getAllEmployee(): List<Employee> {

        val empList = mutableListOf<Employee>()
        val db = readableDatabase
        val query = "SELECT * FROM $EMPLOYEE_TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(EMPLOYEE_COLUMN_ID))
            val empName = cursor.getString(cursor.getColumnIndexOrThrow(EMPLOYEE_COLUMN_NAME))
            val empEmail = cursor.getString(cursor.getColumnIndexOrThrow(EMPLOYEE_COLUMN_EMAIL))
            val empAddress = cursor.getString(cursor.getColumnIndexOrThrow(EMPLOYEE_COLUMN_ADDRESS))
            val empPhone = cursor.getString(cursor.getColumnIndexOrThrow(EMPLOYEE_COLUMN_PHONE))

            val emp = Employee(id, empName, empEmail, empAddress, empPhone)
            empList.add(emp)

        }
        cursor.close()
        db.close()
        return empList


    }

    fun deleteEmployee(employeeId: Long, tableLayout: TableLayout): Int {
        val db = writableDatabase
        val whereClause = "$EMPLOYEE_COLUMN_ID = ?"
        val whereArgs = arrayOf(employeeId.toString())

        // Delete from the database
        val rowsDeleted = db.delete(EMPLOYEE_TABLE_NAME, whereClause, whereArgs)

        if (rowsDeleted > 0) {
            // If the deletion was successful, remove the corresponding row from the UI
            val rowCount = tableLayout.childCount

            for (i in 1 until rowCount) {
                val row = tableLayout.getChildAt(i) as TableRow
                val employee = row.tag as Employee

                if (employee.id == employeeId) {
                    tableLayout.removeView(row)
                    break
                }
            }
        }

        return rowsDeleted
    }

    fun editEmployee(employee: Employee): Int {
        val values = ContentValues().apply {
            put(EMPLOYEE_COLUMN_NAME, employee.name)
            put(EMPLOYEE_COLUMN_EMAIL, employee.email)
            put(EMPLOYEE_COLUMN_ADDRESS, employee.address)
            put(EMPLOYEE_COLUMN_PHONE, employee.phone)
        }
        val db = writableDatabase
        val whereClause = "$EMPLOYEE_COLUMN_ID = ?"
        val whereArgs = arrayOf(employee.id.toString())

        val rowsUpdated = db.update(EMPLOYEE_TABLE_NAME, values, whereClause, whereArgs)

        db.close()

        return rowsUpdated
    }


    fun generatePdf(): String {
        val pdfFileName = "database_export.pdf"
        val pdfPath =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$pdfFileName"

        val document = Document()

        val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(pdfPath))
        document.open()

        document.add(Paragraph("Employee Database"))

        val employeeList = getAllEmployee()

        for (employee in employeeList) {
            document.add(
                Paragraph(
                    "ID: ${employee.id}\n" +
                            "Name: ${employee.name}\n" +
                            "Email: ${employee.email}\n" +
                            "Address: ${employee.address}\n" +
                            "Phone: ${employee.phone}\n\n"
                )
            )
        }

        document.close()

        return pdfPath
    }

    //////////////////////// Items table Queries  ////////////////////////////////////////

    // Items Insert
    fun insertItem(name: String, code: String, printingName: String, price: Double): Long {
        val values = ContentValues().apply {
            put(ITEMS_COLUMN_NAME, name)
            put(ITEMS_COLUMN_CODE, code)
            put(ITEMS_COLUMN_PRINTING_NAME, printingName)
            put(ITEMS_COLUMN_PRICE, price)
        }
        val db = writableDatabase
        return db.insert(ITEMS_TABLE, null, values)
    }


}
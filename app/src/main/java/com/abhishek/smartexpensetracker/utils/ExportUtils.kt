package com.abhishek.smartexpensetracker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.abhishek.smartexpensetracker.data.model.Expense
import java.io.File
import java.io.FileOutputStream

object ExportUtils {
    /**
     * Write CSV to cache and return Uri (does not share directly).
     */
    fun writeCsvToCache(context: Context, fileName: String, csvContent: String): Uri? {
        return try {
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { it.write(csvContent.toByteArray()) }
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    /**
     * Share CSV file given its Uri.
     */
    fun shareCsv(context: Context, uri: Uri) {
        try {
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(share, "Share report"))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(context, "Unable to share file", Toast.LENGTH_SHORT).show()
        }
    }

    fun buildCsvFromExpenses(expenses: List<Expense>): String {
        val header = listOf("id","title","amount","category","notes","receiptUri","timestamp").joinToString(",")
        val rows = expenses.joinToString("\n") {
            listOf(
                it.id,
                escapeCsv(it.title),
                it.amount.toString(),
                escapeCsv(it.category),
                escapeCsv(it.notes ?: ""),
                it.receiptUri ?: "",
                it.timestamp.toString()
            ).joinToString(",")
        }
        return header + "\n" + rows
    }

    private fun escapeCsv(value: String): String {
        var v = value.replace("\"", "\"\"")
        if (v.contains(",") || v.contains("\n")) v = "\"$v\""
        return v
    }
}

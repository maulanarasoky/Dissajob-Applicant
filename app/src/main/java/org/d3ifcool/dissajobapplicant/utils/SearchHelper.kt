package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.ui.search.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.DeleteSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.LoadSearchHistoryCallback

object SearchHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrSearchHistory: MutableList<SearchHistoryResponseEntity> = mutableListOf()

    fun getSearchHistories(applicantId: String, callback: LoadSearchHistoryCallback) {
        database.orderByChild("applicant_id").equalTo(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrSearchHistory.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val searchHistory = SearchHistoryResponseEntity(
                                data.key.toString(),
                                data.child("search_text").value.toString(),
                                data.child("search_date").value.toString(),
                                data.child("applicant_id").value.toString()
                            )
                            arrSearchHistory.add(searchHistory)
                        }
                    }
                    callback.onAllSearchHistoriesReceived(arrSearchHistory)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    fun addSearchHistory(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    ) {
        database.orderByChild("searchText").equalTo(searchHistory.searchText.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        updateToDatabase(
                            dataSnapshot.key.toString(),
                            dataSnapshot.child("searchDate").toString(),
                            callback
                        )
                    } else {
                        insertToDatabase(searchHistory, callback)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    fun insertToDatabase(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    ) {
        database.child(searchHistory.id.toString()).setValue(searchHistory).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_insert)
        }
    }

    fun updateToDatabase(
        historyId: String,
        searchDate: String,
        callback: AddSearchHistoryCallback
    ) {
        database.child(historyId)
            .child("searchDate")
            .setValue(searchDate).addOnSuccessListener {
                callback.onSuccess()
            }.addOnFailureListener {
                callback.onFailure(R.string.txt_failure_insert)
            }
    }

    fun deleteAllSearchHistory(applicantId: String, callback: DeleteSearchHistoryCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .removeEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback.onSuccess()
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onFailure(R.string.txt_failure_delete)
                }

            })
    }

    fun deleteSearchHistoryById(searchHistoryId: String, callback: DeleteSearchHistoryCallback) {
        database.child(searchHistoryId).removeValue().addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_delete)
        }
    }
}
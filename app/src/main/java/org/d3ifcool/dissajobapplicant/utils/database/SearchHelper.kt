package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.ui.search.callback.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.callback.DeleteAllSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.callback.DeleteSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.callback.LoadSearchHistoryCallback

object SearchHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("search_history")
    private val arrSearchHistory: MutableList<SearchHistoryResponseEntity> = mutableListOf()

    fun getSearchHistories(applicantId: String, callback: LoadSearchHistoryCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrSearchHistory.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val searchHistory = SearchHistoryResponseEntity(
                                data.key.toString(),
                                data.child("searchText").value.toString(),
                                data.child("searchDate").value.toString(),
                                data.child("applicantId").value.toString()
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
                        for (items in dataSnapshot.children) {
                            updateToDatabase(
                                items.key.toString(),
                                searchHistory.searchDate.toString(),
                                callback
                            )
                        }
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
        searchHistory.id = database.push().key
        database.child(searchHistory.id.toString()).setValue(searchHistory).addOnSuccessListener {
            callback.onSuccessAdding()
        }.addOnFailureListener {
            callback.onFailureAdding(R.string.txt_failure_insert)
        }
    }

    fun updateToDatabase(
        historyId: String,
        searchDate: String,
        callback: AddSearchHistoryCallback
    ) {
        database.child(historyId).child("searchDate")
            .setValue(searchDate).addOnSuccessListener {
                callback.onSuccessAdding()
            }.addOnFailureListener {
                callback.onFailureAdding(R.string.txt_failure_insert)
            }
    }

    fun deleteAllSearchHistories(applicantId: String, callback: DeleteAllSearchHistoryCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .removeEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback.onSuccessDeleteAllHistory()
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onFailureDeleteAllHistory(R.string.txt_failure_delete)
                }

            })
    }

    fun deleteSearchHistoryById(searchHistoryId: String, callback: DeleteSearchHistoryCallback) {
        database.child(searchHistoryId).removeValue().addOnSuccessListener {
            callback.onSuccessDelete()
        }.addOnFailureListener {
            callback.onFailureDelete(R.string.txt_failure_delete)
        }
    }
}
package org.d3ifcool.dissajobapplicant.ui.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.JobHelper
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback

object Injection {
    fun provideJobRepository(context: Context): JobRepository {
        val database = DissajobRecruiterDatabase.getInstance(context)

        val remoteDataSource = RemoteJobSource.getInstance(JobHelper)
        val localDataSource = LocalJobSource.getInstance(database.jobDao())
        val appExecutors = AppExecutors()

        val callback = object : NetworkStateCallback {
            override fun hasConnectivity(): Boolean {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }
        }

        return JobRepository.getInstance(remoteDataSource, localDataSource, appExecutors, callback)
    }
}
package org.d3ifcool.dissajobapplicant.ui.media.callback

import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity

interface OnClickEditMediaListener {
    fun onClickBtnEdit(mediaData: MediaEntity)
}
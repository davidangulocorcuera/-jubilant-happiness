package com.example.justfivemins.modules.content


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.R.layout.fragment_content
import com.example.justfivemins.model.Article
import com.example.justfivemins.model.CellItem
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.base.Configurator
import com.example.justfivemins.modules.content.content_list_adapter.ContentListAdapter
import com.example.justfivemins.modules.profile_data.data_list_adapter.ProfileDataListAdapter
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.item_content_card_layout.*

class ContentFragment : BaseFragment() {
    private var cards: ArrayList<CellItem> = ArrayList()
    private lateinit var contentListAdapter: ContentListAdapter

    override fun onCreateViewId(): Int {
        return fragment_content
    }

    override fun viewCreated(view: View?) {
        cards.addAll(
            arrayOf(
                Article("Jesus Cebollo", "Clásica persona humana",1),
                Article("Cebollo Rodriguez", "No clásica persona humana",1),
                Article("Jesus Cebollo", "Clásica persona humana",1),
                Article("Cebollo Rodriguez", "No clásica persona humana",1)
            )
        )
        val configurator: Configurator? = null
        setToolbarTitle(getString(R.string.home).toUpperCase())

        configurator?.hasToolbar = true
        initList(recyclerViewContentList)
    }
    private fun initList(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(activity,0,false)
        recyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        contentListAdapter =
            ContentListAdapter(cards)
        recyclerViewContentList.adapter = contentListAdapter
    }
    fun setButtonListener(){
        btn_location.setOnClickListener {
        }
    }

}

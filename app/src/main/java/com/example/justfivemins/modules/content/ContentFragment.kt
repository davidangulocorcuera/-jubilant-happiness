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
import com.example.justfivemins.modules.content.adapters.ContentListAdapter
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.item_content_card_layout.*

class ContentFragment : BaseFragment() {
    private var cards: ArrayList<CellItem> = ArrayList()

    override fun onCreateViewId(): Int {
        return fragment_content
    }
    private val mListItemListener = object : ContentListAdapter.OnCellItemClickListener {
        override fun onItemClick(item: CellItem) {

        }
    }

    override fun viewCreated(view: View?) {
        var configurator: Configurator? = null
        setToolbarTitle(getString(R.string.profiles_title))

        configurator?.hasToolbar = true
        recyclerViewContentList.apply {
            layoutManager = LinearLayoutManager(context,0,false) as RecyclerView.LayoutManager?
            cards.addAll(
                arrayOf(
                    Article("Jesus Cebollo", "Clásica persona humana",1),
                    Article("Cebollo Rodriguez", "No clásica persona humana",1),
                    Article("Jesus Cebollo", "Clásica persona humana",1),
                    Article("Cebollo Rodriguez", "No clásica persona humana",1)
                )
            )
            adapter = ContentListAdapter(cards, mListItemListener)

        }
    }
    fun setButtonListener(){
        btn_location.setOnClickListener {
        }
    }
    companion object {
        fun newInstance(): ContentFragment {
            return ContentFragment()
        }
    }

}

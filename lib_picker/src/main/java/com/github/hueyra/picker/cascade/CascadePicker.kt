package com.github.hueyra.picker.cascade

import android.annotation.SuppressLint
import android.content.Context
import com.github.hueyra.picker.base.IPickerView
import com.orhanobut.dialogplus.DialogPlus
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.hueyra.picker.cascade.core.CascadeAdapter
import com.github.hueyra.picker.cascade.core.ICascadePickerData
import android.view.LayoutInflater
import android.widget.ImageView
import com.github.hueyra.picker.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.orhanobut.dialogplus.ViewHolder
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by zhujun
 * Date : 2022-04-14
 * Desc : 级联Picker
 */
@SuppressLint("NotifyDataSetChanged")
class CascadePicker(context: Context) : IPickerView {

    private val mDialogPlus: DialogPlus
    private val mCplTvTitle: TextView
    private val mCplIvCancel: ImageView
    private val mCplTyTab: TabLayout
    private val mCplRvContent: RecyclerView

    private var mOnItemPickedListener: ((List<ICascadePickerData>) -> Unit)? = null
    private val mPickerAdapter: CascadeAdapter
    private val mICascadePickerDataList: MutableList<ICascadePickerData>
    private val mPickerCascadeMap: MutableMap<String, List<ICascadePickerData>>
    private var mTabLayoutCanDealSelect = true

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_cascade_picker, null)
        //
        mCplTvTitle = view.findViewById(R.id.cpl_tv_title)
        mCplIvCancel = view.findViewById(R.id.cpl_iv_cancel)
        mCplTyTab = view.findViewById(R.id.cpl_ty_tab)
        mCplRvContent = view.findViewById(R.id.cpl_rv_content)
        //
        mPickerCascadeMap = HashMap()
        mICascadePickerDataList = ArrayList()
        //
        mPickerAdapter = CascadeAdapter(context, mICascadePickerDataList);
        mCplRvContent.layoutManager = LinearLayoutManager(context)
        mCplRvContent.adapter = mPickerAdapter
        mPickerAdapter.setOnItemClickListener {
            onItemClick(it)
        }
        //layout inflate
        mDialogPlus = DialogPlus.newDialog(context)
            .setContentHolder(ViewHolder(view))
            .setContentBackgroundResource(R.color.color_dialog_plus_bg)
            .create()
        //
        mCplIvCancel.setOnClickListener { hide() }
        mCplTyTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.tag != null && mTabLayoutCanDealSelect) {
                    val entity = tab.tag as ICascadePickerData?
                    if (entity!!.getPickerParentID() != mICascadePickerDataList[0].getPickerParentID()) {
                        val list = mPickerCascadeMap[entity.getPickerParentID()]
                        if (list != null && list.isNotEmpty()) {
                            mICascadePickerDataList.clear()
                            mICascadePickerDataList.addAll(list)
                            mPickerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    /**
     * 设置数据源
     * */
    fun setPickerDataSource(map: Map<String, List<ICascadePickerData>>, firstTabParentID: String) {
        val keys = map.keys
        keys.forEach {
            map[it]?.let { list -> mPickerCascadeMap[it] = list }
        }
        showFirstTab(firstTabParentID)
    }

    /**
     * 设置数据源
     * */
    fun setPickerDataSource(list: List<List<ICascadePickerData>>, firstTabParentID: String) {
        for (i in list.indices) {
            mPickerCascadeMap[list[i][0].getPickerParentID()] = list[i]
        }
        showFirstTab(firstTabParentID)
    }

    private fun showFirstTab(tabID: String) {
        if (mPickerCascadeMap.containsKey(tabID)) {
            //先默认添加一个tab
            val tab = mCplTyTab.newTab()
            tab.tag = null //
            tab.text = "请选择"
            mCplTyTab.addTab(tab)
            //
            val list = mPickerCascadeMap[tabID]
            if (list != null) {
                mICascadePickerDataList.clear()
                mICascadePickerDataList.addAll(list)
                mPickerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setTitle(title: String) {
        mCplTvTitle.text = title
    }

    override fun hide() {
        mDialogPlus.dismiss()
    }

    override fun show() {
        mDialogPlus.show()
    }

    private fun onItemClick(position: Int) {
        mTabLayoutCanDealSelect = false
        //先处理一下tabLayout，把当前选中的tab后的所有tab删除掉
        if (mCplTyTab.selectedTabPosition != mCplTyTab.tabCount - 1) {
            //如果不是最后一个
            val tempTabs: MutableList<TabLayout.Tab> = ArrayList()
            for (i in mCplTyTab.selectedTabPosition + 1 until mCplTyTab.tabCount) {
                val tab = mCplTyTab.getTabAt(i)
                //先把后面tab所选中的数据源设置未选中
                if (tab != null && tab.tag != null) {
                    val pick = tab.tag as ICascadePickerData
                    val pid: String = pick.getPickerParentID()
                    if (mPickerCascadeMap.containsKey(pid)) {
                        val temp = mPickerCascadeMap[pid]
                        if (temp != null && temp.isNotEmpty()) {
                            for (entity in temp) {
                                entity.isCascadePicked = false
                            }
                        }
                    }
                    tempTabs.add(tab)
                }
            }
            for (tab in tempTabs) {
                mCplTyTab.removeTab(tab)
            }
        }
        val entity = mICascadePickerDataList[position]
        var tab = mCplTyTab.getTabAt(mCplTyTab.selectedTabPosition)
        if (tab != null) {
            tab.tag = entity
            tab.text = entity.getPickerValue()
        } else {
            tab = mCplTyTab.newTab()
            tab.tag = entity
            tab.text = entity.getPickerValue()
            mCplTyTab.addTab(tab)
            mCplTyTab.selectTab(tab)
        }
        //
        for (i in mICascadePickerDataList.indices) {
            mICascadePickerDataList[i].isCascadePicked = (i == position)
        }
        mPickerAdapter.notifyDataSetChanged()
        val temp: List<ICascadePickerData> = ArrayList(mICascadePickerDataList)
        mPickerCascadeMap[mICascadePickerDataList[0].getPickerParentID()] = temp
        //
        if (mPickerCascadeMap.containsKey(entity.getPickerID())) {
            val subs = mPickerCascadeMap[entity.getPickerID()]
            if (subs != null) {
                mICascadePickerDataList.clear()
                mICascadePickerDataList.addAll(subs)
                mPickerAdapter.notifyDataSetChanged()
                val tabNew = mCplTyTab.newTab()
                tabNew.tag = mICascadePickerDataList[position]
                tabNew.text = "请选择"
                mCplTyTab.addTab(tabNew)
                mCplTyTab.selectTab(tabNew)
                mTabLayoutCanDealSelect = true
                return
            }
        }
        //
        if (mOnItemPickedListener != null) {
            val returnList = mutableListOf<ICascadePickerData>()
            for (i in 0 until mCplTyTab.tabCount) {
                val t = mCplTyTab.getTabAt(i)
                if (t != null && t.tag != null) {
                    returnList.add(t.tag as ICascadePickerData)
                }
            }
            mOnItemPickedListener?.invoke(returnList)
            hide()
        }
        mTabLayoutCanDealSelect = true
    }

    fun setOnItemPickedListener(l: (List<ICascadePickerData>) -> Unit) {
        mOnItemPickedListener = l
    }

}
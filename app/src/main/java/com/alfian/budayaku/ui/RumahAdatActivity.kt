package com.alfian.budayaku.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfian.budayaku.database.RumahEntity
import com.alfian.budayaku.databinding.ActivityRumahAdatBinding
import com.alfian.budayaku.helper.ViewModelFactory
import com.alfian.budayaku.adapter.RumahPagedListAdapter
import com.alfian.budayaku.ui.viewmodel.RumahViewModel


class RumahAdatActivity : AppCompatActivity() {

    private lateinit var adapter: RumahPagedListAdapter

    private lateinit var rumahViewModel: RumahViewModel

    private lateinit var binding: ActivityRumahAdatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRumahAdatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rumahViewModel = obtainViewModel(this)
        rumahViewModel.getAllBudaya().observe(this, noteObserver)

        adapter = RumahPagedListAdapter(this)
        binding.rvListBudaya.layoutManager = LinearLayoutManager(this)
        binding.rvListBudaya.setHasFixedSize(true)
        binding.rvListBudaya.adapter = adapter

        var query = ""
        binding.editTextTextPersonName.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    rumahViewModel.searchRumah(query).observe(this@RumahAdatActivity, noteObserver)

                    //hide keyboard
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v?.windowToken, 0)

                    return true
                }
                return false
            }
        })

        //enter keyboard
        binding.editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                query = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    private fun obtainViewModel(activity: AppCompatActivity): RumahViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(RumahViewModel::class.java)
    }

    //tes
    private val noteObserver = Observer<PagedList<RumahEntity>> { noteList ->
        if (noteList != null) {
            adapter.submitList(noteList)
        }
    }


}
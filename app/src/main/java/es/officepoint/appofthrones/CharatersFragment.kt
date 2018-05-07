package es.officepoint.appofthrones

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_charaters.*


class CharatersFragment: Fragment() {

    val list : RecyclerView by lazy {

        val list : RecyclerView = view!!.findViewById(R.id.list)
        list.layoutManager = LinearLayoutManager(context)
        list
    }

    val adapter : CharactersAdapter by lazy {
        val adapter = CharactersAdapter() {item, position ->
            //showDetails(item.id)
            clickListener.onItemClicked(item)
        }
        adapter
    }

    lateinit var clickListener: OnItemClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnItemClickListener)
            clickListener = context
        else
            throw IllegalArgumentException("Attached activity doesnt implement CharacterFragment.OnItemClickListener")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        val characters : MutableList<Character> = CharacterRepo.characters
        adapter.setCharacters(characters)
*/

        list.adapter = adapter

        btnRetry.setOnClickListener {
            retry()
        }
    }

    override fun onResume(){
        super.onResume()
        requestCharacters()
    }

    private fun retry(){
        layoutError.visibility = View.INVISIBLE
        progressbar.visibility = View.VISIBLE

        requestCharacters()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charaters, container, false)
    }


    private fun requestCharacters(){
        CharacterRepo.requestCharacters(context,
                { characters ->
                    view?.let {
                        progressbar.visibility = View.INVISIBLE
                        list.visibility = View.VISIBLE
                        adapter.setCharacters(characters)
                    }
                },
                {
                    view?.let {
                        progressbar.visibility = View.INVISIBLE
                        layoutError.visibility = View.VISIBLE
                    }
                })
    }


    interface  OnItemClickListener{
        fun onItemClicked(character: Character)
    }
}
package com.mun.bonecci.encryptedroomdb.presentation.user_list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mun.bonecci.encryptedroomdb.R
import com.mun.bonecci.encryptedroomdb.commons.ToolbarButtonVisibilityListener
import com.mun.bonecci.encryptedroomdb.commons.safeNavigate
import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.databinding.FragmentUserListBinding
import com.mun.bonecci.encryptedroomdb.presentation.UserState
import com.mun.bonecci.encryptedroomdb.presentation.UserViewModel
import com.mun.bonecci.encryptedroomdb.presentation.adapter.UserListAdapter


/**
 * A fragment to display a list of users.
 */
class UserListFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var userListAdapter: UserListAdapter
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private var toolbarButtonVisibilityListener: ToolbarButtonVisibilityListener? = null

    /**
     * Called when the fragment is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        val activity = activity as AppCompatActivity?

        activity?.let {
            val toolbarButton = it.findViewById<ImageButton>(R.id.toolbar_button)
            toolbarButton.setOnClickListener {
                viewModel.addUser()
            }
        }

        return binding.root
    }

    /**
     * Called immediately after onCreateView() and onViewCreated().
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        observeUserData()
        viewModel.fetchUserData()
        setToolbarButtonVisibility()
    }

    /**
     * Called when the fragment is no longer in use.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * Called when a fragment is first attached to its context.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarButtonVisibilityListener) {
            toolbarButtonVisibilityListener = context
        } else {
            throw RuntimeException("$context must implement ToolbarButtonVisibilityListener")
        }
    }

    /**
     * Initializes the recycler view.
     */
    private fun initRecycler() {
        userListAdapter =
            UserListAdapter(clickListener = (object : UserListAdapter.OnClickListener {
                override fun onItemClick(user: User, position: Int) {
                    userListAdapter.setSelectedItem(position)
                    findNavController().safeNavigate(
                        R.id.action_userListFragment_to_userDetailFragment,
                        Bundle().apply {
                            putString("id", user.id.toString())
                        })
                }

                override fun onDeletePressed(user: User, position: Int) {
                    viewModel.removeUserData(user.id.toString())
                    userListAdapter.setSelectedItem(-1)
                }

            }), requireActivity())

        binding.userRecyclerView.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    /**
     * Observes the user data changes.
     */
    private fun observeUserData() {
        viewModel.userState.observe(viewLifecycleOwner) { state ->
            validateUserState(state)
        }
    }

    /**
     * Validates the user state and updates the UI accordingly.
     */
    private fun validateUserState(state: UserState) {
        state.users.let { users ->
            userListAdapter.submitList(users)
        }

        if (state.error.isNotBlank()) {
            Toast.makeText(requireActivity(), state.error, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sets the visibility of toolbar button.
     */
    private fun setToolbarButtonVisibility() {
        toolbarButtonVisibilityListener?.onToolbarButtonVisibilityChanged(true)
    }
}
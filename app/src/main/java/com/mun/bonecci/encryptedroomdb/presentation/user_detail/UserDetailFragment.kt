package com.mun.bonecci.encryptedroomdb.presentation.user_detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mun.bonecci.encryptedroomdb.R
import com.mun.bonecci.encryptedroomdb.commons.ToolbarButtonVisibilityListener
import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.databinding.FragmentUserDetailBinding
import com.mun.bonecci.encryptedroomdb.presentation.UserViewModel

/**
 * A fragment to display details of a user.
 */
class UserDetailFragment : Fragment() {

    private var id: String? = ""
    private lateinit var viewModel: UserViewModel
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private var toolbarButtonVisibilityListener: ToolbarButtonVisibilityListener? = null

    /**
     * Called when the fragment is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID_PARAM)
        }
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView() and onViewCreated().
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id?.let {
            viewModel.getUserFromId(it)
            viewModel.userData.observe(viewLifecycleOwner) { user ->
                setUserData(user)
            }
        }
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
     * Sets the user data to the UI.
     */
    @SuppressLint("SetTextI18n")
    private fun setUserData(user: User) {
        binding.userNameTextView.text = "Name: ${user.name}"
        binding.userEmailTextView.text = "Email: ${user.email}"
        binding.userIDTextView.text = user.id.toString()
        binding.iconImage.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.baseline_admin_panel_settings_24
            )
        )
    }

    /**
     * Sets the visibility of toolbar button.
     */
    private fun setToolbarButtonVisibility() {
        toolbarButtonVisibilityListener?.onToolbarButtonVisibilityChanged(false)
    }

    companion object {
        const val ID_PARAM = "id"
    }

}
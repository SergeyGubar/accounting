package io.github.gubarsergey.accounting.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsAddAccountButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToAddAccountFragment())
        }
    }
}
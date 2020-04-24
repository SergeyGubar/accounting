package io.github.gubarsergey.accounting.ui.settings

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.databinding.FragmentSettingsBinding
import io.github.gubarsergey.accounting.util.SharedPrefHelper

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
        binding.settingsAddCategoryButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToAddCategoryFragment())
        }
        binding.settingsLogoutButton.setOnClickListener {
            SharedPrefHelper().clearToken(requireContext())
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToLoginFragment())
        }
        binding.settingsCategoriesTotalSpentButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToCategoryTotalSpentFragment())
        }
        binding.settingsTimeReportButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToTimeRangeReportFragment())
        }
    }
}
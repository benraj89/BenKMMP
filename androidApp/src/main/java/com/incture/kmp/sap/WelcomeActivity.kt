package com.incture.kmp.sap

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.incture.kmp.R
import com.incture.kmp.ui.MainActivity
import com.incture.kmp.utils.MyApplicationTheme
import com.sap.cloud.mobile.flows.compose.core.FlowContext
import com.sap.cloud.mobile.flows.compose.ext.FlowOptions
import com.sap.cloud.mobile.flows.compose.flows.FlowUtil
import com.sap.cloud.mobile.foundation.configurationprovider.FileConfigurationProvider
import com.sap.cloud.mobile.foundation.configurationprovider.ProviderConfiguration
import com.sap.cloud.mobile.foundation.configurationprovider.ProviderInputs
import com.sap.cloud.mobile.foundation.mobileservices.TimeoutLockService
import com.sap.cloud.mobile.foundation.model.AppConfig
import com.sap.cloud.mobile.onboarding.compose.settings.CustomScreenSettings
import com.sap.cloud.mobile.onboarding.compose.settings.LaunchScreenContentSettings
import com.sap.cloud.mobile.onboarding.compose.settings.LaunchScreenSettings
import com.sap.cloud.mobile.onboarding.compose.settings.QRCodeReaderScreenSettings

class WelcomeActivity : ComponentActivity() {

    private lateinit var providerConfiguration: ProviderConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            UI()
        }
    }

    @Preview
    @Composable
    private fun UI() {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Body()
            }
        }
    }

    @Composable
    private fun Body()
    {
        var showError by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            providerConfiguration = loadConfiguration(this@WelcomeActivity)
            if (providerConfiguration.providerSuccess) {
                val appConfig = AppConfig.createAppConfigFromJsonString(providerConfiguration.configuration.toString())
                startOnboarding(this@WelcomeActivity, appConfig)
            } else {
                showError = true
            }
        }

        if (showError) {
            val errorMessage = providerConfiguration.returnError?.errorMessage
            AlertDialogComponent(
                text = errorMessage.toString(),
                onPositiveButtonClick = {
                    this.finish()
                }
            )
        }
    }

    private fun loadConfiguration(context: Context): ProviderConfiguration {
        return FileConfigurationProvider(
            context, "sap_mobile_services"
        ).provideConfiguration(
            ProviderInputs()
        )
    }

    private fun startOnboarding(context: Context, appConfig: AppConfig) {
        TimeoutLockService.updateApplicationLockState(true)
        FlowUtil.startFlow(
            context,
            flowContext = getOnboardingFlowContext(context, appConfig)
        ) { resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                launchDashBoardActivity(context)
            } else {
                startOnboarding(context, appConfig)
            }
        }
    }

    private fun prepareScreenSettings() =
        CustomScreenSettings(
            launchScreenSettings = LaunchScreenSettings(
                titleResId = R.string.application_name,
                contentSettings = LaunchScreenContentSettings(
                    title = R.string.sap_launch_screen_content_title,
                    //content = R.string.launch_screen_content_body,
                    //contentImage = R.drawable.ic_sap_icon_sdk_transparent,
                    contentImageHeight = 120.dp
                ),
                bottomPrivacyUrl = "http://www.sap.com"
            ),
            qrCodeReaderScreenSettings = QRCodeReaderScreenSettings(
                scanInternal = 50L
            )
        )

    private fun getOnboardingFlowContext(context: Context, appConfig: AppConfig) = FlowContext(
        appConfig = appConfig,
        flowStateListener = WizardFlowStateListener(context.applicationContext as SAPWizardApplication),
        flowOptions = FlowOptions(
            //oAuthAuthenticationOption = OAuth2WebOption.WEB_VIEW,
            useDefaultEulaScreen = false,
            screenSettings = prepareScreenSettings(),
            fullScreen = false
        )
    )

    private fun launchDashBoardActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }

}



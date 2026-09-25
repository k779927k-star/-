package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.FlowStudioScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonPurple
import com.example.ui.viewmodel.FlowViewModel

enum class AppNavScreen {
    HOME,
    STUDIO,
    CHAT,
    TEMPLATES
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KaledApp()
            }
        }
    }
}

@Composable
fun KaledApp() {
    val viewModel: FlowViewModel = viewModel()
    var currentScreen by remember { mutableStateOf(AppNavScreen.HOME) }
    val selectedProject by viewModel.selectedProject.collectAsState()

    BackHandler(enabled = currentScreen != AppNavScreen.HOME) {
        currentScreen = AppNavScreen.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            if (currentScreen != AppNavScreen.STUDIO) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .border(
                            width = 0.5.dp,
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                ) {
                    NavigationBar(
                        containerColor = DarkSurface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == AppNavScreen.HOME,
                            onClick = { currentScreen = AppNavScreen.HOME },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Layers,
                                    contentDescription = "المشاريع"
                                )
                            },
                            label = {
                                Text(
                                    text = "مشاريعي",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen == AppNavScreen.HOME) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = Color(0x3300F5FF),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_home")
                        )

                        NavigationBarItem(
                            selected = currentScreen == AppNavScreen.CHAT,
                            onClick = { currentScreen = AppNavScreen.CHAT },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "مساعد KALED"
                                )
                            },
                            label = {
                                Text(
                                    text = "مساعد AI",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen == AppNavScreen.CHAT) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonPurple,
                                selectedTextColor = NeonPurple,
                                indicatorColor = Color(0x33A855F7),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_chat")
                        )

                        NavigationBarItem(
                            selected = currentScreen == AppNavScreen.TEMPLATES,
                            onClick = { currentScreen = AppNavScreen.TEMPLATES },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "قوالب مجانية"
                                )
                            },
                            label = {
                                Text(
                                    text = "قوالب Flow",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentScreen == AppNavScreen.TEMPLATES) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = Color(0x3300F5FF),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.testTag("nav_item_templates")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppNavScreen.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToProject = { project ->
                            viewModel.selectProject(project)
                            currentScreen = AppNavScreen.STUDIO
                        },
                        onNavigateToChat = {
                            currentScreen = AppNavScreen.CHAT
                        }
                    )
                }

                AppNavScreen.STUDIO -> {
                    selectedProject?.let { project ->
                        FlowStudioScreen(
                            project = project,
                            viewModel = viewModel,
                            onNavigateBack = {
                                currentScreen = AppNavScreen.HOME
                            }
                        )
                    } ?: run {
                        currentScreen = AppNavScreen.HOME
                    }
                }

                AppNavScreen.CHAT -> {
                    AiChatScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            currentScreen = AppNavScreen.HOME
                        }
                    )
                }

                AppNavScreen.TEMPLATES -> {
                    TemplatesScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            currentScreen = AppNavScreen.HOME
                        },
                        onProjectCreated = {
                            currentScreen = AppNavScreen.HOME
                        }
                    )
                }
            }
        }
    }
}

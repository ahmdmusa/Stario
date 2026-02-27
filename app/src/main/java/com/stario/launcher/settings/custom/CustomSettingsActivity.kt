package com.stario.launcher.settings.custom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.content.Intent

class CustomSettingsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text("Customization Settings") })
                        }
                    ) { padding ->
                        SettingsContent(Modifier.padding(padding))
                    }
                }
            }
        }
    }

    @Composable
    fun SettingsContent(modifier: Modifier = Modifier) {
        val coroutineScope = rememberCoroutineScope()
        
        // Settings State
        val gridRows by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.GRID_ROWS, 5).collectAsState(5)
        val gridCols by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.GRID_COLS, 5).collectAsState(5)
        val iconSize by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.ICON_SIZE, 1.0f).collectAsState(1.0f)
        val cornerRadius by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.CORNER_RADIUS, 24f).collectAsState(24f)
        val blurStrength by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.BLUR_STRENGTH, 15f).collectAsState(15f)
        val animSpeed by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.ANIM_SPEED, 1).collectAsState(1)
        
        val gestureUp by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.GESTURE_UP, 1).collectAsState(1)
        val gestureDown by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.GESTURE_DOWN, 2).collectAsState(2)
        val gestureDoubleTap by CustomSettingsDataStore.getFlow(this, CustomSettingsDataStore.GESTURE_DOUBLE_TAP, 3).collectAsState(3)

        LazyColumn(
            modifier = modifier.fillMaxSize().padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Text("Home Screen Grid", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Grid Rows ($gridRows)")
                Slider(
                    value = gridRows.toFloat(),
                    onValueChange = { 
                        coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.GRID_ROWS, it.toInt()) }
                    },
                    valueRange = 3f..6f,
                    steps = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(text = "Grid Columns ($gridCols)")
                Slider(
                    value = gridCols.toFloat(),
                    onValueChange = { 
                        coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.GRID_COLS, it.toInt()) }
                    },
                    valueRange = 3f..6f,
                    steps = 2
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Visuals & Shapes", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Icon Size (%.1fx)".format(iconSize))
                Slider(
                    value = iconSize,
                    onValueChange = { 
                        coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.ICON_SIZE, it) }
                    },
                    valueRange = 0.8f..1.3f
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Corner Radius (${cornerRadius.toInt()}px)")
                Slider(
                    value = cornerRadius,
                    onValueChange = { 
                        coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.CORNER_RADIUS, it) }
                    },
                    valueRange = 0f..100f
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Blur Strength (${blurStrength.toInt()}px)")
                Slider(
                    value = blurStrength,
                    onValueChange = { 
                        coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.BLUR_STRENGTH, it) }
                    },
                    valueRange = 0f..50f
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Interactions", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Animation Speed")
                val speedOptions = listOf("Slow", "Normal", "Fast")
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    speedOptions.forEachIndexed { index, title ->
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = animSpeed == index,
                                onClick = {
                                    coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.ANIM_SPEED, index) }
                                }
                            )
                            Text(text = title)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Gestures", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                val gestureOptions = listOf("None", "App Drawer", "Notifications", "Lock Screen")

                Text(text = "Swipe Up")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    gestureOptions.forEachIndexed { index, title ->
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = gestureUp == index,
                                onClick = {
                                    coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.GESTURE_UP, index) }
                                }
                            )
                            Text(text = title)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Swipe Down")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    gestureOptions.forEachIndexed { index, title ->
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = gestureDown == index,
                                onClick = {
                                    coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.GESTURE_DOWN, index) }
                                }
                            )
                            Text(text = title)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Double Tap")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    gestureOptions.forEachIndexed { index, title ->
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = gestureDoubleTap == index,
                                onClick = {
                                    coroutineScope.launch { CustomSettingsDataStore.setValue(this@CustomSettingsActivity, CustomSettingsDataStore.GESTURE_DOUBLE_TAP, index) }
                                }
                            )
                            Text(text = title)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Advanced Features", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(this@CustomSettingsActivity, CategoriesSettingsActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage App Categories")
                }
            }
        }
    }
}

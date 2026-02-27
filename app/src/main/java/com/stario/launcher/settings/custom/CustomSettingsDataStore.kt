package com.stario.launcher.settings.custom

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "custom_stario_settings")

object CustomSettingsDataStore {
    @JvmField val GRID_ROWS = intPreferencesKey("grid_rows")
    @JvmField val GRID_COLS = intPreferencesKey("grid_cols")
    @JvmField val GRID_SPACING = floatPreferencesKey("grid_spacing")
    @JvmField val ICON_SIZE = floatPreferencesKey("icon_size")
    @JvmField val CORNER_RADIUS = floatPreferencesKey("corner_radius")
    @JvmField val BLUR_STRENGTH = floatPreferencesKey("blur_strength")
    @JvmField val ANIM_SPEED = intPreferencesKey("anim_speed") // 0=Slow, 1=Normal, 2=Fast

    @JvmField val PRIMARY_COLOR = stringPreferencesKey("primary_color")
    @JvmField val BG_OPACITY = floatPreferencesKey("bg_opacity")

    @JvmField val DOCK_VISIBLE = booleanPreferencesKey("dock_visible")
    @JvmField val DOCK_ICON_SIZE = floatPreferencesKey("dock_icon_size")
    @JvmField val DOCK_BG_OPACITY = floatPreferencesKey("dock_bg_opacity")

    @JvmField val DRAWER_SEARCH = booleanPreferencesKey("drawer_search")
    @JvmField val DRAWER_BG_OPACITY = floatPreferencesKey("drawer_bg_opacity")
    @JvmField val DRAWER_SCROLLBAR = booleanPreferencesKey("drawer_scrollbar")

    @JvmField val GESTURE_UP = intPreferencesKey("gesture_up")
    @JvmField val GESTURE_DOWN = intPreferencesKey("gesture_down")
    @JvmField val GESTURE_DOUBLE_TAP = intPreferencesKey("gesture_double_tap")

    @JvmField val SHOW_WIDGETS = booleanPreferencesKey("show_widgets")
    @JvmField val SHOW_LABELS = booleanPreferencesKey("show_labels")
    @JvmField val ICON_LABEL_SIZE = floatPreferencesKey("icon_label_size")
    @JvmField val LOCK_LAYOUT = booleanPreferencesKey("lock_layout")

    @JvmStatic
    fun <T> getValueSync(context: Context, key: Preferences.Key<T>, default: T): T {
        return runBlocking {
            context.dataStore.data.map { it[key] ?: default }.first()
        }
    }

    @JvmStatic
    suspend fun <T> setValue(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    @JvmStatic
    fun <T> getFlow(context: Context, key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.map { it[key] ?: default }
    }
}

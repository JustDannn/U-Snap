package com.example.u_snap.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue

private data class NavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun USnapFloatingNavBar(
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember {
        listOf(
            NavItem("Home", Icons.Filled.Home),
            NavItem("Maps", Icons.Filled.LocationOn),
            NavItem("Camera", Icons.Filled.CameraAlt),
            NavItem("Chat", Icons.Outlined.ChatBubbleOutline)
        )
    }

    Surface(
        modifier = modifier
            .height(76.dp)
            .padding(horizontal = 16.dp),
        shape = CircleShape,
        color = Color(0xFF2A2A2A),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) Color(0xFFFFEB3B) else Color.Transparent,
                    animationSpec = tween(durationMillis = 220),
                    label = "navBackgroundColor"
                )

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) Color.Black else Color(0xFFBDBDBD),
                    animationSpec = tween(durationMillis = 220),
                    label = "navContentColor"
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable { onSelectedIndexChange(index) }
                        .animateContentSize(animationSpec = tween(durationMillis = 220))
                        .padding(
                            horizontal = if (isSelected) 18.dp else 12.dp,
                            vertical = 12.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = contentColor,
                            modifier = Modifier.size(26.dp)
                        )

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = fadeIn(tween(160)) + expandHorizontally(tween(220)),
                            exit = fadeOut(tween(120)) + shrinkHorizontally(tween(180))
                        ) {
                            Text(
                                text = item.title,
                                color = contentColor,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
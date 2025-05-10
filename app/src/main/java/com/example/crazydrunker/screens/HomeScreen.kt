package com.example.crazydrunker.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crazydrunker.DetailsScreenData
import com.example.crazydrunker.ScreenSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(64.dp)  // Larger touch target
                        .padding(12.dp)  // Visual padding
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)  // Standard icon size
                    )
                }
            }
        },
        actions = {actions()},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.surfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
    )
}
@Composable
fun JuiceBlock(
    juices: List<DetailsScreenData>,
    modifier: Modifier = Modifier,
    navController: NavController,
    selectedJuice: DetailsScreenData? = null,
    onJuiceSelected: (DetailsScreenData) -> Unit = {}
) {
    val windowSize = ScreenSize.getInstance().getDeviceSize()
    val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

    if (isTablet) {
        // Tablet layout - Row with list on left and details on right
        Row(modifier = modifier.fillMaxSize()) {
            // List on the left (40% width)
            LazyColumn(
                modifier = Modifier
                    .weight(0.4f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(juices) { juice ->
                    JuiceCard(
                        juice = juice,
                        isSelected = selectedJuice?.id == juice.id,
                        onClick = { onJuiceSelected(juice) }
                    )
                }
            }

            // Details on the right (60% width)
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (selectedJuice != null) {
                    ProductScreen(selectedJuice, navController = navController)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Select a cocktail to view details",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    } else {
        // Phone layout - Simple list
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(juices) { juice ->
                JuiceCard(
                    juice = juice,
                    onClick = { navController.navigate(juice) }
                )
            }
        }
    }
}

@Composable
private fun JuiceCard(
    juice: DetailsScreenData,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    // Animation for selection indicator
    val animatedHeight by animateDpAsState(
        targetValue = if (isSelected) 15.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "selectionAnimation"
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .clickable(
                onClick = onClick
            )
    ) {
        // Main card content
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isSelected) 8.dp else 4.dp
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image container with Fit scaling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Image(
                        painter = painterResource(id = juice.img),
                        contentDescription = juice.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Star rating overlay
                    StarRatingOverlay(difficulty = juice.difficulty, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp))
                }

                // Text content
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = juice.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Refreshing citrus blend with herbal notes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                // Preparation time
                PreparationTime(timeToMake = juice.timeToMake)
            }
        }

        // Animated selection indicator
        SelectionIndicator(animatedHeight = animatedHeight)
    }
}

@Composable
internal fun StarRatingOverlay(difficulty: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(4.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < difficulty) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}


@Composable
internal fun PreparationTime(timeToMake: Int) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Time",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
        Text(
            text = "$timeToMake min",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SelectionIndicator(animatedHeight: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(animatedHeight)
                .background(
                    brush = Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                    )
                )
                .align(Alignment.BottomCenter) // Center the 80% width bar
        )
    }
}
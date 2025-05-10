package com.example.crazydrunker.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crazydrunker.CocktailMakeInfo
import com.example.crazydrunker.CocktailsData
import com.example.crazydrunker.DetailsScreenData
import com.example.crazydrunker.Ingredient
import com.example.crazydrunker.TimerScreenData

@Composable
fun ProductScreen(
    cocktail: DetailsScreenData,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painterResource(id = cocktail.img),
                    contentDescription = cocktail.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                StarRatingOverlay(
                    difficulty = cocktail.difficulty, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = cocktail.name,
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                PreparationTime(timeToMake = cocktail.timeToMake)

                Text(
                    text = "Classic cocktail with modern twist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }

        item {
            SectionHeader(title = "Ingredients")
            Spacer(Modifier.height(8.dp))
            IngredientsHorizontalList(ingredients = CocktailsData.getIngredients(cocktail.id))
        }

        item {
            SectionHeader(title = "Preparation")
            Spacer(Modifier.height(8.dp))
            StepsList(
                cocktail = cocktail,
                navController = navController
            )
        }
    }
}

@Composable
internal fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun IngredientsHorizontalList(ingredients: List<Ingredient>) {
    val horizontalScroll = rememberScrollState(0)
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .horizontalScroll(horizontalScroll)
    ) {
        ingredients.forEach { ingredient ->
            IngredientItem(ingredient = ingredient)
        }
    }
}

@Composable
private fun IngredientItem(ingredient: Ingredient) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.size(width = 80.dp, height = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = ingredient.name,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
internal fun StepsList(
    cocktail: DetailsScreenData,
    navController: NavController,
    showTimers: Boolean = true,
    onStepTimerClick: (Any?, Any?) -> Unit = { _, _ -> },
    activeStepIndex: MutableState<Int> = rememberSaveable { mutableIntStateOf(0) }
) {

    val steps = CocktailsData.getCocktailSteps(cocktail.id)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(8.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        steps.forEachIndexed { index, step ->
            StepItem(
                step = step,
                index = index,
                navController = navController,
                cocktailId = cocktail.id,
                showTimers = showTimers,
                onStepClick = { activeStepIndex.value = index },
                onStepTimerClick = onStepTimerClick,
                isCurrentStep = activeStepIndex.value == index,
            )
        }
    }
}

@Composable
private fun StepItem(
    step: CocktailMakeInfo,
    index: Int,
    navController: NavController,
    cocktailId: String,
    showTimers: Boolean = true,
    onStepTimerClick: (Int?, Int?) -> Unit,
    onStepClick: () -> Unit,
    isCurrentStep: Boolean,
    ) {

    val highlightWidth by animateDpAsState(
        targetValue = if (isCurrentStep) 12.dp else 0.dp,
        animationSpec = spring(),
        label = "highlightAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable { onStepClick() }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Step highlight
        if (isCurrentStep) {
            Box(
                modifier = Modifier
                    .width(highlightWidth)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        // Step number badge
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Step text and timer in the same row
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            // Step text with fixed height
            Text(
                text = step.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.dp)
            )

            Box(modifier = Modifier.width(80.dp).height(48.dp), contentAlignment = Alignment.Center) {
                if (step.includeTimer) {
                    val formattedTime = formatTime(step.min, step.sec)
                    TextButton(
                        onClick = {
                            onStepClick();
                            if (showTimers) {
                                navController.navigate(
                                    TimerScreenData(
                                        isStarted = false,
                                        cocktailId = cocktailId,
                                        min = step.min,
                                        sec = step.sec,
                                        stepIndex = index
                                    )
                                )
                            } else {
                                onStepTimerClick(step.min, step.sec)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Start Timer",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }

    }
}
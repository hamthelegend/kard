package com.thebrownfoxx.kard.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.kard.R
import com.thebrownfoxx.kard.logic.Player
import com.thebrownfoxx.kard.logic.extension.CoinFace
import com.thebrownfoxx.kard.logic.turn.Card
import com.thebrownfoxx.kard.ui.component.RestartButton
import com.thebrownfoxx.kard.ui.extension.Elevation
import com.thebrownfoxx.kard.ui.game.cardselector.CardSelector
import com.thebrownfoxx.kard.ui.game.coinflipresult.CoinFlipResult
import com.thebrownfoxx.kard.ui.game.coinguesser.CoinGuesser
import com.thebrownfoxx.kard.ui.game.dierollresult.DieRollResult
import com.thebrownfoxx.kard.ui.game.gameover.GameOver
import com.thebrownfoxx.kard.ui.game.playerinfo.AiInfo
import com.thebrownfoxx.kard.ui.game.playerinfo.PlayerInfo
import com.thebrownfoxx.kard.ui.game.supposedturnresult.SupposedTurnResult
import com.thebrownfoxx.kard.ui.game.turnresult.TurnResult
import com.thebrownfoxx.kard.ui.theme.KardTheme

@Composable
fun Game(
    player: Player,
    ai: Player,
    uiState: UiState,
    onCardSelect: (Card?) -> Unit,
    onCardCommit: () -> Unit,
    onCoinGuess: (CoinFace) -> Unit,
    onTurnAcknowledge: () -> Unit,
    onSupposedTurnResult1Acknowledge: () -> Unit,
    onSupposedTurnResult2Acknowledge: () -> Unit,
    onTurnResultAcknowledge: () -> Unit,
    showGameOver: Boolean,
    playerWon: Boolean,
    onGameOverAcknowledge: () -> Unit,
    onGameRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(16.dp)
                .systemBarsPadding(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                )
                Box(modifier = Modifier.weight(1f)) {
                    RestartButton(
                        onLongClick = onGameRestart,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
            AiInfo(ai = ai)
            PlayerInfo(player = player)
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = Elevation.level(1),
            ) {
                AnimatedContent(
                    targetState = uiState,
                    modifier = Modifier.padding(16.dp),
                    contentKey = { it::class },
                ) { uiState ->
                    when (uiState) {
                        is UiState.SelectingTurn -> CardSelector(
                            cards = player.cards,
                            selectedCard = uiState.selectedCard,
                            onCardSelect = onCardSelect,
                            onCardCommit = onCardCommit,
                        )

                        is UiState.Attacked -> DieRollResult(
                            value = uiState.rolledDie,
                            onAcknowledge = onTurnAcknowledge,
                        )

                        is UiState.BlockingGuessing -> CoinGuesser(onCoinGuessed = onCoinGuess)
                        is UiState.BlockingFlipped -> CoinFlipResult(
                            guessedCoin = uiState.guessedCoin,
                            flippedCoin = uiState.flippedCoin,
                            onAcknowledge = onTurnAcknowledge,
                        )

                        is UiState.ShowingSupposedTurnResult1 -> SupposedTurnResult(
                            supposedTurnResult = uiState.supposedTurnResult,
                            onAcknowledge = onSupposedTurnResult1Acknowledge,
                        )

                        is UiState.ShowingSupposedTurnResult2 -> SupposedTurnResult(
                            supposedTurnResult = uiState.supposedTurnResult,
                            onAcknowledge = onSupposedTurnResult2Acknowledge,
                        )

                        is UiState.ShowingTurnResult -> TurnResult(
                            turnResult = uiState.turnResult,
                            onAcknowledge = onTurnResultAcknowledge,
                        )
                    }
                }
            }
        }
    }

    GameOver(show = showGameOver, playerWon = playerWon, onAcknowledge = onGameOverAcknowledge)
}

@Preview
@Composable
fun GamePreview() {
    KardTheme {
        Game(
            player = Player(id = 1, name = "Player").damagedBy(50),
            ai = Player(id = 2, name = "AI").damagedBy(70).usedCard(null),
            uiState = UiState.SelectingTurn(selectedCard = null),
            onCardSelect = {},
            onCardCommit = {},
            onTurnAcknowledge = {},
            onCoinGuess = {},
            onSupposedTurnResult1Acknowledge = {},
            onSupposedTurnResult2Acknowledge = {},
            onTurnResultAcknowledge = {},
            showGameOver = false,
            playerWon = false,
            onGameOverAcknowledge = {},
            onGameRestart = {},
        )
    }
}
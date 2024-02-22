# Current Known Bugs
## Adaptive Issues
1. The current CSS style is not compatible with varying display sizes, due to the use of pixels for some placements. These should be in a cross-compatible measurement that does not rely on specific pixel densities. It could also apply different styles based on the size of the user's display.
2. 

## Perfective Issues
1. Error messages are not user friendly, should not specify API endpoint for code related issues. Should state an easily understandable error message such as "Placement Conflict" or "It is not your turn"
2. The access code is not clear or labelled. It appears unlabelled after host game is selected.
3. Access code text box does not ignore leading spaces, and copying the access code directly often inputs a space before the code.
4. When the game is complete, the users have to press reset to start from the beginning. This closes the connection and requires the players to create a new connection. Ideally, the reset button could reset the board and keep the connection live.
5. When a player selects "Host Game" it starts by saying it is their move, before another player joins. It should display a header indicating that the game is waiting for another player before indicating it is their move.
6. The game requires the host to always go first, in Tic-Tac-Toe this is a massive advantage, as such the first move should be decided randomly when a player joins by giving the X and O to players randomly.
7. The X and O symbols are slightly too high within the box, they could be centered better.
8. The Console Logging is often unclear or incorrect, when a user makes an invalid move, it is logged as "Move" rather than an error message. The detailed information on valid moves, and players joining works well. 
9. When reset is pressed, the access code is not cleared from the text box. This is not an issue if perfective issue 4 is resolved.
10. The user is unable to press the "Enter" key to submit an access code.
11. The scaling for UI elements at different window sizes is not consistent. The boxes will grow/shrink, and the text always remains the same size.
12. The UI layout does not update properly to changing window sizes, the access code box can be reduced to being invisible. The content does not re-align vertically in most cases.

## Corrective Issues
1. "Your Move" "Opponent Move" Header does not always reflect who's move it is, or the proper state of the game.
2. Header after game completion specifies "No Winner for this Game", even if a player has won.
3. Occasionally a player can move multiple times in a row
4. ~~Occasionally, a player can not place an X or an O in certain positions, especially the center due to "Placement Conflict"~~
5. ~~Occasionally the game will end early, before a line of three is made, or all spaces are filled.~~
6. ~~In the Console logging, it often specifies that a player played "EMPTY" at x, y. Which should not be a valid option, should be either X or O~~
7. ~~The code to check if a game is won only checks one diagonal option twice, (0,0) (1,1) (2,2) and does not check the other, (0,2) (1,1) (2,0). As such, a diagonal victory will never be found for the one direction~~
8. On placement conflicts, the player is still swapped. Rather than letting the player who made the error, choose a new position.
9. When a Player leaves the game room, the other player is never notified, and can continue playing without the other player.
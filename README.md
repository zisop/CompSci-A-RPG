Ideas:

mob AI

- how?
- transform room into a list of lines
- make sure paths never intersect the lines
- <b>following player</b>
- when should it follow player?
- each mob should have a visibility distance
- melee mobs will path to the player and turn on followingPlayer
- CreateMovementPoint will depend on followingPlayer
- if a mob finds a collision, it should stop moving in its path
- if player gets outside of mob range, mob should stop followingPlayer
- <b>attacking</b>
- if a mob gets in attack range, it should attack
- the mob should go into n(should be small) frames of attack animation
- after entering attack animation, a melee mob cannot miss
- attacking should reduce player health
- attacking should render the player invulnerable temporarily
- <b>player attacks</b>
- if a player attacks a mob, the mob goes into hit stun
- hit stun involves taking knockback that should decelerate
- give mobs a hit stun velocity on initial hit, and a hit stun acceleration
- a mob cannot move or attack until hitstun is over
- <b>bosses</b>
- oh my fucking god i dont want to think about this please kill me

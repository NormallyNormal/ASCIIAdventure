A platformer game that renders to a terminal window.

Building requires Java 16 or higher language features.
## Current features
### Player character
Can run and jump.
Also has dash and double jump abilities.
### Platforms
Solid platforms that the player can land on. They can also move and bring the player along.
Semi-solid platforms allow the player to jump up through them or drop down.
### Seperate Moving Object Renderers
Each world object can have an independent renderer object associated with it. They can easily be given different special effects without changing behavior.
### Seperate Moving Object Controllers
Each moving world object can have an independent movement controller associated with it. This allows for endless potential movement patterns, coordination between moving objects, and more.
### Hazards
Spikes that kill the player, sending them to a checkpoint.
### Special Objects
Gravity fields that can flip the players gravity.
Bounce pads.
### Particle system
Particles can make unique effects for the player and environment features.
### Post Processing Shaders
Allow for fullscreen special effects.
The torchlight shader creates lighting effects around glowing objects when enabled.
### NPCs
Non player characters can tell the player information.
### Orbs
Refill double jump or dash abilities instantly on contact.
## Demo
https://github.com/user-attachments/assets/41afadf0-787b-47f9-a29c-7792dc3ff86c

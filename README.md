# This is for version 1.21.10 @ Redstone Additions 1.1.0

## Purpose of the Mod
Adds OR, XOR, NOT, and AND boolean logic gates and a CLOCK  with the idea of designing more compact redstone machines with potentially better performance.

 
# Recipes and Usage
## AND Gate:


|<img src ="https://www.vhv.rs/dpng/d/51-514803_transparent-minecraft-redstone-torch-hd-png-download.png" alt="Redstone Torch Lit" width="40"/> | <img src ="https://www.vhv.rs/dpng/d/51-514803_transparent-minecraft-redstone-torch-hd-png-download.png" alt="Redstone Torch Lit" width="40"/> | <img src ="https://www.vhv.rs/dpng/d/51-514803_transparent-minecraft-redstone-torch-hd-png-download.png" alt="Redstone Torch Lit" width="40"/> |
|---|---|---|
| <img src="https://minecraft.wiki/images/thumb/Redstone_Dust_JE2_BE2.png/150px-Redstone_Dust_JE2_BE2.png?8cf17" alt="Redstone Dust" width="60"/> | <img src="https://minecraft.wiki/images/thumb/Redstone_Repeater_%28item%29_JE1.png/32px-Redstone_Repeater_%28item%29_JE1.png?1b9a2" alt="Redstone Repeater" width="60"/> | <img src="https://minecraft.wiki/images/Copper_Ingot_JE2_BE1.png?0d410" alt="Copper Ingot" width="60"/> | 
| <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> |

### Usage
2 Inputs 1 Output
1 Input on Left
1 Input on Right
1 Output on redstone torch side


|L| R | Output|
|--|--|-- |
|0|0|0
| 0 | 1 |0|
|1|0|0|
|1|1|1|

## NOT Gate:
|X | X | X |
|---|---|---|
| <img src="https://minecraft.wiki/images/thumb/Redstone_Dust_JE2_BE2.png/150px-Redstone_Dust_JE2_BE2.png?8cf17" alt="Redstone Dust" width="60"/> | <img src="https://minecraft.wiki/images/Copper_Ingot_JE2_BE1.png?0d410" alt="Copper Ingot" width="60"/> | <img src ="https://www.vhv.rs/dpng/d/51-514803_transparent-minecraft-redstone-torch-hd-png-download.png" alt="Redstone Torch Lit" width="40"/> | 
| <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> |

### Usage
1 Input and 1 Output
Output is the opposite of the input
So if Redstone is powered in, it will come out off.
|Input | Output|
|--|-- |
| 0 | 1|
|1|0|

## (X)OR Gate:
XOR and OR is the same block
| X | X  | X |
|---|---|---|
| <img src="https://minecraft.wiki/images/thumb/Redstone_Dust_JE2_BE2.png/150px-Redstone_Dust_JE2_BE2.png?8cf17" alt="Redstone Dust" width="60"/> | <img src="https://minecraft.wiki/images/thumb/Redstone_Repeater_%28item%29_JE1.png/32px-Redstone_Repeater_%28item%29_JE1.png?1b9a2" alt="Redstone Repeater" width="60"/> |<img src="https://minecraft.wiki/images/Copper_Ingot_JE2_BE1.png?0d410" alt="Copper Ingot" width="60"/> | 
| <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> |

### Usage
2 Inputs 1 Output
1 Input on Left
1 Input on Right
1 Output on redstone torch side

OR:
|L| R | Output|
|--|--|-- |
|  |  ||
XOR:
|L| R | Output|
|--|--|-- |
|  |  ||

## CLOCK:
| X | X  | X |
|---|---|---|
| <img src="https://minecraft.wiki/images/thumb/Redstone_Dust_JE2_BE2.png/150px-Redstone_Dust_JE2_BE2.png?8cf17" alt="Redstone Dust" width="60"/> | <img src="https://minecraft.wiki/images/thumb/Redstone_Repeater_%28item%29_JE1.png/32px-Redstone_Repeater_%28item%29_JE1.png?1b9a2" alt="Redstone Repeater" width="60"/> | <img src ="https://www.vhv.rs/dpng/d/51-514803_transparent-minecraft-redstone-torch-hd-png-download.png" alt="Redstone Torch Lit" width="40"/> | 
| <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png?849cb" alt="Iron Ingot" width="60"/> |

### Usage
1 Output:
Flips between on and off at a rate specified by the delay which is changed when right clicked

|Delay #|Flips per Second |
|--|--|
| 1 | 20 |
|5|4|
|10|2|
|15|1.333|
|20|1|




# Potential Performance Increase with Clocks


## NOTE!
I never made this mod to make a better performant way for using Redstone, I more built this to learn modding and to save space in Redstone designs. Do not download this mod for the potential performance increase

If you have any information to add regarding of if the test is accurate or flawed, I would love to hear about it.

## Base Rules for each test:
Both worlds were duplicates using Minecrafts built in recreate button
Sim distance: 12 chunks
Render Distance: 12 chunks
Super flat world with "Redstone Ready" preset. Peaceful, no structures.
Windowed mode
Forge install with just Redstone Additions and WorldEdit

### Redstone Additions Clock
For the Redstone Additions test, 10,201 (100x100 grid) clocks were set with WorldEdit with a delay set to 1 (flips 20 times a tick)

<img src="https://raw.githubusercontent.com/LizardWizard5/RedstoneAdditions/refs/heads/1.20.1/performanceComparisons/RedstonePerfClock.png" alt="Performance picture for Redstone Additions with Integrated Server metrics showing 23ms ticks, 3tx, 1111rx and 888MB of Memory usage "/>

### Vanilla Clock
For the vanilla clock, this is a design I have came across from just playing the game, I am unsure the exact speed it flips but just watching the flip rate, it feels similar to the speed of Redstone Additions Clock with a delay =1 world edit was also used to copy and stack in a 100x100 grid
effectively placing 10,000 individual instances of this design.


<img src="https://raw.githubusercontent.com/LizardWizard5/RedstoneAdditions/refs/heads/1.20.1/performanceComparisons/vanillaPerfClock.png" alt="Performance picture for Vanilla  with Integrated Server metrics showing 1962ms ticks, 4tx, 119rx and 1432MB of Memory usage "/>

### The conclusions
Through my own research on what each value means in the f3 menu through the following <a src="https://minecraft.fandom.com/wiki/Debug_screen">Minecraft Wiki Link</a>, it would seem that using the mod lowers the total amount of memory while also lowering speed it takes to process a tick. The mod also has an increased amount packets received from the Integrated Server by roughly 10x. 
You can compare these values and anything else I might have missed by looking at both images above. 


## NOTE!
I never made this mod to make a better performant way for using Redstone, I more built this to learn modding and to save space in Redstone designs. Do not download this mod for the potential performance increase

If you have any information to add regarding of if the test is accurate or flawed, I would love to hear about it.

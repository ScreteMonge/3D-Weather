# 3D Weather Plugin
A plugin that gives immersive, 3D weather with dynamic weather cycles and ambience.

Note that this plugin looks best when the colour of the skybox is changed through the default Skybox plugin.
It looks even better with 117HD enabled!

## Gallery

### Rain

![Rain](https://imgur.com/Q7RSYp0.gif)

### Snow

![Snow](https://imgur.com/Ps5jQS2.gif)

### Fog

![Fog](https://imgur.com/N5fG6v7.gif)

### Ashfall

![Ashfall](https://imgur.com/r6tLE9R.gif)

### Stars

![Stars](https://imgur.com/na8m2vY.gif)

## Features

Weather can be manually set or dynamically self-regulated. 

![Config](https://imgur.com/fpStaA2.png)

For Dynamic Weather, the Weather will automatically loop based on your Season and Biome. 
Season can be set or dynamically self-regulated as well, changing every 7 days based on Jagex time.
Naturally, Winter season will feature more frequent colder, precipitous Weathers, while Summer will tend to be drier.

Biome is determined by your chunk on the world map. 
A chunk that is predominantly within the Desert region will therefore feature as a Desert Biome. 
Note that this does result in some awkward gaps where a Biome may be applied to the edge of an area that is clearly a different Biome.

![Conditions](https://imgur.com/T7nXnOL.png)

Weather Density gives control over how many Weather objects spawn - how dense the rain is, for example.
This is particularly handy for setting a particular scene or controlling how much the plugin will impact your performance.

![Overlay](https://imgur.com/qP9EIVo.png)

An overlay that indicates the current Weather, Biome, and Season can also be toggled on or off. 
This is purely for informational purposes. 
As of right now, you'll notice some Weathers aren't any different from each other (Cloudy vs Sunny vs Partly Cloudy, for example).
There are hopes that they and the different Seasons will be given some personality of their own in the future.

This plugin also features ambient Weather sounds which can be toggled on or off in the config, as well as have their volume adjusted.
Note that the ambient Weather volume is dependent on both the Ambient Volume setting as well as the Weather Density - higher density of Weather objects will be louder.

## Credits

Special thanks to the RLweather plugin by Bogstandard for providing inspiration for this plugin.

Also special thanks to these authors for making the ambient sounds used in this plugin freely available on FreeSound.org:

| Track                        | Author            | URL                                                           |
|------------------------------|-------------------|---------------------------------------------------------------|
| 241102H (Mystic voice)       | Freed             | https://freesound.org/people/Freed/sounds/1105/               |
| Rumble                       | HerbertBoland     | https://freesound.org/people/HerbertBoland/sounds/147661/     |
| Lightning Strike and Thunder | Aeonemi           | https://freesound.org/people/Aeonemi/sounds/180327/           |
| raw_wind                     | rivv3t            | https://freesound.org/people/rivv3t/sounds/201208/            |
| Rain Forest Steady           | mikaelacampbell18 | https://freesound.org/people/mikaelacampbell18/sounds/617078/ |
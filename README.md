A program to parse, process, and return data regarding the penetration values of various tank guns from the game World of Tanks, to serve as a comparison between available silver and gold ammunition.

Big thanks to Jak_Atackka of [tanks.gg](https://tanks.gg/) for supplying me with the input data.

## Motivation

The exact motivation behind this is that golden ammunition is a contentious topic in the WoT community, and some arguments pro gold ammo are that it does have its own drawbacks.
Most commonly those stated drawbacks are that it either has reduced/no normalization compared to silver ammo, or that it loses more penetration over distance.

Since these drawbacks are relatively easy to put into numbers, that's exactly what I decided to do [here](https://old.reddit.com/r/WorldofTanks/comments/p8bbj8/on_normalization_and_penetration_loss_over/) a few months ago.
That post contains the data for all tanks tiers 8 and 10 in their "top" configuration as defined by the website [tanks.gg](https://tanks.gg/), but since the scope of that post is limited, I was thinking to expand it to all tanks, or even all guns on all tanks.
Attempts to handle that data manually revealed that it'd be a *massive pain*, hence this program.

## Functionality

As it stands, the program is made to read a csv file in a specific format, transform the contents into an object representation, then filter and sort them to remove guns and tanks that aren't suitable for this analysis (ex. a gun/tank that has both silver and gold AP shells), and write the results to a group of scv files in a format that's ready to be plugged into a Google Sheets similar to the one in the original post (see [Motivation](#-motivation)).

Both data input and output methods were programmed to be modular, though I don't expect to utilize this feature.

By default, the application expects the input file `1.15.0-shell-pen-dropoff.csv` to be in the `resources` folder, and will place the output files directly in `D:`, but some aspects of this are configurable through the `application.properties` file in the `resources` folder.

## Running the program

The program can be run through Gradle with a simple:

```./gradlew run```

The program was written in Kotlin targeting Java 11.

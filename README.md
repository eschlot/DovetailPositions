
# DovetailPositions
A java program to calculate the positions to use on the Box Joint Jig to cut Dovetails. 

This program can be used to calculate the cutting positions for dovetails on the Box Joint Jig I described here: 
https://github.com/eschlot/Box-Joint-Jig-Control


## Installation: 
At the moment You need to download a recent Eclipse version togeterh with Java JDK 9. Then import this project and comile it. 
Once I find the time I will add a compiled version for easier usage. 

## Usage of the tool:
The tool needs some inputs to calculate the cutting positions of the dovetails. 
- The angle of the dovetails
- the thickness (height) of the wood
- the width of the wood
- the width of the sawblade

And for each dovetail
- The startposition 
- the width at the narrow side of the dovetail

The program will then calculate all necessary cutting positions and draw a visual representation of what the cuts will do. This part is partially implemented. The output of these positions is on the right side of the tool and the numbers can be entered into the individual distribution of cuts of the Box Joint Jig. 
To utilize the program You also need to build an extension jig to the Box Joint Jig to hold the pin side of the wood at the destination angle in the jig. 
There are five different cut types to be executed to produce a complete Dovetail joint:

- For the Dovetail board
  - A straight cut to remove the material on the dovetail piece
  - After dialing in the zero position for a tilted blade: A cut on one side of the dovetail.
  - After turning the wood and dialing in the zero position another time: A cut on the other side of the dovetail
- For the Pinboard using the Angle jig
  - A cut with the sawblade at 90° but the workpiece at an angle to cut one side of the pins and remove most material
  - a cut at the oposite angle of the worpiece to cut the other side of the pins. 
  
Actually the process is lengthy and it is required to work very exact when dialing in the zero-position again and again. But it is possible to cut perfect dovetails at high quality. 

## Using the tool:

The tool is written with JavaFX and the textfields and the tables to add the dovetail positions require always to press "Enter" after a number is changed. This is a bit unpleasant for the moment and I do not expect too many people to use the tool in the end. But maybe somebody likes to contribute to improve this behavior. 

  

# Pythagoras Encryption Algorithm v2.1

The Pythagoras Encryption Algorithm (abbreviated to PEA) is a custom symmetric cipher algorithm developed by Eero Kaan as part of the 2016 Paper "Verteiltes Rechnen: Entwicklung einer Renderfarm mit Sicherheitspriorität" (German for "Distributed Computing: Development of a Renderfarm with priority on security").

This paper was part of a voluntary graduation project which took place over the course of one year.

## Functionality
PEA is a symmetric block cipher based on a substitution–permutation network implementation. The 128 Bit long blocks are being read and encrypted under the use of a 128 Bit long cipher key. The main components are described as the "BorderTransposition", "KeyProzessor", "S-Boxen" and "Shifting" modules respectively.

For further information please refer to the paper [Verteiltes Rechnen: Entwicklung einer Renderfarm mit Sicherheitspriorität](https://eerokaan.de/).

### BorderTransposition module

The BorderTransposition module utilizes the logistic map and parts of the provided key cipher in order to generate 16 different subsituation pairs. The used parts on the key side are determined by the context, from which the BorderTransposition module is called. The BorderTransposition module is being applied to the data-block (as the name may suggest) as the very first and last step in the block encryption pipeline.

### KeyProzessor module

The KeyProzessor module takes the provided key cipher as an input in order to generate 16 different S-Boxes from which the different subsitutions are being performed.

### S-Boxen module

The S-Boxen module performs the actual round-based substitutions on the data-blocks.

### Shifting module

The Shifting module provides a diagonal shifting to the processed block.


## License
[Permitted use under the MIT licence](https://choosealicense.com/licenses/mit/)

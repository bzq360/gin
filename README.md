# Refining Fitness Functions for Search-Based Program Repair

This is an artefact to accompany the "Refining Fitness Functions for Search-Based Program Repair" paper by Zhqiang Bian, Aymeric Blot and Justyna Petke. 
The experiemnt scripts can be found at https://github.com/SOLAR-group/apr2021experiment-scripts.

This repository is forked from the original Gin repository https://github.com/gintool/gin/commit/5cf4861cce58b5cd7f39c49bff20441442db9f70. 

The experiment scripts can be found at https://github.com/bzq360/fitness-experiment.

## Implementation

### Assertion distance calculation
- gin/fitness/AssertionValueType.java
- gin/fitness/AssertionValueTypeChecker.java
- gin/fitness/DistanceMetric.java

### GenProg fitness
- gin/util/GPFix.java

### Arja-e fitness
- gin/util/GPArjaEFix.java

### 2-phase fitness
- gin/util/GPNovelFix.java

## License

This project is licensed under the MIT License. Please see the [LICENSE.md](LICENSE.md) file for details

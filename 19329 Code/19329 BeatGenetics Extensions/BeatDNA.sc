/*
BeatDNA class, creates the beat sequences
and handles the interactive genetic algorithm.

With a population size of six, it creates six
randomly generated beat sequences per instrument.
Instruments Include:
kick, snare, closed hi hat, ride, tom, clap, percussion 1 & 2.

@kickSample = Folder of Kick samples
@...(rest follow by example)

Part of the Beat Genetics Project.
© 2013-2014, Candidate Number 19329
Generative Creativity, University of Sussex, Spring 2014
*/

BeatDNA {

	classvar <>fitnessScores;
	classvar <>kickPatterns;
	classvar <>snarePatterns;
	classvar <>closedHatsPatterns;
	classvar <>ridePatterns;
	classvar <>tomPatterns;
	classvar <>clapPatterns;
	classvar <>perc1Patterns;
	classvar <>perc2Patterns;
	classvar nextGeneration;

	*new{arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;
		^super.newCopyArgs.createDNA(kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;);
	}

	createDNA {arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;

		// Kick
		var kickBeats = List();
		var kickSequences;
		var kickMatingPool = List();
		var kickChildren = List();
		// Snare
		var snareBeats = List();
		var snareSequences;
		var snareMatingPool = List();
		var snareChildren = List();
		// Closed Hats
		var closedHatsBeats = List();
		var closedHatsSequences;
		var closedHatsMatingPool = List();
		var closedHatsChildren = List();
		// Ride
		var rideBeats = List();
		var rideSequences;
		var rideMatingPool = List();
		var rideChildren = List();
		// Tom
		var tomBeats = List();
		var tomSequences;
		var tomMatingPool = List();
		var tomChildren = List();
		// Clap
		var clapBeats = List();
		var clapSequences;
		var clapMatingPool = List();
		var clapChildren = List();
		// Percussion 1
		var perc1Beats = List();
		var perc1Sequences;
		var perc1MatingPool = List();
		var perc1Children = List();
		// Percussion 2
		var perc2Beats = List();
		var perc2Sequences;
		var perc2MatingPool = List();
		var perc2Children = List();
		// Populations
		var populationSize = 6;
		// Fitness
		var totalFitness;
		var matingConstant;
		// Functions
		var createBeatDNA;
		var assignSynths;
		var fitnessFunction;
		var reproduce;
		// get samples
		var bufferSamples;
		// server
		var s;

		// Init the SynthDefs
		assignSynths = {
			SynthDef(\rideSample2, {arg out = 0, bufnum, amp = 0.5, level = 1, attack = 0.01, release = 1, sustain = 3, decay = 0.7;
				var signal;
				var env;
				env = EnvGen.kr(Env.adsr(attack, decay,  sustain, release, 1), 1, level, doneAction:2);
				signal = PlayBuf.ar(1, bufnum)*env*amp;
				Out.ar(out, Pan2.ar(signal*env));
			}).add;

			SynthDef(\kickSample2, {arg out = 0, bufnum, amp = 0.5, level = 1, attack = 0.01;
				var signal;
				var env;
				env = EnvGen.kr(Env.adsr(attack, 0.2,  0, 1, 1), 1, level, doneAction:2);
				signal = PlayBuf.ar(1, bufnum)*env*amp;
				Out.ar(out, Pan2.ar(signal*env));
			}).add;

			bufferSamples = StoreSampleBuffers(kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples);
		};

		// INIT the beat DNA population
		createBeatDNA = {

			// Set fitness scores with 0
			fitnessScores = Array.fill(7, {0.1});

			// Set beat patterns
			kickPatterns = List();
			snarePatterns = List();
			closedHatsPatterns = List();
			ridePatterns = List();
			tomPatterns = List();
			clapPatterns = List();
			perc1Patterns = List();
			perc2Patterns = List();

			// Fill kick beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Kick Beat Durations : " + i + ": " + random).postln;
				kickBeats.add(Array.fill(random, {rrand(0, 1)}));
				kickBeats.add(Array.fill(random, {4/random}));
			};
			// Fill snare beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Snare Beat Durations : " + i + ": " + random).postln;
				snareBeats.add(Array.fill(random, {rrand(0, 1)}));
				snareBeats.add(Array.fill(random, {4/random}));
			};
			// Fill closed hat beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Closed Hat Beat Durations : " + i + ": " + random).postln;
				closedHatsBeats.add(Array.fill(random, {rrand(0, 1)}));
				closedHatsBeats.add(Array.fill(random, {4/random}));
			};
			// Fill ride beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Ride Beat Durations : " + i + ": " + random).postln;
				rideBeats.add(Array.fill(random, {rrand(0, 1)}));
				rideBeats.add(Array.fill(random, {4/random}));
			};
			// Fill tom beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Tom Beat Durations : " + i + ": " + random).postln;
				tomBeats.add(Array.fill(random, {rrand(0, 1)}));
				tomBeats.add(Array.fill(random, {4/random}));
			};
			// Fill clap beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("Clap Beat Durations : " + i + ": " + random).postln;
				clapBeats.add(Array.fill(random, {rrand(0, 1)}));
				clapBeats.add(Array.fill(random, {4/random}));
			};
			// Fill perc1 beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("perc1 Beat Durations : " + i + ": " + random).postln;
				perc1Beats.add(Array.fill(random, {rrand(0, 1)}));
				perc1Beats.add(Array.fill(random, {4/random}));
			};
			// Fill perc2 beats with random levels and random durations
			populationSize.do{arg i;
				var random = rrand(1, 32);
				("perc2 Beat Durations : " + i + ": " + random).postln;
				perc2Beats.add(Array.fill(random, {rrand(0, 1)}));
				perc2Beats.add(Array.fill(random, {4/random}));
			};

			// Store all the beat sequences
			kickSequences = Array.fill(kickBeats.size, {arg i; Pseq(kickBeats.at(i), inf)});
			snareSequences = Array.fill(snareBeats.size, {arg i; Pseq(snareBeats.at(i), inf)});
			closedHatsSequences = Array.fill(closedHatsBeats.size, {arg i; Pseq(closedHatsBeats.at(i), inf)});
			rideSequences = Array.fill(rideBeats.size, {arg i; Pseq(rideBeats.at(i), inf)});
			tomSequences = Array.fill(tomBeats.size, {arg i; Pseq(tomBeats.at(i), inf)});
			clapSequences = Array.fill(clapBeats.size, {arg i; Pseq(clapBeats.at(i), inf)});
			perc1Sequences = Array.fill(perc1Beats.size, {arg i; Pseq(perc1Beats.at(i), inf)});
			perc2Sequences = Array.fill(perc2Beats.size, {arg i; Pseq(perc2Beats.at(i), inf)});

			// Store Pbinds
			6.do{arg i;
				// Kick Pbinds
				kickPatterns.add(Pbind(
					\instrument, \kickSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getKicks.choose,
					\attack, 0.01,
					\level, Pseq ([kickSequences[i*2]]),
					\dur, Pseq ([kickSequences[i*2+1]]),
				));
				// Snare pbinds
				snarePatterns.add(Pbind(
					\instrument, \kickSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getSnares.choose,
					\attack, 0.01,
					\level, Pseq ([snareSequences[i*2]]),
					\dur, Pseq ([snareSequences[i*2+1]]),
				));
				// hat pbinds
				closedHatsPatterns.add(Pbind(
					\instrument, \rideSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getClosedHats.choose,
					\attack, 0.01,
					\level, Pseq ([closedHatsSequences[i*2]]),
					\dur, Pseq ([closedHatsSequences[i*2+1]]),
				));
				// Ride samples
				ridePatterns.add(Pbind(
					\instrument, \rideSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getRides.choose,
					\attack, 0.01,
					\level, Pseq ([rideSequences[i*2]]),
					\dur, Pseq ([rideSequences[i*2+1]]),
				));
				// tom samples
				tomPatterns.add(Pbind(
					\instrument, \rideSample2,
					\amp, 0.2,
					\bufnum, bufferSamples.getToms.choose,
					\attack, 0.01,
					\level, Pseq ([tomSequences[i*2]]),
					\dur, Pseq ([tomSequences[i*2+1]]),
				));
				// clap samples
				clapPatterns.add(Pbind(
					\instrument, \kickSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getClaps.choose,
					\attack, 0.01,
					\level, Pseq ([clapSequences[i*2]]),
					\dur, Pseq ([clapSequences[i*2+1]]),
				));
				// perc1 samples
				perc1Patterns.add(Pbind(
					\instrument, \kickSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getPerc1.choose,
					\attack, 0.01,
					\level, Pseq ([perc1Sequences[i*2]]),
					\dur, Pseq ([perc1Sequences[i*2+1]]),
				));
				// perc2 samples
				perc2Patterns.add(Pbind(
					\instrument, \kickSample2,
					\amp, 0.5,
					\bufnum, bufferSamples.getPerc2.choose,
					\attack, 0.01,
					\level, Pseq ([perc2Sequences[i*2]]),
					\dur, Pseq ([perc2Sequences[i*2+1]]),
				));
			};

			// Reproduce (Choose best parents for mating pool, create beat children from mating pool)
			//@instBeats = The beat array to reset for specific instrument
			//@instMatingPool = The mating pool for specific instrument
			//@instChildren = The children for specific instrument
			reproduce = {arg instBeats, instMatingPool, instChildren;
				var parentA;
				var parentB;
				var childSizeLevel;
				var largestLevel, smallestLevel;
				var largestDuration, smallestDuration;
				var smallestParentLevel, smallestParentDuration;
				var childSizeDuration;
				var childLevelArray;
				var childDurationArray;
				var child;
				var sum;

				// Reset mating pool
				instMatingPool = List();

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Fitness Function below:

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Add up all the fitness scores
				totalFitness = fitnessScores.sum;
				// Divide each score by the total Fitness
				fitnessScores.do{arg i, j;
					fitnessScores[j] = (fitnessScores[j]/totalFitness*6);
				};
				// Normalise the fitness scores
				fitnessScores = fitnessScores/fitnessScores.sum;

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Mating Pool: (0-1 = beat1) (2-3 = beat2) (4-5 = beat3) etc

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Create a mating pool according to fitnesss weighting
				matingConstant = 0;
				fitnessScores.size.do{arg i;
					var n = (fitnessScores[i]*6);
					matingConstant = matingConstant+n;
					n.do{arg j;
						instMatingPool.add([instBeats[i*2], instBeats[i*2+1]]);
					};
				};

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Reproduction: (Choosing parents and creating beat Children)

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Choose parents at random and choose child's level and duration psequence size
				parentA = instMatingPool.choose;
				parentB = instMatingPool.choose;
				childSizeLevel = [parentA[0].size, parentB[0].size].choose;
				childSizeDuration = [parentA[1].size, parentB[1].size].choose;
				childLevelArray = List();
				childDurationArray = List();
				child = List();
				sum = 0;

				// Assign largest/Smallest level size
				case
				{parentA[0].size >= parentB[0].size} {
					largestLevel = parentA[0].size;
					smallestLevel = parentB[0].size;
					smallestParentLevel = "b";
				}
				{parentA[0].size < parentB[0].size} {
					largestLevel = parentB[0].size;
					smallestLevel = parentA[0].size;
					smallestParentLevel = "a";
				};
				// Assign largest/Smallest Duration size
				case
				{parentA[1].size >= parentB[1].size} {
					largestDuration = parentA[1].size;
					smallestDuration = parentB[1].size;
					smallestParentDuration = "b";
				}
				{parentA[1].size < parentB[1].size} {
					largestDuration = parentB[1].size;
					smallestDuration = parentA[1].size;
					smallestParentDuration = "a";
				};

				// Assign Level values (restricted to the size of child level array length)
				largestLevel.do{arg i;
					// If smaller than the child level size
					case
					{i < childSizeLevel} {
						// If less than the smallest array then randomly choose between both
						case
						{i < smallestLevel} {
							childLevelArray.add([parentA[0][i], parentB[0][i]].choose);
						}
						// Else exceeded smallest array, determine the longest and just use this
						{
							case
							{smallestParentLevel == "a"} {
								childLevelArray.add(parentB[0][i]);
							}
							{
								childLevelArray.add(parentA[0][i]);
							}
						};
					};
				};

				// Assign duration values (restricted to the size of child duration array length)
				largestDuration.do{arg i;
					case
					// If smaller than the child level size
					{i < childSizeDuration} {
						case
						// If less than the smallest array then randomly choose between both
						{i < smallestDuration} {
							childDurationArray.add([parentA[1][i], parentB[1][i]].choose);
						}
						// Else exceeded smallest array, determine the longest and just use this
						{
							case
							{smallestParentDuration == "a"} {
								childDurationArray.add(parentB[1][i]);
							}
							{
								childDurationArray.add(parentA[1][i]);
							}
						};
					};
				};

				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Mutation: with 25% chance (increase the first value in rrand, for higher probability)

				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Mutate the levels
				childLevelArray.size.do{arg i;
					case
					{rrand(25, 100) == 1} {childLevelArray[i] = rrand(0, 1); "Mutating Levels".postln;};
				};

				// Mutate the Durations
				childDurationArray.size.do{arg i;
					case
					{rrand(25, 100) == 1} {childDurationArray[i] = 4/rrand(1, 32); "Mutating Durations".postln;};
				};

				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Quantise the levels and durations to make sure both are the same length, and that the beat sums to 4 beats worth.

				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// If the sum of the durations is smaller than 4 (increase)
				case
				{childDurationArray.sum < 4} {
					var diff;
					diff = 4-childDurationArray.sum;
					childDurationArray = childDurationArray++diff;
				}
				// If the sum of the durations is bigger than 4 (decrease)
				{childDurationArray.sum > 4} {
					while({childDurationArray.sum > 4}, {
						childDurationArray.removeAt(childDurationArray.size-1);
					});
					case
					{childDurationArray.sum < 4} {
						var diff;
						diff = 4-childDurationArray.sum;
						childDurationArray = childDurationArray++diff;
					};
				};

				// If the size of the levels is smaller than the size of durations (increase)
				case
				{childLevelArray.size < childDurationArray.size} {
					while({childLevelArray.size < childDurationArray.size}, {
						childLevelArray = childLevelArray++1;
					});
				}
				// If the size of the levels is bigger than the size of the durations (decrease)
				{childLevelArray.size > childDurationArray.size} {
					while({childLevelArray.size > childDurationArray.size}, {
						childLevelArray = childLevelArray.removeAt(childLevelArray.size-1);
					});
				};

				// After removing, values won't always stay in array form. So put back into an array and flatten.
				childLevelArray = Array.fill(1, {childLevelArray.asArray});
				childLevelArray = childLevelArray.flatten(1);
				childDurationArray = Array.fill(1, {childDurationArray.asArray});
				childDurationArray = childDurationArray.flatten(1);

				// Add the child to the kickChildren array
				instChildren.add(childLevelArray);
				instChildren.add(childDurationArray);
			};

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// Next Generation

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// Next Generation function
			nextGeneration = {

				"Next generation".postln;

				// Reproduce method call (Mate Beats and make children)
				populationSize.do{arg i;
					reproduce.value(kickBeats, kickMatingPool, kickChildren);
					reproduce.value(snareBeats, snareMatingPool, snareChildren);
					reproduce.value(closedHatsBeats, closedHatsMatingPool, closedHatsChildren);
					reproduce.value(rideBeats, rideMatingPool, rideChildren);
					reproduce.value(tomBeats, tomMatingPool, tomChildren);
					reproduce.value(clapBeats, clapMatingPool, clapChildren);
					reproduce.value(perc1Beats, perc1MatingPool, perc1Children);
					reproduce.value(perc2Beats, perc2MatingPool, perc2Children);
				};

				// Reset populations
				kickBeats = List();
				snareBeats = List();
				closedHatsBeats = List();
				rideBeats = List();
				tomBeats = List();
				clapBeats = List();
				perc1Beats = List();
				perc2Beats = List();

				// Replace the population with new children
				(populationSize*2).do{arg i;
					kickBeats.add(kickChildren[i]);
					snareBeats.add(snareChildren[i]);
					closedHatsBeats.add(closedHatsChildren[i]);
					rideBeats.add(rideChildren[i]);
					tomBeats.add(tomChildren[i]);
					clapBeats.add(clapChildren[i]);
					perc1Beats.add(perc1Children[i]);
					perc2Beats.add(perc2Children[i]);
				};

				// Reset the children
				kickChildren = List();
				snareChildren = List();
				closedHatsChildren = List();
				rideChildren = List();
				tomChildren = List();
				clapChildren = List();
				perc1Children = List();
				perc2Children = List();

				// Store/Reset all the beat sequences
				kickSequences = Array.fill(kickBeats.size, {arg i; Pseq(kickBeats.at(i), inf)});
				snareSequences = Array.fill(snareBeats.size, {arg i; Pseq(snareBeats.at(i), inf)});
				closedHatsSequences = Array.fill(closedHatsBeats.size, {arg i; Pseq(closedHatsBeats.at(i), inf)});
				rideSequences = Array.fill(rideBeats.size, {arg i; Pseq(rideBeats.at(i), inf)});
				tomSequences = Array.fill(tomBeats.size, {arg i; Pseq(tomBeats.at(i), inf)});
				clapSequences = Array.fill(clapBeats.size, {arg i; Pseq(clapBeats.at(i), inf)});
				perc1Sequences = Array.fill(perc1Beats.size, {arg i; Pseq(perc1Beats.at(i), inf)});
				perc2Sequences = Array.fill(perc2Beats.size, {arg i; Pseq(perc2Beats.at(i), inf)});

				// Reset patterns
				kickPatterns = List();
				snarePatterns = List();
				closedHatsPatterns = List();
				ridePatterns = List();
				tomPatterns = List();
				clapPatterns = List();
				perc1Patterns = List();
				perc2Patterns = List();

				// Store Pbinds
				6.do{arg i;
					// Kick Pbinds
					kickPatterns.add(Pbind(
						\instrument, \kickSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getKicks.choose,
						\attack, 0.01,
						\level, Pseq ([kickSequences[i*2]]),
						\dur, Pseq ([kickSequences[i*2+1]]),
					));
					// Snare pbinds
					snarePatterns.add(Pbind(
						\instrument, \kickSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getSnares.choose,
						\attack, 0.01,
						\level, Pseq ([snareSequences[i*2]]),
						\dur, Pseq ([snareSequences[i*2+1]]),
					));
					// hat pbinds
					closedHatsPatterns.add(Pbind(
						\instrument, \rideSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getClosedHats.choose,
						\attack, 0.01,
						\level, Pseq ([closedHatsSequences[i*2]]),
						\dur, Pseq ([closedHatsSequences[i*2+1]]),
					));
					// Ride samples
					ridePatterns.add(Pbind(
						\instrument, \rideSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getRides.choose,
						\attack, 0.01,
						\level, Pseq ([rideSequences[i*2]]),
						\dur, Pseq ([rideSequences[i*2+1]]),
					));
					// tom samples
					tomPatterns.add(Pbind(
						\instrument, \rideSample2,
						\amp, 0.2,
						\bufnum, bufferSamples.getToms.choose,
						\attack, 0.01,
						\level, Pseq ([tomSequences[i*2]]),
						\dur, Pseq ([tomSequences[i*2+1]]),
					));
					// clap samples
					clapPatterns.add(Pbind(
						\instrument, \kickSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getClaps.choose,
						\attack, 0.01,
						\level, Pseq ([clapSequences[i*2]]),
						\dur, Pseq ([clapSequences[i*2+1]]),
					));
					// perc1 samples
					perc1Patterns.add(Pbind(
						\instrument, \kickSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getPerc1.choose,
						\attack, 0.01,
						\level, Pseq ([perc1Sequences[i*2]]),
						\dur, Pseq ([perc1Sequences[i*2+1]]),
					));
					// perc2 samples
					perc2Patterns.add(Pbind(
						\instrument, \kickSample2,
						\amp, 0.5,
						\bufnum, bufferSamples.getPerc2.choose,
						\attack, 0.01,
						\level, Pseq ([perc2Sequences[i*2]]),
						\dur, Pseq ([perc2Sequences[i*2+1]]),
					));
				};

				// Reset the fitness scores
				fitnessScores.do{arg i; i = 0};
			};
		};

		// Boot server then run functions in order
		s = Server.local;
		s.boot.doWhenBooted{
			assignSynths.defer(0.01);
			createBeatDNA.defer(0.03);
		};
	}

	nextGen {
		"Fitness values".postln;
		fitnessScores.postln;
		nextGeneration.value;
	}


	getFitness{
		"Current Fitness Scores".postln;
		^fitnessScores;
	}

	setFitness{arg fitnessNum, fitnessScore;
		fitnessScores[fitnessNum] = fitnessScore;
	}

	getKickPinds {
		^kickPatterns;
	}

	getSnarePinds {
		^snarePatterns;
	}

	getClosedHatsPinds {
		^closedHatsPatterns;
	}

	getRidePinds {
		^ridePatterns;
	}

	getTomPinds {
		^tomPatterns;
	}

	getClapPinds {
		^clapPatterns;
	}

	getPerc1Pinds {
		^perc1Patterns;
	}

	getPerc2Pinds {
		^perc2Patterns;
	}
}

/*
BeatGenetics class, the GUI and core class all others
are called from.

Creates a GUI and ability to play each beat in turn.
Next generation button, creates the new generation of beats.
Fitness is stored using TempoClock.

@kickSample = Folder of Kick samples
@...(rest follow by example)

Part of the Beat Genetics Project.
© 2013-2014, Candidate Number 19329
Generative Creativity, University of Sussex, Spring 2014
*/

BeatGenetics {

	*new{arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;
		^super.newCopyArgs.createBeatGenetics(kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;);
	}

	createBeatGenetics {arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;

		// Views
		var win;
		var dividecvs;
		var beatAcrosscv1;
		var beatAcrosscv2;
		// Text
		var beatText;
		var beatText2;
		var genText;
		var genText2;
		var infoText;
		// Font
		var font;
		var font2;
		var font3;
		// Buttons
		var beatButtons = Array.fill(3, {0});
		var beatButtons2 = Array.fill(3, {0});
		var nextGenButton;
		var genNum = 0;
		// Time spent on each beat
		var fitnessScores = Array.fill(6, {0});
		var beatOn = false;
		var timeClocks = Array.fill(6, {});
		// Beat DNA Class
		var beatDNA;
		// Beat Pbind assigners (0 = kick, 1 = snare etc)
		var instrumentPbinds = Array.fill(10, {Array.fill(10, {})});
		// functions
		var initGUI;
		var createBeatDNA;
		// server
		var s;

		// Call beatDNA class to store samples as buffers
		createBeatDNA = {
			beatDNA = BeatDNA(kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;);
		};

		// Create GUI
		initGUI = {

			// set fonts
			font = Font("Chalkduster", 20, true);
			font2 = Font("Chalkduster", 25, true);
			font3 = Font("Chalkduster", 15, false);

			// Views
			win = Window("Beat Genetics", Rect(450, 250, 800, 600));
			dividecvs = Array.fill(3, {arg i; CompositeView(win, Rect(0, 0+(i*win.bounds.height/3), win.bounds.width, win.bounds.height/3))});
			beatAcrosscv1 = Array.fill(3, {arg i; CompositeView(dividecvs[0], Rect(0+(i*dividecvs[0].bounds.width/3), 0, dividecvs[0].bounds.width/3, dividecvs[0].bounds.height))});
			beatAcrosscv2 = Array.fill(3, {arg i; CompositeView(dividecvs[1], Rect(0+(i*dividecvs[1].bounds.width/3), 0, dividecvs[1].bounds.width/3, dividecvs[1].bounds.height))});

			// Text
			beatText = Array.fill(3, {arg i; StaticText(beatAcrosscv1[i], Rect(30, 30, 100, 20))});
			beatText2 = Array.fill(3, {arg i; StaticText(beatAcrosscv2[i], Rect(30, 30, 100, 20))});
			genText = Array.fill(3, {arg i; StaticText(beatAcrosscv1[i], Rect(30, 80, 100, 20))});
			genText2 = Array.fill(3, {arg i; StaticText(beatAcrosscv2[i], Rect(30, 80, 100, 20))});

			// Set text string and font
			beatText.do{arg i, j;
				i.string = "Beat" + (j+1);
				i.font = font2;
			};
			beatText2.do{arg i, j;
				i.string = "Beat" + (j+4);
				i.font = font2;
			};
			genText.do{arg i;
				i.string = "Gen" + (genNum+1);
				i.font = font;
			};
			genText2.do{arg i;
				i.string = "Gen" + (genNum+1);
				i.font = font;
			};

			// Info text
			infoText = StaticText(dividecvs[2], Rect(150, 0, 300, 200)).align_(\center);
			infoText.font = font;
			infoText.string = "Welcome to Beat Genetics! Time listened per beat = fitness score. (Click Next Gen to reproduce)";

			// GUI Colours
			dividecvs.do{arg i;
				i.background_(Color.rand);
			};
			beatAcrosscv1.do{arg i;
				i.background_(Color.rand);
			};
			beatAcrosscv2.do{arg i;
				i.background_(Color.rand);
			};

			// Play Buttons
			3.do{arg j;
				beatButtons[j] = Array.fill(2, {arg i; Button(beatAcrosscv1[j], Rect(beatAcrosscv1[j].bounds.width/10+(i*beatAcrosscv1[j].bounds.width/2.5), beatAcrosscv1[j].bounds.height/1.5, beatAcrosscv1[j].bounds.width/3, beatAcrosscv1[j].bounds.height/4))});
			};
			3.do{arg j;
				beatButtons2[j] = Array.fill(2, {arg i; Button(beatAcrosscv2[j], Rect(beatAcrosscv2[j].bounds.width/10+(i*beatAcrosscv2[j].bounds.width/2.5), beatAcrosscv2[j].bounds.height/1.5, beatAcrosscv2[j].bounds.width/3, beatAcrosscv2[j].bounds.height/4))});
			};
			3.do{arg i;
				beatButtons[i][0].states_([["Play", Color.white, Color.grey]]);
				beatButtons[i][0].font = font3;
				beatButtons2[i][0].states_([["Play", Color.white, Color.grey]]);
				beatButtons2[i][0].font = font3;
			};
			3.do{arg i;
				beatButtons[i][1].states_([["Stop", Color.white, Color.grey]]);
				beatButtons[i][1].font = font3;
				beatButtons2[i][1].states_([["Stop", Color.white, Color.grey]]);
				beatButtons2[i][1].font = font3;
			};

			// Nextgen button
			nextGenButton = Button(dividecvs[2], Rect(dividecvs[2].bounds.width-beatAcrosscv1[0].bounds.width+10, 20, beatAcrosscv1[0].bounds.width-20, dividecvs[2].bounds.height-40));
			nextGenButton.states_([["Next
				Generation", Color.white, Color.grey]]);
			nextGenButton.font = font2;

			// Next gen action
			nextGenButton.action = {arg b;
				case
				// Execute only when all beats have stopped
				{beatOn != true} {

					// Call beatDNA next Gen
					beatDNA.nextGen;

					// Free all synths for next round
					s.freeAll;

					// Reset fitness scores
					fitnessScores.size.do{arg i; fitnessScores[i] = 0};
					6.do{arg i; beatDNA.setFitness(i, 0)};

					// Update gen numbers for text
					genNum = genNum + 1;
					genText.do{arg i;
						i.string = "Gen" + (genNum+1);
					};
					genText2.do{arg i;
						i.string = "Gen" + (genNum+1);
					};

					// Colour Update
					dividecvs.do{arg i;
						i.background_(Color.rand);
					};

					beatAcrosscv1.do{arg i;
						i.background_(Color.rand);
					};

					beatAcrosscv2.do{arg i;
						i.background_(Color.rand);
					};

					"GUI generation".postln;
					"Fitness Scores".postln;
					fitnessScores.postln;

				} {"Can't Create Next Generation Until Beat is stopped".postln;};
			};

			// Time storing for each beat
			3.do{arg i;
				beatButtons[i][0].action = {arg b;
					case
					{beatOn != true} {
						case
						// Beat 1:
						{b == beatButtons[0][0]} {
							// Play beat 1 pbinds
							instrumentPbinds[0][0] = beatDNA.getKickPinds[0].play;
							instrumentPbinds[1][0] = beatDNA.getSnarePinds[0].play;
							instrumentPbinds[2][0] = beatDNA.getClosedHatsPinds[0].play;
							instrumentPbinds[3][0] = beatDNA.getRidePinds[0].play;
							instrumentPbinds[4][0] = beatDNA.getTomPinds[0].play;
							instrumentPbinds[5][0] = beatDNA.getClapPinds[0].play;
							instrumentPbinds[6][0] = beatDNA.getPerc1Pinds[0].play;
							instrumentPbinds[7][0] = beatDNA.getPerc2Pinds[0].play;

							// Track Listening time for fitness
							timeClocks[0] = TempoClock(10);
							timeClocks[0].schedAbs(timeClocks[0].beats.ceil, { arg beat, sec;
								fitnessScores[0] = fitnessScores[0] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(0, fitnessScores[0]);
							beatDNA.getFitness;
						}
						{b == beatButtons[1][0]} {
							// Play beat 2 pbinds
							instrumentPbinds[0][1] = beatDNA.getKickPinds[1].play;
							instrumentPbinds[1][1] = beatDNA.getSnarePinds[1].play;
							instrumentPbinds[2][1] = beatDNA.getClosedHatsPinds[1].play;
							instrumentPbinds[3][1] = beatDNA.getRidePinds[1].play;
							instrumentPbinds[4][1] = beatDNA.getTomPinds[1].play;
							instrumentPbinds[5][1] = beatDNA.getClapPinds[1].play;
							instrumentPbinds[6][1] = beatDNA.getPerc1Pinds[1].play;
							instrumentPbinds[7][1] = beatDNA.getPerc2Pinds[1].play;

							timeClocks[1] = TempoClock(10);
							timeClocks[1].schedAbs(timeClocks[1].beats.ceil, { arg beat, sec;
								fitnessScores[1] = fitnessScores[1] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(1, fitnessScores[1]);
							beatDNA.getFitness;
						}
						{b == beatButtons[2][0]} {

							// Play beat 3 pbinds
							instrumentPbinds[0][2] = beatDNA.getKickPinds[2].play;
							instrumentPbinds[1][2] = beatDNA.getSnarePinds[2].play;
							instrumentPbinds[2][2] = beatDNA.getClosedHatsPinds[2].play;
							instrumentPbinds[3][2] = beatDNA.getRidePinds[2].play;
							instrumentPbinds[4][2] = beatDNA.getTomPinds[2].play;
							instrumentPbinds[5][2] = beatDNA.getClapPinds[2].play;
							instrumentPbinds[6][2] = beatDNA.getPerc1Pinds[2].play;
							instrumentPbinds[7][2] = beatDNA.getPerc2Pinds[2].play;

							timeClocks[2] = TempoClock(10);
							timeClocks[2].schedAbs(timeClocks[2].beats.ceil, { arg beat, sec;
								fitnessScores[2] = fitnessScores[2] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(2, fitnessScores[2]);
							beatDNA.getFitness;
						};
						"on".postln;
						beatOn = true;
						fitnessScores.postln;
					};
				};
			};

			// Time stopping for each beat
			3.do{arg i;
				beatButtons[i][1].action = {arg b;
					case
					{beatOn == true} {
						case
						{b == beatButtons[0][1]} {

							// Stop beat 1:
							8.do{arg i;
								instrumentPbinds[i][0].stop;
							};
							s.freeAll;
							timeClocks[0].stop;
							fitnessScores[0].postln;
						}
						{b == beatButtons[1][1]} {

							// Stop beat 2:
							8.do{arg i;
								instrumentPbinds[i][1].stop;
							};
							s.freeAll;
							timeClocks[1].stop;
							fitnessScores[1].postln;
						}
						{b == beatButtons[2][1]} {

							// Stop beat 3:
							8.do{arg i;
								instrumentPbinds[i][2].stop;
							};
							s.freeAll;
							timeClocks[2].stop;
							fitnessScores[2].postln;
						};
						"off".postln;
						beatOn = false;
					};
				};
			};

			// Time storing for each beat (second row)
			3.do{arg i;
				beatButtons2[i][0].action = {arg b;
					case
					{beatOn != true} {
						case
						{b == beatButtons2[0][0]} {

							// Play beat 4 pbinds
							instrumentPbinds[0][3] = beatDNA.getKickPinds[3].play;
							instrumentPbinds[1][3] = beatDNA.getSnarePinds[3].play;
							instrumentPbinds[2][3] = beatDNA.getClosedHatsPinds[3].play;
							instrumentPbinds[3][3] = beatDNA.getRidePinds[3].play;
							instrumentPbinds[4][3] = beatDNA.getTomPinds[3].play;
							instrumentPbinds[5][3] = beatDNA.getClapPinds[3].play;
							instrumentPbinds[6][3] = beatDNA.getPerc1Pinds[3].play;
							instrumentPbinds[7][3] = beatDNA.getPerc2Pinds[3].play;

							timeClocks[3] = TempoClock(10);
							timeClocks[3].schedAbs(timeClocks[3].beats.ceil, { arg beat, sec;
								fitnessScores[3] = fitnessScores[3] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(3, fitnessScores[3]);
							beatDNA.getFitness;
						}
						{b == beatButtons2[1][0]} {

							// Play beat 5 pbinds
							instrumentPbinds[0][4] = beatDNA.getKickPinds[4].play;
							instrumentPbinds[1][4] = beatDNA.getSnarePinds[4].play;
							instrumentPbinds[2][4] = beatDNA.getClosedHatsPinds[4].play;
							instrumentPbinds[3][4] = beatDNA.getRidePinds[4].play;
							instrumentPbinds[4][4] = beatDNA.getTomPinds[4].play;
							instrumentPbinds[5][4] = beatDNA.getClapPinds[4].play;
							instrumentPbinds[6][4] = beatDNA.getPerc1Pinds[4].play;
							instrumentPbinds[7][4] = beatDNA.getPerc2Pinds[4].play;

							timeClocks[4] = TempoClock(10);
							timeClocks[4].schedAbs(timeClocks[4].beats.ceil, { arg beat, sec;
								fitnessScores[4] = fitnessScores[4] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(4, fitnessScores[4]);
							beatDNA.getFitness;
						}
						{b == beatButtons2[2][0]} {

							// Play beat 6 pbinds
							instrumentPbinds[0][5] = beatDNA.getKickPinds[5].play;
							instrumentPbinds[1][5] = beatDNA.getSnarePinds[5].play;
							instrumentPbinds[2][5] = beatDNA.getClosedHatsPinds[5].play;
							instrumentPbinds[3][5] = beatDNA.getRidePinds[5].play;
							instrumentPbinds[4][5] = beatDNA.getTomPinds[5].play;
							instrumentPbinds[5][5] = beatDNA.getClapPinds[5].play;
							instrumentPbinds[6][5] = beatDNA.getPerc1Pinds[5].play;
							instrumentPbinds[7][5] = beatDNA.getPerc2Pinds[5].play;

							timeClocks[5] = TempoClock(10);
							timeClocks[5].schedAbs(timeClocks[5].beats.ceil, { arg beat, sec;
								fitnessScores[5] = fitnessScores[5] + beat; 1;
							});

							// Set fitness scores
							beatDNA.setFitness(5, fitnessScores[5]);
						};
						"on".postln;
						beatOn = true;
					};
				};
			};

			// Time stopping for each beat (second row)
			3.do{arg i;
				beatButtons2[i][1].action = {arg b;
					case
					{beatOn == true} {
						case
						{b == beatButtons2[0][1]} {

							// Stop beat 4:
							8.do{arg i;
								instrumentPbinds[i][3].stop;
							};
							s.freeAll;
							timeClocks[3].stop;
							fitnessScores[3].postln;
						}
						{b == beatButtons2[1][1]} {

							// Stop beat 5:
							8.do{arg i;
								instrumentPbinds[i][4].stop;
							};
							s.freeAll;
							timeClocks[4].stop;
							fitnessScores[4].postln;
						}
						{b == beatButtons2[2][1]} {

							// Stop beat 6:
							8.do{arg i;
								instrumentPbinds[i][5].stop;
							};
							s.freeAll;
							timeClocks[5].stop;
							fitnessScores[5].postln;
						};
						"off".postln;
						beatOn = false;
					};
				};
			};

			// Free synths on close
			win.onClose_({
				8.do{arg i;
					instrumentPbinds[i][0].stop;
					instrumentPbinds[i][1].stop;
					instrumentPbinds[i][2].stop;
					instrumentPbinds[i][3].stop;
					instrumentPbinds[i][4].stop;
					instrumentPbinds[i][5].stop;
				};
				s.freeAll});

			// Display Window
			win.front;
		};

		// Execute functions in order
		s = Server.local;
		s.boot.doWhenBooted{
			createBeatDNA.defer(0.01);
			initGUI.defer(0.3);
		};
	}
}
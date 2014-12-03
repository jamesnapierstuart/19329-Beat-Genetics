/*
StoreSampleBuffers class Stores the samples in Buffer arrays, for system to randomly pick from.

@kickSample = Folder of Kick samples
@...(rest follow by example)

Part of the Beat Genetics Project.
© 2013-2014, Candidate Number 19329
Generative Creativity, University of Sussex, Spring 2014
*/

StoreSampleBuffers {

	var s;
	classvar <>kickBuffers;
	classvar <>snareBuffers;
	classvar <>closedHatsBuffers;
	classvar <>rideBuffers;
	classvar <>tomBuffers;
	classvar <>clapBuffers;
	classvar <>perc1Buffers;
	classvar <>perc2Buffers;

	classvar <>kick;
			classvar <>snare;
			classvar <>closedHats;
			classvar <>ride;
			classvar <>tom;
			classvar <>clap;
			classvar <>perc1;
			classvar <>perc2;

	*new{arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;
		^super.newCopyArgs.sampleBuffers(kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples);
	}

	sampleBuffers {arg kickSamples, snareSamples, closedHatsSamples, rideSamples, tomSamples, clapSamples, perc1Samples, perc2Samples;

			// Function Variables
			var getSample;
			// Instrument temp array
			var instrumentTempArray = List();
			kickBuffers = List();
			snareBuffers = List();
			closedHatsBuffers = List();
			rideBuffers = List();
			tomBuffers = List();
			clapBuffers = List();
			perc1Buffers = List();
			perc2Buffers = List();
			// Instrument Variables
			kick = List();
			snare = List();
			closedHats = List();
			ride = List();
			tom = List();
			clap = List();
			perc1 = List();
			perc2 = List();

			// Get sample and assign instrument
			getSample = {arg sampleFolder, instrument;
				// Add * to select all files from the folder
				sampleFolder = sampleFolder ++ "*";
				// Reset the instrument temp Array
				instrumentTempArray = List();

				// Collect all samples in the folder
				sampleFolder.pathMatch.collect{arg i;
					// Store all samples from folder in temp array
					instrumentTempArray.add(i);

					// Randomly Pick a sample from the temp array to be the instrument
					switch(instrument,
						"kick", {kick = instrumentTempArray},
						"snare", {snare = instrumentTempArray},
						"closedHats", {closedHats = instrumentTempArray},
						"ride", {ride = instrumentTempArray},
						"tom", {tom = instrumentTempArray},
						"clap", {clap = instrumentTempArray},
						"perc1", {perc1 = instrumentTempArray},
						"perc2", {perc2 = instrumentTempArray};
					);
				};
			};

			// Kicks
			getSample.value(kickSamples, "kick");
			// Snares
			getSample.value(snareSamples, "snare");
			// closedhats
			getSample.value(closedHatsSamples, "closedHats");
			// ride
			getSample.value(rideSamples, "ride");
			// tom
			getSample.value(tomSamples, "tom");
			// clap
			getSample.value(clapSamples, "clap");
			// perc1
			getSample.value(perc1Samples, "perc1");
			// perc2
			getSample.value(perc2Samples, "perc2");

			// Store buffers
			kick.size.do{arg i;
				kickBuffers.add(Buffer.read(s, kick[i]));
			};
			snare.size.do{arg i;
				snareBuffers.add(Buffer.read(s, snare[i]));
			};
			closedHats.size.do{arg i;
				closedHatsBuffers.add(Buffer.read(s, closedHats[i]));
			};
			ride.size.do{arg i;
				rideBuffers.add(Buffer.read(s, ride[i]));
			};
			tom.size.do{arg i;
				tomBuffers.add(Buffer.read(s, tom[i]));
			};
			clap.size.do{arg i;
				clapBuffers.add(Buffer.read(s, clap[i]));
			};
			perc1.size.do{arg i;
				perc1Buffers.add(Buffer.read(s, perc1[i]));
			};
			perc2.size.do{arg i;
				perc2Buffers.add(Buffer.read(s, perc2[i]));
			};

			// Conform all into arrays
			kickBuffers = Array.fill(1, {kickBuffers.asArray});
			kickBuffers = kickBuffers.flatten(1);
			snareBuffers = Array.fill(1, {snareBuffers.asArray});
			snareBuffers = snareBuffers.flatten(1);
			closedHatsBuffers = Array.fill(1, {closedHatsBuffers.asArray});
			closedHatsBuffers = closedHatsBuffers.flatten(1);
			rideBuffers = Array.fill(1, {rideBuffers.asArray});
			rideBuffers = rideBuffers.flatten(1);
			tomBuffers = Array.fill(1, {tomBuffers.asArray});
			tomBuffers = tomBuffers.flatten(1);
			clapBuffers = Array.fill(1, {clapBuffers.asArray});
			clapBuffers = clapBuffers.flatten(1);
			perc1Buffers = Array.fill(1, {perc1Buffers.asArray});
			perc1Buffers = perc1Buffers.flatten(1);
			perc2Buffers = Array.fill(1, {perc2Buffers.asArray});
			perc2Buffers = perc2Buffers.flatten(1);
	}

	getKicks {
		^kickBuffers;
	}

	getSnares {
		^snareBuffers;
	}

	getClosedHats {
		^closedHatsBuffers;
	}

	getRides {
		^rideBuffers;
	}

	getToms {
		^tomBuffers;
	}

	getClaps {
		^clapBuffers;
	}

	getPerc1 {
		^perc1Buffers;
	}

	getPerc2 {
		^perc2Buffers;
	}

}